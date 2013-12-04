package timeline.frame;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LineContainer extends VBox{

	private ArrayList<TimelineFrameLine> lines;
	
	public ArrayList<TimelineFrameLine> getLines(){
		return lines;
	}
	
	public LineContainer(){
		lines=new ArrayList<TimelineFrameLine>();
		this.getStyleClass().add("line-container");
	}
	
	public TimelineFrameLine addLine(double h){
		TimelineFrameLine l=new TimelineFrameLine(h);
		lines.add(l);
		
		this.getChildren().add(l);
		
		return l; 
	}
}
