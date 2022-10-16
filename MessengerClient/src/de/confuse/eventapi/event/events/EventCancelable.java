package de.confuse.eventapi.event.events;

import de.confuse.eventapi.event.ICancelable;
import de.confuse.eventapi.event.IEvent;

/**
 * Extends this abstract Event for a more complex implementation.<br>
 * This Class provides a boolean value to determine if the value has been
 * cancelled or not, which can be checked while computing a given event.
 * 
 * @author Confuse / Confuse#5117
 * @see IEvent
 * @see ICancelable
 */
public abstract class EventCancelable implements IEvent, ICancelable
{
	private boolean cancelled = false;
	private final String tag;

	public EventCancelable()
	{
		this(null);
	}

	public EventCancelable(String tag)
	{
		this.tag = tag;
	}

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
