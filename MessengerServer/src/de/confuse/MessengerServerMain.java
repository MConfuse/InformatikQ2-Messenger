package de.confuse;

import de.confuse.util.CryptoCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MessengerServerMain
{
	public static MessengerServerMain instance;
	public final MessengerServer messengerServer;

	public static void main(String[] args)
	{
		new MessengerServerMain();
	}

	public MessengerServerMain()
	{
		System.out.println("Starte Server...");
		instance = this;
		this.messengerServer = new MessengerServer(1887);

		consoleThread();
		System.out.println("Server Online!");
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
						messengerServer.close();
						reader.close();
						break;
					}
					else
					{
						// TODO: Mach was mit dem console thread?
					}

				}

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}, "Console-Thread").start();
	}

}
