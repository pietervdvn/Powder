package gui;

import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;
import simulation.UsefullFullGrid;

public class TickThread implements Runnable, ValueListener<Boolean> {

	public BooleanValue control = new BooleanValue(true, "Render (play/pause)");
	public BooleanValue controlLocked = new BooleanValue(false, "Control is toggable");
	public IntegerValue timeout = new IntegerValue(15, "Time between ticks");

	private final UsefullFullGrid fg;

	private Boolean isUpdating = false;

	public TickThread(UsefullFullGrid fg) {
		this.fg = fg;
		control.addListener(this);
	}

	/**
	 * Stops updating the simulation, runs the runnable, and restarts the
	 * simulation when 'r' is performed. If the simulation wasn't running, only
	 * runs 'r'
	 * 
	 * Precontrol is run before the actual halting (e.g. to disable gui
	 * elements)
	 */
	public void pauseWhile(Runnable precontrol, Runnable r, Runnable postControl) {
		boolean running;
		running = control.get();
		if (precontrol != null) {
			precontrol.run();
		}
		controlLocked.set(true);
		control.set(false);

		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (isUpdating) {
					while (isUpdating) {
						try {
							isUpdating.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				r.run();

				control.set(running);
				controlLocked.set(false);
				if (postControl != null) {
					postControl.run();
				}
			}

		}).start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (control.get()) {
					synchronized (isUpdating) {
						isUpdating = true;
						fg.tick();
						isUpdating = false;
						isUpdating.notifyAll();
					}
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
