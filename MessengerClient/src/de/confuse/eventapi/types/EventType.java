package de.confuse.eventapi.types;

import de.confuse.eventapi.event.events.EventTyped;

/**
 * Defines byte values for the event types found in the {@link EventTyped}
 * class.
 * 
 * @author Confuse / Confuse#5117
 *
 */
public class EventType
{
	/**
	 * Used to define the type/stage of typed event.
	 */
	public static final byte PRE = 0, MID = 1, POST = 2, SEND = 3, RECEIVE = 4;

}
