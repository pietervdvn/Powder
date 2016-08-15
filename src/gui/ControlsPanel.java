package gui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import grid2.Element;
import grid2.UsefullFullGrid;
import reactivity.ValueListener;
import reactivity.gui.ProgressMeter;
import reactivity.gui.Toggle;
import reactivity.gui.coloringTextField.IntegerValueTextField;
import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.BooleanValue.Not;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;

public class ControlsPanel extends JPanel {
	private static final long serialVersionUID = 5508832599308671632L;

	public final Value<Element> selection;
	public final Value<Integer> pencilSize = new IntegerValue(3, "Pencil size");
	
	public ControlsPanel(GUIColorModel cm, UsefullFullGrid elements, TickThread ticker, List<BooleanValue> toggles, Element[] knownElements) {

		setBorder(new TitledBorder("Controls"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Toggle playPause = new Toggle(ticker.control);
		playPause.setEnabled(new Not(ticker.controlLocked));

		add(playPause);
		add(new ToggleOptions(toggles, true));

		JButton tickOnce = new JButton(new AbstractAction("Tick Once") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				elements.tick();
			}
		});
		add(tickOnce);

		add(new ThousandTickerPanel(elements, ticker));

		ticker.control.addHardListener(new ValueListener<Boolean>() {
			@Override
			public void onValueChanged(Value<Boolean> source, Boolean newValue) {
				tickOnce.setEnabled(!newValue);
			}
		});
		ticker.control.throwEvent();

		ElementSelector es = new ElementSelector(knownElements);
		selection = es.selection;
		add(es);

		add(new IntegerValueTextField(cm, pencilSize, 1, 50));
		
	}

	public static class ThousandTickerPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public ThousandTickerPanel(UsefullFullGrid elements, TickThread ticker) {
			IntegerValue progresMeas = new IntegerValue(0, "Progress");
			final JButton tickThousand = new JButton();
			AbstractAction a = new AbstractAction("Tick 1000") {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					progresMeas.set(1000);
					tickThousand.setEnabled(false);
					Runnable r = new Runnable() {
						@Override
						public void run() {
							elements.tick(progresMeas);
							progresMeas.set(0);
							tickThousand.setEnabled(true);
						}
					};
					ticker.pauseWhile(null, r, null);
				}
			};
			tickThousand.setAction(a);

			add(tickThousand);
			add(new ProgressMeter(progresMeas, 0, 1000));

		}

	}

}
