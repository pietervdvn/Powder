package reactivity.resources;

public interface ResourceTrackerListener {
	
	/**
	 * Called when the locale is changed
	 */
	public void onLocaleChanged(ResourceTracker source);
	
}
