package de.confuse;

import de.confuse.abiKlassen.Client;
import de.confuse.confFileV2.ConfFileFieldV2;
import de.confuse.confFileV2.ConfFileReaderV2;
import de.confuse.security.AesUtilities;
import de.confuse.security.RsaUtilities;
import de.confuse.util.CryptoCommunication;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Objects;

public class MessengerClient extends Client
{
	private final MessengerClientMain messengerClientMain;
	/**
	 * Eine HashMap, welche für jeden Benutzernamen zu dem eine <strong>aktuelle</strong> Verbindung besteht, sowohl
	 * die
	 * lokale Verschlüsselung, als auch die zu verwendende Verschlüsselung der gegenseite, in form eines Arrays
	 * speichert.<br>
	 * <strong>Ein Array in dieser Map hat folgende Eigenschaften:</strong>
	 * <ul>
	 *     <li>Länge von 2; [0, 1]</li>
	 *     <li>Keine <code>null</code> Einträge</li>
	 * </ul><br>
	 * <strong>Ordnung des Arrays:</strong>
	 * <ol>
	 *     <li>Lokale Verschlüsselung, die an die Gegenseite gesendet wurde</li>
	 *     <li>Externe Verschlüsselung, die verwendet wird, um Nachrichten zu verschicken</li>
	 * </ol>
	 */
	private final HashMap<String, CryptoCommunication.CryptoStorage[]> direktChatCryptoStorageHashMap;
	/**
	 * Der Name den der Server gegeben hat
	 */
	private String name;
	/**
	 * Speichert die Verschlüsselungsdaten von Client und Server.<br>
	 * <strong>Ordnung des Arrays:</strong>
	 * <ol>
	 *     <li>Lokale Verschlüsselung, die an die Gegenseite gesendet wurde</li>
	 *     <li>Externe Verschlüsselung, die verwendet wird, um Nachrichten zu verschicken</li>
	 * </ol>
	 * Keys werden wie folgt gespeichert:<br>
	 * IP:Port, Bsp.: '127.0.0.1:53241'
	 */
	private CryptoCommunication.CryptoStorage[] serverCryptoStorageArray;

	public MessengerClient(String pServerIP, int pServerPort)
	{
		super(pServerIP, pServerPort);
		this.messengerClientMain = MessengerClientMain.instance;

		// Client Variablen
		this.direktChatCryptoStorageHashMap = new HashMap<>();
		this.serverCryptoStorageArray = new CryptoCommunication.CryptoStorage[2];
	}

