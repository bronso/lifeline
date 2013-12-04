package timeline.timelineDates;

import timeline.TimelineContainer;

public class TimelineContainerDates extends TimelineContainer {

	public TimelineContainerDates(long st,long et,long s,int tu){//starttime, endtime, width, scope 
		
		TimelineDates timelineDates = new TimelineDates(st,et,s,tu); //grid er en gridpane med datoer
 
    	double containerWidth=timelineDates.getNodeWidth();
    	
    	par=new DateTimelineParameters();
	    par.setStartTime(timelineDates.getStartTime());
	    par.setEndTime(timelineDates.getEndTime());
	    
    	this.getNode().getChildren().add(timelineDates);
    	this.getNode().setPrefWidth(containerWidth);
    	
    	timelineDates.prefWidthProperty().bind(this.getNode().widthProperty());	
    	timelineDates.prefHeightProperty().bind(this.getNode().heightProperty());	 
    	
    	this.getNode().getStyleClass().add("container-date");
	}
}


