package reactivity;

import reactivity.valueWrappers.Value;

public interface ValueListener<T> {

	void onValueChanged(Value<T> source, T newValue);
	
}
