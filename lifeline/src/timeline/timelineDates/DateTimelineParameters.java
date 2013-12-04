package timeline.timelineDates;

import timeline.TimelineParameters;

public class DateTimelineParameters extends TimelineParameters {
		
	private int timeUnit;
	
	public void setTimeUnit(int tu){
		timeUnit=tu;
	}
	
	public int getTimeUnit(){
		return timeUnit;
	}
	
	public DateTimelineParameters(){
		
	}
	
	public DateTimelineParameters(TimelineParameters tp,int tu){
		super();
		importTimelineParameters(tp);
		setTimeUnit(tu);
	}
	
	public DateTimelineParameters(DateTimelineParameters tp){
		super();
		importTimelineParameters(tp);
		setTimeUnit(tp.getTimeUnit());
	}
	
	public DateTimelineParameters(long st,long et,long s,int tu){
		super(st,et,s);
		
		setTimeUnit(tu);
		
	}
}
