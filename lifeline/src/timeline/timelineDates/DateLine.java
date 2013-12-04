package timeline.timelineDates;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import timeline.TimelineContainer;
import timeline.TimelineDirectives;
import timeline.TimelineParameters;

public class DateLine extends DateLineBase{
	
	@Override protected void createTimelineContainer(long st,long et){ //timeline er fylt av timelineContainere 				
		
		//System.out.println("startTime:"+st+" endTime:"+et+" Width:"+width+" Scope:"+demands.getScope()+" TimeUnit:"+getTimeUnit());
		
		final GetTimelineContainer addContainer = new GetTimelineContainer(new DateTimelineParameters(st,et,demands.getScope(),getTimeUnit())); //service som starter ny tr�d		
		
		addContainer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		   @Override public void handle(WorkerStateEvent t) {
		     TimelineContainer c = addContainer.getValue();     
		     DateLine.this.addTimelineContainer(c);
		   }
		 });
		
		addContainer.setOnFailed(new EventHandler<WorkerStateEvent>() {
		    @Override
		    public void handle(WorkerStateEvent t) {
		        System.out.println("error");
		    }
		});
		
		new Thread(addContainer).start();
	}
	
	private class GetTimelineContainer extends Task<TimelineContainerDates> {
		
		private DateTimelineParameters t;
		
		public GetTimelineContainer(DateTimelineParameters tp) { 
			this.t = tp; 
		}
		
		@Override protected  TimelineContainerDates call() throws Exception {		
			return new TimelineContainerDates(t.getStartTime(),t.getEndTime(),t.getScope(),t.getTimeUnit());		
		}
	}
	
	public void setLineStyle(String s){
		node.getStyleClass().add(s);
	}
	
	public void refreshTimeline(){				
		
		TimelineDirectives todo=checkStatus(demands); //analyserer nye parametre og ser om det m� gj�res oppdatering i timeline
		
		if(todo.getUpdate()){ //hvis det m� gj�res oppdateringer utf�res executeUpdate
			executeUpdate(todo);
		}
		
		par.setScope(demands.getScope());
	}
	
	public DateLine(TimelineParameters tp,double w,int tu,long at){
		super(tp,w,at);
		setTimeUnit(tu);
		refreshTimeline();	
	}
}
