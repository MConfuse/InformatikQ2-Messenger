package de.confuse.eventapi.event;

public interface ICancelable
{
	/**
	 * Retrieves the current state of the event.
	 * 
	 * @return True if the event is cancelled.
	 */
	boolean isCancelled();

	/**
	 * Changes the current state of the event.
	 * 
	 * @param state Whether the event should be cancelled or not.
	 */
	void setCancelled(boolean state);
}
