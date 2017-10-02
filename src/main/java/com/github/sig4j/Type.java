package com.github.sig4j;

/**
 * The enum of supported connection types.
 *
 * To actuate a slot by a {@link Dispatcher} use
 * {@link Signal#connect(Slot, Dispatcher)}.
 *
 * @see Dispatcher
 */
public enum Type {
	/**
	 * The slot is actuated within the thread context of the emitter.
	 */
	DIRECT,

	/**
	 * Similar to {@link #QUEUED}, but actuates the slot within the thread
	 * context of the JavaFX thread.
	 */
	JAVAFX,

	/**
	 * The slot is queued until the worker thread used in {@link Signal} is
	 * able to actuate the slot. Use this connection type if you want the
	 * emitter to return immediately without any blocking.
	 */
	QUEUED,

	/**
	 * Similar to {@link #QUEUED}, but actuates the slot within the thread
	 * context of the Swing thread.
	 */
	SWING,
}
