package com.github.msteinbeck.sig4j.dispatcher;

import com.github.msteinbeck.sig4j.Dispatcher;
import javax.swing.SwingUtilities;

/**
 * A {@link Dispatcher} for Swing events.
 */
public class SwingDispatcher extends Dispatcher {

	/**
	 * The singleton instance.
	 */
	private static SwingDispatcher instance;

	/**
	 * Default constructor.
	 */
	private SwingDispatcher() {}

	/**
	 * Returns the singleton instance. If no instance exists yet, a new one is
	 * created and started using {@link Dispatcher#start()}.
	 *
	 * @return The singleton instance.
	 */
	public static synchronized SwingDispatcher getInstance() {
		if (instance == null) {
			instance = new SwingDispatcher();
			instance.start();
		}
		return instance;
	}

	@Override
	protected void switchContext() {
		// do not call `super.switchContext();`!
		SwingUtilities.invokeLater(this::dispatch);
	}
}
