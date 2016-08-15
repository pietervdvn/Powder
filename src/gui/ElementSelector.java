package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import grid2.Element;
import reactivity.valueWrappers.Value;

public class ElementSelector extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1935925059529481503L;

	public Value<Element> selection;
	
	private final JComboBox<Element> combo;

	public ElementSelector(Element[] knownElements) {
	selection	= new Value<Element>(knownElements[0], "Element selection");
		add(new JLabel("Element to draw: "));
		combo = new JComboBox<>(knownElements);
		this.add(combo);
		combo.addActionListener(this);
		actionPerformed(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		selection.set((Element) combo.getSelectedItem());
	}

}
