package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import grid.FullGrid;
import levels.Level;
import reactivity.gui.Button;
import reactivity.valueWrappers.BooleanValue.Not;

public class PresetLevelLoaderControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public PresetLevelLoaderControlPanel(FullGrid fg, TickThread t, List<Level> levels) {
		setBorder(new TitledBorder("Preset levels"));
		if (levels == null || levels.size() == 0) {
			add(new JLabel("No preset levels found!"));
			return;
		}

		setLayout(new GridLayout(3, 2));

		List<Button> allButtons = new ArrayList<>();
		for (Level l : levels) {
			allButtons.add(createButton(t, l, allButtons, fg));
		}
		allButtons.forEach(buttons -> add(buttons));
		allButtons.forEach(button -> button.setEnabled(new Not(t.controlLocked)));

	}

	private Button createButton(TickThread t, final Level l, final List<Button> allButtons, FullGrid fg) {
		Button b = new Button(l.name());
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Runnable precontrol = new Runnable() {
					
					@Override
					public void run() {
						b.setText(l.name() + " (loading...)");
						allButtons.forEach(button -> button.setEnabled(false));
					}
				};

				Runnable postControl = new Runnable() {

					@Override
					public void run() {
						b.setText(l.name());
						allButtons.forEach(button -> button.setEnabled(true));
					}
				};

				Runnable control = new Runnable() {

					@Override
					public void run() {
						fg.init(l);
					}
				};

				t.pauseWhile(precontrol, control, postControl);

			}
		});
		return b;
	}

}
