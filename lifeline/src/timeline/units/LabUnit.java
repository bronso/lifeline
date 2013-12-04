package timeline.units;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import timeline.TimelineParameters;
import timeline.TimelinesView;
import timeline.frame.LifelineFrame;

//LabUnit samler sammen alle komponenter som brukes for å vise labresultater. 

public class LabUnit extends Region{
	
	private TimelineParameters par;
	private double unitHeight=500d;
	private TimelinesView vPort;
	private LifelineFrame frame;
	private double s1Width=50;
	private double s2Width=200;
	private double vPortWidth=700;
	
	public LabUnit(){
		ObservableList<Node> children=this.getChildren();
		
		par=new TimelineParameters();
		
		long currentTime=System.currentTimeMillis();
		par.setScope(1500000000L);
		par.setStartTime(currentTime-(par.getScope()/2)); //tiende mai
		par.setEndTime(currentTime+(par.getScope()/2)); //tiende november	
		
		frame=new LifelineFrame();
		vPort=new TimelinesView(vPortWidth,par);
		
		frame.addToGrid(vPort.getNode(),0,vPortWidth);
		frame.setPrefHeight(unitHeight);
		
		children.add(frame);	
	}
}
