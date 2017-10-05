package com.github.msteinbeck.sig4j.dispatcher;

import com.github.msteinbeck.sig4j.Dispatcher;
import javafx.application.Platform;

/**
 * A {@link Dispatcher} for JavaFX events.
 */
public class JavaFXDispatcher extends Dispatcher {

	/**
	 * The singleton instance.
	 */
	private static JavaFXDispatcher instance;

	/**
	 * Default constructor.
	 */
	private JavaFXDispatcher() {}

	/**
	 * Returns the singleton instance. If no instance exists yet, a new one is
	 * created and started using {@link Dispatcher#start()}.
	 *
	 * @return The singleton instance.
	 */
	public static synchronized JavaFXDispatcher getInstance() {
		if (instance == null) {
			instance = new JavaFXDispatcher();
			instance.start();
		}
		return instance;
	}

	@Override
	protected void switchContext() {
		// do not call `super.switchContext();`!
		Platform.runLater(this::dispatch);
	}
}
