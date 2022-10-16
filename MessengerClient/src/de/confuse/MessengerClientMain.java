package de.confuse;

import de.confuse.confFileV2.ConfFileFieldV2;
import de.confuse.security.AesUtilities;
import de.confuse.security.RsaUtilities;
import de.confuse.util.CryptoCommunication;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MessengerClientMain
{
	public static MessengerClientMain instance;

	/**
	 * {@link MessengerClient}-Instanz.
	 */
	public final MessengerClient messengerClient;

	public MessengerClientMain()
	{
		System.out.println("Starte Client...");
		instance = this;
		new MessageHandler();
		this.messengerClient = new MessengerClient("localhost", 1887);

		consoleThread();
		messengerClient.anmelden();
		System.out.println("Client Online!");
	}

	public static void main(String[] args)
	{
		new MessengerClientMain();
	}

	private void consoleThread()
	{
		new Thread(() ->
		{
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try
			{
				while ((line = reader.readLine()) != null)
				{
					if (line.equalsIgnoreCase("end"))
					{
						messengerClient.close();
						reader.close();
						break;
					}
					else
						handleConsoleInput(line, reader);

				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}, "Console-Thread").start();
	}

	/**
	 * Bearbeitet die Konsolen eingabe
	 */
	private void handleConsoleInput(String line, BufferedReader reader) throws NoSuchAlgorithmException, IOException
	{
		if (messengerClient.isConnected())
		{
			if (line.equalsIgnoreCase("anmelden"))
			{
				messengerClient.anmelden();
			}
			else if (line.startsWith("verbinde="))
			{
				messengerClient.verbinde(line.substring(line.indexOf('=') + 1).trim());
			}
			else if (line.startsWith("nachricht="))
			{
				System.out.println("The IP:Port of your receiver:");
				String receiver = reader.readLine();

				messengerClient.nachricht(receiver, line.substring(line.indexOf('=') + 1));
			}
			else
			{
				messengerClient.send(line);
			}

		}
		else
			System.out.println("Kann nicht senden; Verbindung getrennt!");
	}

}
