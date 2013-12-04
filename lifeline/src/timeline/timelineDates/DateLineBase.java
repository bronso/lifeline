package timeline.timelineDates;

import timeline.Timeline;
import timeline.TimelineParameters;

public abstract class DateLineBase extends Timeline {
	
	protected int timeUnit=0;
	
	public DateLineBase(TimelineParameters p,double w) {
		super(p,w);
	}
	
	public DateLineBase(TimelineParameters p,double w,long at) {
		super(p,w,at);
	}

	public int getTimeUnit(){//legger til hjelpende funksjoner f�r � minske stress med timeUnit. Sipper n� � caste par hele tiden.
		return timeUnit;
	}
	
	public void setTimeUnit(int tu){
		timeUnit=tu;
	}
}
