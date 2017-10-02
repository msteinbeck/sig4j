package com.github.dispatcher;

import com.github.SlotDispatcher;
import javax.swing.SwingUtilities;

/**
 * A {@link SlotDispatcher} for Swing events.
 */
public class SwingDispatcher extends SlotDispatcher {

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
	 * created and started using {@link SlotDispatcher#start()}.
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
