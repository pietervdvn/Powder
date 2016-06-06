package reactivity;

import java.util.ArrayList;

/**
 * Weak referenced kinda of arraylist, where things can be removed safely: the
 * current index will be changed accordingly, and will not skip anything
 * 
 * Should be synchronized extarnally
 * 
 * @author pieter
 * 
 */
/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class SafeList<T> {

	private ArrayList<Reference<T>> list = new ArrayList<Reference<T>>();
	private int currentIndex = 0;
	private int internalIndex = 0;

	// v1

	public void resetCounter() {
		currentIndex = 0;
	}

	public void remove() {
		synchronized (list) {

			currentIndex--;
			remove(currentIndex);
		}
	}

	public void remove(T object) {
		synchronized (list) {

			int ind = indexOf(object);
			remove(ind);
		}
	}

	public void remove(int ind) {
		synchronized (list) {
			if (ind < 0 || ind >= list.size()) {
				return;
			}
			list.remove(ind);

			if (ind < currentIndex) {
				currentIndex--;
			}

			if (ind < internalIndex) {
				internalIndex--;
			}
		}
	}

	public boolean contains(T object) {
		return indexOf(object) >= 0;
	}

	public int indexOf(T object) {
		synchronized (list) {

			internalIndex = 0;
			while (hasNext(internalIndex)) {
				if (list.get(internalIndex).get().equals(object)) {
					return internalIndex;
				}
				internalIndex ++;
			}
			return -1;
		}
	}

	public boolean hasNext(int startIndex) {

		if (startIndex >= list.size()) {
			return false;
		}

		T obj = list.get(startIndex).get();
		if (obj == null) {
			list.remove(startIndex);
			return hasNext();
		}
		return true;
	}

	public boolean hasNext() {
		return hasNext(currentIndex);
	}

	public T next() {

		T obj = list.get(currentIndex++).get();
		if (obj == null) {
			remove();
			if (hasNext()) {
				return next();
			} else {
				throw new IllegalStateException("No next object");
			}
		}
		return obj;
	}

	public void add(T object) {
		synchronized (list) {
			list.add(new WeakReference<T>(object));
		}
	}

	public void addHard(T object) {
		synchronized (list) {
			list.add(new HardReference<T>(object));
		}
	}

	private interface Reference<T> {
		public T get();
	}

	@SuppressWarnings("hiding")
/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
	private class HardReference<T> implements Reference<T> {

		private final T ref;

		public HardReference(T referent) {
			this.ref = referent;
		}

		@Override
		public T get() {
			return ref;
		}
	}

	@SuppressWarnings("hiding")
/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
	private class WeakReference<T> extends java.lang.ref.WeakReference<T>
			implements Reference<T> {

		public WeakReference(T referent) {
			super(referent);
		}

	}

}
