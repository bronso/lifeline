package timeline;

import javafx.scene.layout.Pane;

public class TimelineContainer{

	public TimelineParameters par;
	public double xPos;
	private Pane node;
	
	public TimelineParameters getPar(){
		return par;
	}
	
	public double getXPos(){
		return xPos;
	}
	
	public Pane getNode(){
		return node;
	}
	
	public Pane createNode(){		
		node=new Pane();
		return node;
	}
	
	public TimelineContainer(){
		createNode();
	}
}


