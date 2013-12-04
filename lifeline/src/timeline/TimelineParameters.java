package timeline;

public class TimelineParameters{
	
	public long startTime,endTime,startLimit,endLimit,containerStartTime,containerEndTime,scope=0;
	public double width=0;
	
	//setters
	public void setStartTime(long st){
		startTime=st;	
	}
	
	public void setEndTime(long et){
		endTime=et;	
	}
	
	public void setScope(long s){
		scope=s;
	}
	
	//getters
	public long getStartTime(){
		return startTime;	
	}
	
	public long getEndTime(){
		return endTime;	
	}
	
	public long getScope(){
		return scope;
	}
	
	public void importTimelineParameters(TimelineParameters tp){
		setStartTime(tp.startTime);
		setEndTime(tp.endTime);
		setScope(tp.scope);
	}
	
	public TimelineParameters(){
	
	}
	
	public TimelineParameters(TimelineParameters tp){
		importTimelineParameters(tp);
	}
	
	public TimelineParameters(long st,long et,long s){
		setStartTime(st);
		setEndTime(et);
		setScope(s);
	}
}
