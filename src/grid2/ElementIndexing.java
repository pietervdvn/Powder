package grid2;

import grid2.Element;

public class ElementIndexing {
	
	private final int totalNumberOfElements;
	private final Element[] elementList;
	
	public ElementIndexing(Element[] baseList) {
		this.elementList = baseList;
		this.totalNumberOfElements = baseList.length;
	}
	
	public int getCompositeNumber(Element[] baseElements){
		int index = 0;
		for (int i = baseElements.length; i >= 0; i--) {
			index = index * totalNumberOfElements + baseElements[i].id;
		}
		return index;
	}
	
	public Element getBaseElement(int multiRepr){
		return elementList[multiRepr % totalNumberOfElements];
	}
	
	public int dissolve(int multiRep){
		return multiRep/ totalNumberOfElements;
	}
	
	

}
