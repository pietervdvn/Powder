package reactivity.gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.StringValue;
import reactivity.valueWrappers.Value;

public class Toggle extends JPanel implements ValueListener<Boolean>{
	private static final long serialVersionUID = -3883165167106884378L;
	
	private BooleanValue enabled;
	
	public Toggle(BooleanValue bv) {
		this(bv, new StringValue(bv.getName(), ""));
	}
	public Toggle(BooleanValue bv, StringValue text) {
		
		this.setLayout(new FlowLayout());
		
		AutoJLabel txt = new AutoJLabel(text,"");
		JCheckBox box = new JCheckBox();
		box.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				bv.set(box.isSelected());
			}
		});
		
		txt.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				bv.set(!bv.get());
			}
		});
		
		bv.addHardListener(new ValueListener<Boolean>() {
			@Override
			public void onValueChanged(Value<Boolean> source, Boolean newValue) {
				box.setSelected(newValue);
			}
		});
		box.setSelected(bv.get());
		
		this.add(box);
		this.add(txt);
	}
	
	public void setEnabled(BooleanValue enabled) {
		if(this.enabled != null){
			this.enabled.removeListener(this);
		}
		enabled.addListener(this);
		onValueChanged(enabled, enabled.get());
		this.enabled = enabled;
	}
	@Override
	public void onValueChanged(Value<Boolean> source, Boolean newValue) {
		setEnabled(newValue);
	}

}
