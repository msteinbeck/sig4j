package com.github.example;

import com.github.ConnectionType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.github.signal.Signal1;

import java.util.Arrays;

public class JavaFXExample extends Application {

	private final Signal1<String> textChanged = new Signal1<>();

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("JavaFX Example");

		final TextField textField = new TextField();
		textField.textProperty().addListener(l -> {
			// Call `emit` in non-fx thread
			new Thread(() -> textChanged.emit(textField.getText())).start();
		});

		final Label label = new Label();
		// Remove `JAVAFX` to get FX exceptions
		textChanged.connect(label::setText, ConnectionType.JAVAFX);

		final Pane root = new VBox();
		root.getChildren().addAll(Arrays.asList(textField, label));
		stage.setScene(new Scene(root, 500, 800));
		stage.show();
	}
}
