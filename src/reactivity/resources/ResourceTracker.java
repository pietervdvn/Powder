package reactivity.resources;

import reactivity.resources.guiColorModel.GUIColorModel;
import reactivity.valueWrappers.Value;

public class ResourceTracker {

	public final Value<GUIColorModel> guiColorModel = new Value<GUIColorModel>(
			new GUIColorModel(), "Gui Color Model");

}
