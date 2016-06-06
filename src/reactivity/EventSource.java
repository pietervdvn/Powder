package reactivity;

import reactivity.valueWrappers.IntegerValue;
import reactivity.valueWrappers.Value;


/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
public class EventSource<T> {
	
	private final static Value<Integer> NUMBER_OF_EVENT_SOURCES = new IntegerValue(1, "numberOfEventSources");
	public static Value<Integer> getNumberofeventsources() {
		return NUMBER_OF_EVENT_SOURCES;
	}
	private final SafeList<T> listeners = new SafeList<T>();
	
	public EventSource() {
		if(NUMBER_OF_EVENT_SOURCES != null){
			NUMBER_OF_EVENT_SOURCES.set(NUMBER_OF_EVENT_SOURCES.get()+1);
		}
	}
		
	
	public void addListener(T listener){
		listeners.add(listener);
	}
	
	public void addHardListener(T listener){
		listeners.addHard(listener);
	}
	
	
	public void removeListener(T listener){
		listeners.remove(listener);
	}
	
	public SafeList<T> getListeners(){
		return listeners;
	}
}
