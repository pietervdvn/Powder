package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import elements.Element;
import elements.Elements;
import reactivity.valueWrappers.Value;

public class ElementSelector extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1935925059529481503L;

	public Value<Element> selection = new Value<Element>(elements.Elements.AIR.behaviour, "Element selection");

		public ElementSelector() {
			add(new JLabel("Element to draw: "));
			JComboBox<Elements> combo = new JComboBox<>(elements.Elements.values());
			this.add(combo);
			combo.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(arg0);
		}
	
	
}
