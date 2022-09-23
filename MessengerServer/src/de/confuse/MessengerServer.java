package de.confuse;

import de.confuse.abiKlassen.Server;

public class MessengerServer extends Server
{
	public MessengerServer(int pPort)
	{
		super(pPort);
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort)
	{
		System.out.println("Connection: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort);
	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage)
	{

	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort)
	{
		System.out.println("Disconnect: pClientIP = " + pClientIP + ", pClientPort = " + pClientPort);
	}
}
