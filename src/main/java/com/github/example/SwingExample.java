package com.github.example;

import com.github.ConnectionType;
import com.github.signal.Signal1;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.HeadlessException;

public class SwingExample extends JFrame implements DocumentListener {

	private final Signal1<String> textChanged = new Signal1<>();

	private SwingExample() throws HeadlessException {
		setTitle("Swing Example");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final JTextField textField = new JTextField();
		textField.getDocument().addDocumentListener(this);

		final JLabel label = new JLabel();
		textChanged.connect(label::setText, ConnectionType.SWING);

		add(textField);
		add(label);

		setSize(500, 800);
		textField.setSize(getWidth(), 50);
		label.setSize(getWidth(), 50);

		setVisible(true);
	}

	@Override
	public void insertUpdate(final DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void removeUpdate(final DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void changedUpdate(final DocumentEvent e) {
		try {
			final int length = e.getDocument().getLength();
			final String text = e.getDocument().getText(0, length);
			// Call `emit` in non-Swing thread
			new Thread(() -> textChanged.emit(text)).start();
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(SwingExample::new);
	}
}
