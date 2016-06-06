package reactivity.resources.guiColorModel;


import static java.awt.Color.black;
import static java.awt.Color.gray;
import static java.awt.Color.red;
import static java.awt.Color.white;
import static java.awt.Color.yellow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import reactivity.EventSource;
import reactivity.ValueListener;
import reactivity.valueWrappers.ColorValue;
import reactivity.valueWrappers.Value;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class GUIColorModel extends EventSource<GUIColorModelListener> implements ValueListener<Color> {
	
	public static final int DEFAULT = 0;
	public static final int UNSAVED = 1;
	public static final int ERROR = 2;
	public static final int NOT_EDITABLE = 3;
	
	
	private final static Color[] DEFAULT_FOREGROUND = {
		black,
		black,
		black,
		black
	};
	private final static Color[] DEFAULT_BACKGROUND = {
		white,
		yellow,
		red,
		gray,
	};
	
	private final List<Value<Color>> foreground = new ArrayList<Value<Color>>();
	private final List<Value<Color>> background = new ArrayList<Value<Color>>();
	
	private final List<Value<Color>> xmlables = new ArrayList<Value<Color>>();
	
	public GUIColorModel() {
		
		JTextField f = new JTextField();
		f.setEditable(false);
		DEFAULT_BACKGROUND[NOT_EDITABLE] = f.getBackground();
		
		for (int i = 0; i < DEFAULT_FOREGROUND.length; i++) {
			Value<Color> val = new ColorValue(DEFAULT_FOREGROUND[i], "foreground"+i);
			foreground.add(val);
			xmlables.add(val);
			val.addListener(this);
			
		}
		for (int i = 0; i < DEFAULT_BACKGROUND.length; i++) {
			Value<Color> val = new ColorValue(DEFAULT_BACKGROUND[i], "background"+i);
			background.add(val);
			xmlables.add(val);
			val.addListener(this);
		}
	}
	
	public Color getForeground(int index){
		return foreground.get(index).get();
	}
	
	public Value<Color> getForegroundValue(int index){
		return foreground.get(index);
	}
	
	public void setForeground(int index, Color newColor){
		foreground.get(index).set(newColor);
	}

	public Color getBackground(int index){
		return background.get(index).get();
	}
	
	public Value<Color> getBackgroundValue(int index){
		return background.get(index);
	}
	
	public void setBackground(int index, Color newColor){
		background.get(index).set(newColor);
	}
	
	
	public void apply(JComponent component){
		apply(component, DEFAULT);
	}
	
	public void apply(JComponent component, int key){
		component.setForeground(getForeground(key));
		component.setBackground(getBackground(key));
	}
	
	private void throwColorsChanged() {
		synchronized (getListeners()) {
			getListeners().resetCounter();
			while(getListeners().hasNext()){
				getListeners().next().onColorsChanged(this);
			}
		}
	}

	@Override
	public void onValueChanged(Value<Color> source, Color newValue) {
		throwColorsChanged();
	}

}
