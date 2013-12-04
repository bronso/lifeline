package timeline;

public class TimelineDirectives { //Holder ordre som gis til objekt vedrørende eventuelle operasjoner som må gjøres. 
	private boolean reScope,addStart,addEnd,fill,update=false;
	
	public void reScope(){
		reScope=true;
		update=true;
	}
	
	public void addStart(){
		addStart=true;
		update=true;
	}
	
	public void addEnd(){
		addEnd=true;
		update=true;
	}
	
	public void fill(){
		fill=true;
		update=true;
	}
	
	public boolean getReScope(){
		return reScope;
	}
	
	public boolean getAddStart(){
		return addStart;
	}
	
	public boolean getAddEnd(){
		return addEnd;
	}
	
	public boolean getFill(){
		return fill;
	}
	
	public boolean getUpdate(){
		return update;
	}
}
