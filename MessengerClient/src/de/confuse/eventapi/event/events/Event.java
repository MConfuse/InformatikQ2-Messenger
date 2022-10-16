package de.confuse.eventapi.event.events;

import de.confuse.eventapi.event.ICancelable;
import de.confuse.eventapi.event.IEvent;
import de.confuse.eventapi.event.ITyped;
import de.confuse.eventapi.types.EventType;

/**
 * This abstract class Event combines the functionalities of the
 * {@link EventCancelable} and {@link EventTyped} into one class while not
 * inheriting from them.<br>
 * If you do not need any special functionality it is probably always advisable
 * to inherit from this class, so you always have the full feature set at your
 * disposal.
 * 
 * @author Confuse
 *
 */
public abstract class Event implements IEvent, ITyped, ICancelable
{
	private boolean cancelled = false;
	private final String tag;
	private byte type;

	/**
	 * If not set manually, the {@link EventType} of this event will be defaulted to
	 * {@link EventType#PRE}!
	 */
	public Event()
	{
		this(null, EventType.PRE);
	}

	public Event(byte type)
	{
		this(null, type);
	}

	/**
	 * If not set manually, the {@link EventType} of this event will be defaulted to
	 * {@link EventType#PRE}!
	 * 
	 * @param tag The tag of this event
	 */
	public Event(String tag)
	{
		this(null, EventType.PRE);
	}

	public Event(String tag, byte type)
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
	public boolean isCancelled()
	{ return cancelled; }

	@Override
	public void setCancelled(boolean state)
	{ this.cancelled = state; }

	@Override
	public String getTag()
	{ return tag; }

}
