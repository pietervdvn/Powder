package reactivity.gui.coloringTextField;


import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import reactivity.gui.coloringTextField.parser.Parser;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.resources.guiColorModel.GUIColorModelListener;


/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class ColoringTextField<T> extends JTextField implements
		GUIColorModelListener, FocusListener{

	private static final long serialVersionUID = 1L;

	private final Parser<T> parser;
	private final GUIColorModel colorMod;

	private boolean isSaved = true;
	private boolean seemsLegit = true;

	private T value = null;

	public ColoringTextField(GUIColorModel colorSettings, Parser<T> parser) {
		this.addFocusListener(this);
		this.colorMod = colorSettings;
		colorSettings.addListener(this);
		this.parser = parser;
		this.getDocument().addDocumentListener(new DocList());
		this.setAction(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				apply();
			}
		});
	}

	private void apply() {
		try {
			value = parser.parse(this.getText());
			setSaved(true);
			parser.newValueAction(getValue());
		} catch (ParseException e) {
			seemsLegit = false;
		}
		applyColors();
	}

	private void checkIfCorrectEntry() {
		try {
			isSaved = parser.parse(getText()).equals(value);
			seemsLegit = true;
		} catch (ParseException e) {
			seemsLegit = false;
		}
			
		applyColors();
	}

	private void applyColors() {
		colorMod.apply(this);
		if (!isSaved) {
			colorMod.apply(this, GUIColorModel.UNSAVED);
		}
		if (!seemsLegit) {
			colorMod.apply(this, GUIColorModel.ERROR);
		}
		if(!this.isEditable()){
			colorMod.apply(this, GUIColorModel.NOT_EDITABLE);
		}
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
		applyColors();
	}

	public boolean isCorrectlyParsed() {
		return seemsLegit;
	}

	public T save() {
		apply();
		return value;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public T getValue() {
		return value;
	}

	@Override
	public void onColorsChanged(GUIColorModel source) {
		applyColors();
	}

	@Override
	public void focusGained(FocusEvent e) {
		setCaretPosition(0);
		moveCaretPosition(getText().length());
	}

	@Override
	public void focusLost(FocusEvent e) {
		apply();
		this.setText(parser.getShortestNotationFor(value));
	}
	
	public void setValue(T value) {
		this.value = value;
		this.setText(parser.getShortestNotationFor(value));
		setSaved(true);
		
	}
	
	private class DocList implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			checkIfCorrectEntry();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			checkIfCorrectEntry();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			checkIfCorrectEntry();
		}
	}
	
	@Override
	public void setEditable(boolean b) {
		super.setEditable(b);
		if(colorMod != null){
			applyColors();
		}
	}
}
