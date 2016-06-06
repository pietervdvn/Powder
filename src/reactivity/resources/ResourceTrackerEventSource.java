package reactivity.resources;

import reactivity.EventSource;

/**
  * Bozels
  * 
  * Door:
  * Pieter Vander Vennet
  * 1ste Bachelor Informatica
  * Universiteit Gent
  * 
  */
class ResourceTrackerEventSource extends EventSource<ResourceTrackerListener>{
	
	private ResourceTracker source;
	
	protected final void setSource(ResourceTracker src){
		this.source = src;
	}

	public void throwLocaleChanged(){
		synchronized(getListeners()){
			getListeners().resetCounter();
			while(getListeners().hasNext()){
				getListeners().next().onLocaleChanged(source);
			}
		}
	}
}
