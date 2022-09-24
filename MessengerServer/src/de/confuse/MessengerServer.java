package de.confuse;

import de.confuse.abiKlassen.Server;
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

public class MessengerServer extends Server
{
	private final MessengerServerMain messengerServerMain;

	/**
	 * Eine HashMap, welche für jeden Benutzernamen zu dem eine <strong>aktuelle</strong> Verbindung besteht, sowohl
	 * die
	 * lokale Verschlüsselung, als auch die zu verwendende Verschlüsselung der gegenseite, in form eines Arrays
	 * speichert.<br>
	 * <strong>Ein Array in dieser Map hat folgende Eigenschaften:</strong>
	 * <ul>
	 *     <li>Länge von 2; [0, 1]</li>
	 *     <li>Keine <code>null</code> Einträge, nach abschluss des Verbindungsaufbaus</li>
	 * </ul><br>
	 * <strong>Ordnung des Arrays:</strong>
	 * <ol>
	 *     <li>Lokale Verschlüsselung, die an die Gegenseite gesendet wurde</li>
	 *     <li>Externe Verschlüsselung, die verwendet wird, um Nachrichten zu verschicken</li>
	 * </ol>
	 * Keys werden wie folgt gespeichert:<br>
	 * IP:Port, Bsp.: '127.0.0.1:53241'
	 */
	private final HashMap<String, CryptoCommunication.CryptoStorage[]> direktChatCryptoStorageHashMap;

	public MessengerServer(int pPort)
	{
		super(pPort);
		this.messengerServerMain = MessengerServerMain.instance;

		// Server Variablen
		this.direktChatCryptoStorageHashMap = new HashMap<>();
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort)
	{
		System.out.println("Connection: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort);
		// Neuer
		direktChatCryptoStorageHashMap.put(pClientIP + ':' + pClientPort, new CryptoCommunication.CryptoStorage[2]);
		send(pClientIP, pClientPort, "Connection: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort);
	}

	// Eingehende Nachrichten
	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage)
	{
		System.out.println("Message: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort + ", pMessage = " + pMessage);

		try
		{
			final ConfFileReaderV2 inputReader = new ConfFileReaderV2(pMessage);
			final String user = pClientIP + ':' + pClientPort;
			final ConfFileFieldV2 field;

			if ((field = inputReader.getField("CryptoHandshake")) != null && field.getValue("receiver").equals(
					"server"))
			{
				final String stage = field.getValue("stage");

				if (stage.equals("1"))
				{
					final PublicKey externerPublicKey = CryptoCommunication.extractHandshakeDataStage1(field);
					final KeyPair clientKeyPair = RsaUtilities.generateRsaKeyPair();
					final IvParameterSpec clientIvParameterSpec = AesUtilities.generateIv();
					final SecretKey clientSecretKey = AesUtilities.generateSecretKey(256);
					direktChatCryptoStorageHashMap.get(user)[0] =
							new CryptoCommunication.CryptoStorage(clientSecretKey, clientKeyPair.getPublic(),
									clientKeyPair.getPrivate(), clientIvParameterSpec);
					// Erste Datenspeicherung der Client Daten: RSA Schlüssel
					direktChatCryptoStorageHashMap.get(user)[1] =
							new CryptoCommunication.CryptoStorage(clientSecretKey, externerPublicKey,
									clientIvParameterSpec);

					// Stellt Stage 2 Handshake zusammen
					final ConfFileFieldV2 stage2HandshakeOutgoing =
							Objects.requireNonNull(CryptoCommunication.generateCryptoHandshakeStage2(externerPublicKey
									, clientKeyPair.getPublic(), clientSecretKey, clientIvParameterSpec)).put("name",
									user).put("sender", "server").put("receiver", user);
					assert stage2HandshakeOutgoing != null;
					send(pClientIP, pClientPort, stage2HandshakeOutgoing.getFormattedFieldV2(0));
				}
				else if (stage.equals("3"))
				{
					// Prüft, ob alles funktioniert hat
					if (CryptoCommunication.extractHandshakeDataStage3(field,
							direktChatCryptoStorageHashMap.get(user)[1],
							direktChatCryptoStorageHashMap.get(user)[0].serverPrivateKey))
					{
						System.out.println("Stage 3 bestätigt");
					}
					else
					{
						// Schließt verbindung, falls nicht
						System.err.println("Stage 3 Handshake failed");
						send(pClientIP, pClientPort, "FALSCH");
						closeConnection(pClientIP, pClientPort);
					}

					return;
				}

				return;
			}

			// Da beides hier ankommen kann, muss beides berücksichtigt werden
			final ConfFileFieldV2 cryptoCommunication;
			if (inputReader.getField("CryptoCommunication") != null)
				cryptoCommunication = inputReader.getField("CryptoCommunication");
			else
				cryptoCommunication = inputReader.getField("CryptoHandshake");
			final String[] receiver = cryptoCommunication.getValues("receiver");

			// Ohne receiver kann nicht weitergeleitet werden
			if (receiver == null)
				// TODO: Fehlercodes
				System.err.println("Unknown CryptoCommunication! Add error code!");
			else if (receiver[0].equals("server"))
			{
				// TODO: Nachrichten handeln die an den Server gerichtet sind
				System.out.println("SIEHE TODO: SERVER NACHRICHTEN HANDELN");
			}
			else
			{
				// Leitet die Nachricht weiter, falls möglich
				send(receiver[0], Integer.parseInt(receiver[1]), pMessage);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort)
	{
		System.out.println("Disconnect: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort);
	}
}
