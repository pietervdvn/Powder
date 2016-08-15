package gui;

import grid2.UsefullFullGrid;
import reactivity.ValueListener;
import reactivity.valueWrappers.BooleanValue;
import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;

public class TickThread implements Runnable, ValueListener<Boolean> {

	public BooleanValue control = new BooleanValue(true, "Render (play/pause)");
	public BooleanValue controlLocked = new BooleanValue(false, "Control is toggable");
	public IntegerValue timeout = new IntegerValue(0, "Time between ticks");

	private final UsefullFullGrid fg;

	public TickThread(UsefullFullGrid fg) {
		this.fg = fg;
		control.addListener(this);
	}

	/**
	 * Stops updating the simulation, runs the runnable, and restarts the
	 * simulation when 'r' is performed. If the simulation wasn't running, only
	 * runs 'r'
	 * 
	 * Precontrol is run befor the actual halting (e.g. to disable gui elements)
	 */
	public void pauseWhile(Runnable precontrol, Runnable r, Runnable postControl) {
		boolean running;
		synchronized (control) {
			running = control.get();
			if (precontrol != null) {
				precontrol.run();
			}
			controlLocked.set(true);
			control.set(false);
			try {
				if (running) {
					control.wait();
				}

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
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
					fg.tick();
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
