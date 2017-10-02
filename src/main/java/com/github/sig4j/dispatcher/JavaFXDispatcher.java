package com.github.sig4j.dispatcher;

import javafx.application.Platform;
import com.github.sig4j.SlotDispatcher;

/**
 * A {@link SlotDispatcher} for JavaFX events.
 */
public class JavaFXDispatcher extends SlotDispatcher {

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
	 * created and started using {@link SlotDispatcher#start()}.
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
