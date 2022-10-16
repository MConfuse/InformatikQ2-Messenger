package de.confuse;

import de.confuse.eventapi.EventManager;
import de.confuse.eventapi.EventTarget;

public class MessageHandler
{
	MessageHandler()
	{
		EventManager.register(this);
	}

	/*
	 * Hier z.B. noch weitere Events für spezifische Nachrichten etc hinzufügen, bzw die eventMessageReceived Methode
	 * dazu nutzen, die Nachricht in einem standardisiertem Format zu Lesen und dann entsprechend weitere Events zum
	 * Verteilen zu nutzen.
	 */
	@EventTarget
	public void eventMessageReceived(EventMessageReceived event)
	{
		System.out.println("MessageHandler.eventMessageReceived");
		System.out.println("event = " + event);
	}

}
