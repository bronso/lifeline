package timeline;

import javafx.concurrent.Service;

public abstract class TimelineContainerCreator extends Service<TimelineContainer> {

	protected TimelineParameters t;
	
	public void setTimelineParameters(TimelineParameters tp) { 
		this.t = tp; 
	}
	
	public void setTime(long st, long et){
		t.setStartTime(st);
		t.setEndTime(et);
	}
	
}
