package gui;

import grid.FullGrid;
import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;

public class TickThread implements Runnable, ValueListener<Boolean> {

	public BooleanValue control = new BooleanValue(true, "Render (play/pause)");
	public IntegerValue timeout = new IntegerValue(0, "Time between ticks");

	private final FullGrid fg;

	public TickThread(FullGrid fg) {
		this.fg = fg;
		control.addListener(this);
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (control.get()) {
					fg.evolve();
					Thread.sleep(timeout.get());
				} else {
					synchronized (control) {
						control.notifyAll();
					}
					synchronized (this) {
						this.wait();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onValueChanged(Value<Boolean> source, Boolean newValue) {
		synchronized (this) {
			this.notifyAll();
		}
	}

}
