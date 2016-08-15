package grid2;

import java.util.HashMap;
import java.util.Map;

public class ElementIndexing {
	
	private final int totalNumberOfElements;
	private final Element[] elementList;
	private final Map<String, Element> fromName = new HashMap<>();
	
	public ElementIndexing(Element[] baseList) {
		this.elementList = baseList;
		this.totalNumberOfElements = baseList.length;
		for (Element element : baseList) {
			fromName.put(element.name.toUpperCase(), element);
		}
	}
	
	
	public Element getBaseElement(int multiRepr){
		return elementList[(multiRepr-1) % totalNumberOfElements];
	}
	
	public String nameOf(int id){
		return getBaseElement(id).name;
	}
	
	public Element fromName(String name){
		Element element=  fromName.get(name.toUpperCase());
		if(element == null){
			throw new IllegalStateException("The element "+name+" is not known");
		}
		return element;
	}
	
	public int idFromName(String name){
		return fromName(name.toUpperCase()).id;
	}
	
	public int numberOfElements(){
		return elementList.length;
	}
	
}