	@Override
	public void processMessage(String pMessage)
	{
		System.out.println("---------------MessengerClient.processMessage---------------");
		System.out.println("pMessage = " + pMessage);

		try
		{
			final ConfFileReaderV2 inputReader = new ConfFileReaderV2(pMessage);

			if (inputReader.getField("CryptoHandshake") != null)
			{
				System.out.print("Handshake Stage: ");
				final ConfFileFieldV2 handshake = inputReader.getField("CryptoHandshake");
				final String stage = handshake.getValue("stage");
				final String[] user = handshake.getValues("sender");
				final String formattedUser = user.length > 1 ? user[0] + ':' + user[1] : user[0] + ":000001";

				if (stage.equals("1"))
				{
					System.out.println("1!");
					final PublicKey externerPublicKey = CryptoCommunication.extractHandshakeDataStage1(handshake);
					final KeyPair clientKeyPair = RsaUtilities.generateRsaKeyPair();
					final IvParameterSpec clientIvParameterSpec = AesUtilities.generateIv();
					final SecretKey clientSecretKey = AesUtilities.generateSecretKey(256);

					final CryptoCommunication.CryptoStorage[] cryptoStorages =
							new CryptoCommunication.CryptoStorage[2];
					direktChatCryptoStorageHashMap.putIfAbsent(formattedUser, cryptoStorages);
					direktChatCryptoStorageHashMap.get(formattedUser)[0] =
							new CryptoCommunication.CryptoStorage(clientSecretKey, clientKeyPair.getPublic(),
									clientKeyPair.getPrivate(), clientIvParameterSpec);
					// Erste Datenspeicherung der Client Daten: RSA Schlüssel
					direktChatCryptoStorageHashMap.get(formattedUser)[1] =
							new CryptoCommunication.CryptoStorage(clientSecretKey, externerPublicKey,
									clientIvParameterSpec);

					ConfFileFieldV2 stage2HandshakeOutgoing =
							Objects.requireNonNull(CryptoCommunication.generateCryptoHandshakeStage2(externerPublicKey,
									clientKeyPair.getPublic(), clientSecretKey, clientIvParameterSpec)).put("receiver"
									, user).put("sender", name.split(":"));
					send(stage2HandshakeOutgoing.getFormattedFieldV2(0));
					return;
				}
				else if (stage.equals("2"))
				{
					System.out.println("2!");
					final CryptoCommunication.CryptoStorage fullStorage;

					// Handshake vom Server durch die Anmeldung
					if (handshake.getValue("name") != null && handshake.getValue("sender").equals("server"))
					{
						fullStorage = CryptoCommunication.extractHandshakeDataStage2(handshake,
								serverCryptoStorageArray[0].serverPrivateKey);
						name = handshake.getValue("name");
						serverCryptoStorageArray[1] = fullStorage; // Alle Daten erhalten
					}
					else
					{ // Andere Handshakes, z. B. durch Direktverbindungen
						final CryptoCommunication.CryptoStorage[] cryptoStorages =
								new CryptoCommunication.CryptoStorage[2];
						direktChatCryptoStorageHashMap.putIfAbsent(formattedUser, cryptoStorages);

						fullStorage = CryptoCommunication.extractHandshakeDataStage2(handshake,
								direktChatCryptoStorageHashMap.get(formattedUser)[0].serverPrivateKey);
						// Erste Datenspeicherung der Client Daten: RSA Schlüssel
						direktChatCryptoStorageHashMap.get(formattedUser)[1] = fullStorage;
					}

					assert fullStorage != null;
					final ConfFileFieldV2 stage3HandshakeOutgoing =
							Objects.requireNonNull(CryptoCommunication.generateCryptoHandshakeStage3
											(fullStorage.serverPublicKey, fullStorage.secretKey,
													fullStorage.ivParameterSpec))
									.put("receiver", handshake.getValueObject("sender").getValues())
									.put("sender", name.split(":"));
					send(stage3HandshakeOutgoing.getFormattedFieldV2(0));
					return;
				}
				else if (stage.equals("3"))
				{
					System.out.println("3!");
					// Prüft, ob alles funktioniert hat
					if (CryptoCommunication.extractHandshakeDataStage3(handshake,
							direktChatCryptoStorageHashMap.get(formattedUser)[1],
							direktChatCryptoStorageHashMap.get(formattedUser)[0].serverPrivateKey))
					{
						System.out.println("Stage 3 bestätigt");
					}
					else
					{
						// Schließt verbindung, falls nicht
						System.err.println("Stage 3 Handshake failed");
					}

					return;
				}

			}
			else if (inputReader.getField("CryptoCommunication") != null)
			{
				System.out.println("---------------------------------------------------------------");
				final ConfFileFieldV2 communication = inputReader.getField("CryptoCommunication");
				final String[] user = communication.getValues("sender");
				final String formattedUser = user.length > 1 ? user[0] + ':' + user[1] : user[0] + ":000001";

				final String nachricht = CryptoCommunication.decryptReceivedMessage(communication,
						direktChatCryptoStorageHashMap.get(formattedUser)[1],
						direktChatCryptoStorageHashMap.get(formattedUser)[0].serverPrivateKey);

				// TODO: Interface hierfür erstellen, welches implementiert werden kann?
				System.out.println("nachricht = " + nachricht);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Meldet den Client an
	 */
	public void anmelden()
	{
		System.out.println("Melde an...");
		final KeyPair clientKeyPair = RsaUtilities.generateRsaKeyPair();
		final CryptoCommunication.CryptoStorage lokalerStorage =
				new CryptoCommunication.CryptoStorage(null, clientKeyPair.getPublic(),
						clientKeyPair.getPrivate(), null);
		serverCryptoStorageArray[0] = lokalerStorage;

		// Das Handshake Package
		final ConfFileFieldV2 handshake =
				CryptoCommunication.generateCryptoHandshakeStage1(clientKeyPair.getPublic()).put("receiver", "server");

		send(handshake.getFormattedFieldV2(0));
		System.out.println("Anmeldung gesendet!");
	}

	/**
	 * Verbindet den Client mit einem anderen Client, ende zu Ende verschlüsselt.
	 *
	 * @param name Der Name des Clients
	 */
	public void verbinde(String name)
	{
		System.out.println("---------------MessengerClient.verbinde---------------");
		System.out.println("Verbinde...");
		final KeyPair clientKeyPair = RsaUtilities.generateRsaKeyPair();
		final CryptoCommunication.CryptoStorage lokalerStorage =
				new CryptoCommunication.CryptoStorage(null, clientKeyPair.getPublic(),
						clientKeyPair.getPrivate(), null);
		direktChatCryptoStorageHashMap.put(name, new CryptoCommunication.CryptoStorage[2]);
		direktChatCryptoStorageHashMap.get(name)[0] = lokalerStorage;

		// Das Handshake Package
		final ConfFileFieldV2 handshake =
				CryptoCommunication.generateCryptoHandshakeStage1(clientKeyPair.getPublic()).put("receiver",
						name.split(":")).put("sender", this.name.split(":"));

		send(handshake.getFormattedFieldV2(0));
		System.out.println("Verbindungsversuch gestartet!");
	}

	/**
	 * Sendet eine Nachricht an einen anderen Client mit dem bereits eine Verschlüsselung abgesprochen wurde
	 *
	 * @param name      Der Name des Empfängers
	 * @param nachricht Die Nachricht | Sinnvoll, wenn ebenfalls das ConfFile System benutzt werden würde, um
	 *                  einheitlich zu bleiben
	 * @see #verbinde(String)
	 */
	public void nachricht(String name, String nachricht)
	{
		System.out.println("---------------MessengerClient.nachricht---------------");
		if (!direktChatCryptoStorageHashMap.containsKey(name))
		{
			System.err.println("Unbekannter/Nicht verbundener Nutzer!");
			return;
		}

		final CryptoCommunication.CryptoStorage[] cryptoStorage = direktChatCryptoStorageHashMap.get(name);
		final ConfFileFieldV2 cryptoPacket = Objects.requireNonNull(CryptoCommunication.generateCryptoPacket(nachricht
				, cryptoStorage[1].serverPublicKey,
				cryptoStorage[1].secretKey, cryptoStorage[1].ivParameterSpec)).put("receiver",
				name.split(":")).put("sender", this.name.split(":"));

		send(cryptoPacket.getFormattedFieldV2(0));
		System.out.println("Nachricht gesendet!");
	}

	public HashMap<String, CryptoCommunication.CryptoStorage[]> getDirektChatCryptoStorageHashMap()
	{
		return direktChatCryptoStorageHashMap;
	}

	public CryptoCommunication.CryptoStorage[] getServerCryptoStorageArray()
	{
		return serverCryptoStorageArray;
	}
}
