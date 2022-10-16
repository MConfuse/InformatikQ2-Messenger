package de.confuse;

import de.confuse.eventapi.event.IEvent;

/**
 * Erstellt ein neues Event, welches im {@link de.confuse.eventapi.EventManager} an alle empfänger zugestellt
 * werden kann.
 *
 * @param message Die Nachricht, welche Empfangen wurde. Kein Format garantiert!
 */
public record EventMessageReceived(String message) implements IEvent
{
	@Override
	public String getTag()
	{
		return "EventMessageReceived";
	}

	@Override
	public String toString()
	{
		return "EventMessageReceived{" +
				"message='" + message + '\'' +
				'}';
	}
}
