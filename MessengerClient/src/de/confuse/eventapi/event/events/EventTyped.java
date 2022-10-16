package de.confuse.eventapi.event.events;

import de.confuse.eventapi.event.IEvent;
import de.confuse.eventapi.event.ITyped;
import de.confuse.eventapi.types.EventType;

/**
 * Extends this abstract Event for a more complex implementation.<br>
 * This Class provides a byte value to determine the state of the event based on
 * the byte values found in the {@link EventType} class.<br>
 * <br>
 * If not set manually, the {@link EventType} of this event will be defaulted to
 * {@link EventType#PRE}!
 * 
 * @author Confuse / Confuse#5117
 * @see IEvent
 * @see ITyped
 */
public abstract class EventTyped implements IEvent, ITyped
{
	private final String tag;
	private byte type;

	/**
	 * If not set manually, the {@link EventType} of this event will be defaulted to
	 * {@link EventType#PRE}!
	 */
	public EventTyped()
	{
		this(null, EventType.PRE);
	}

	public EventTyped(byte type)
	{
		this(null, type);
	}

	/**
	 * If not set manually, the {@link EventType} of this event will be defaulted to
	 * {@link EventType#PRE}!
	 * 
	 * @param tag The tag of this event
	 */
	public EventTyped(String tag)
	{
		this(null, EventType.PRE);
	}

	public EventTyped(String tag, byte type)
	{
		this.tag = tag;
		this.type = type;
	}

	@Override
	public byte getType()
	{ return type; }

	@Override
	public void setType(byte type)
	{ this.type = type; }

	@Override
	public String getTag()
	{ return tag; }

}
