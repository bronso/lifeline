package timeline.frame;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import timeline.Timeline;

class TimelineFrameLine extends Pane{
	
	private void constructorBase(){
		this.setMaxWidth(Double.MAX_VALUE);
		this.getStyleClass().add("line");
	}
		
	public TimelineFrameLine(Region r){	//binder høyde til parent
		this.prefHeightProperty().bind(r.heightProperty()); 
		this.constructorBase();
	}
	
	public TimelineFrameLine(double h){ //setter spesifikk høyde 
		this.setPrefHeight(h);
		this.constructorBase();
	}
	
	public void addTimeline(Timeline t){
		//this.getChildren().add(t);
		//t.prefHeightProperty().bind(this.heightProperty()); 
	}
	
}
