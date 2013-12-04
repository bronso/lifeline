package timeline.timelineDates;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import timeline.TimelineParameters;

//Dates inneholder og administrer bakgrunn i tidslinje. 
public class DateLines extends DateLineBase{
		
	public HashMap<Integer,DateLine> lineReference=new HashMap<Integer,DateLine>(); 
	private double minWidth=100d;
	private static final long timeUnitLength[]= {1000,10000,60000,900000,3600000,21600000,86400000,604800000,2629743000L,7889231000L,31556926000L,315569260000L}; //helper array

	private static int findTimeUnit(long s,double w,double mw){ //scope, width,minimumwidth. Finner største timeunit som trenger mindre enn 70px for å vise label
		int tu=0;
		
		for(int i=0; i<timeUnitLength.length; i++)
		{
			tu=i;	
			
			if((((double)timeUnitLength[i]/(double)s)*w)>mw)
			{	
				break;
			}
		}	
		
		return tu;
	}
	
	private void pickLines(){ //fjerner og legger til DateLines etter behov.
		
		/*for (Node n : this.getChildren()) {
			DateLine dl=(DateLine)n;
			int tu=dl.getTimeUnit();
	        if(tu<(getTimeUnit()-1) || tu>(getTimeUnit()+1) ){
	        	this.getChildren().remove(n);
	        }
	    }*/
		
		ArrayList<Integer> toRemove=new ArrayList<Integer>(); //holder id for linjer som skal fjernes

		for (DateLine dl : this.lineReference.values()) {
			
			int tu=dl.getTimeUnit();
			 
			if(tu<(getTimeUnit()-1) || tu>(getTimeUnit()) ){
				toRemove.add(tu);
			}
		}
		
		for(int i:toRemove){
			if(lineReference.containsKey(i)){	
				removeDateLine(lineReference.get(i)); //fjerner linje
			}
		}

		
	    ArrayList<Integer> toImport=new ArrayList<Integer>();

		toImport.add(getTimeUnit()-1);
		toImport.add(getTimeUnit());
		//toImport.add(getTimeUnit()+1);
		
		for(int i:toImport){
			if(!lineReference.containsKey(i) && i>0 && i<12){	
				addDateLine(demands,i);
			}
		}
		
		styleLines();
	}
	
	private String getLineStyle(int tu){
		String className="";
		
		if(getTimeUnit()==tu){
			className="dates-main";
		}else if(getTimeUnit()>tu){
			className="dates-child";
		}else if(getTimeUnit()<tu){
			className="dates-parent";
		}
	
		return className;
	}
	
	private void styleLine(DateLine dl){
		dl.getNode().getStyleClass().removeAll("dates-main","dates-parent","dates-child");
		dl.getNode().getStyleClass().add("dates-default");
		dl.setLineStyle(getLineStyle(dl.getTimeUnit()));
	}
	
	private void styleLines(){		
		//System.out.println("StyleLines:tu="+((DateTimelineParameters)par).getTimeUnit());
		lineReference.get(getTimeUnit()).getNode().toFront(); //plasserer aktuell dateline fremst
		
		for ( DateLine dl : lineReference.values()) {
			styleLine(dl); 
	    }
		
	}
	
	public void updateScope(int tu){
		setTimeUnit(tu);
		pickLines();
	}
	
	public void refreshTimeline(){ //refresh timeline
		
		this.updateXPos();
		
		int tu=findTimeUnit(demands.getScope(),width,minWidth);
		
		if(tu!=getTimeUnit())
		{
			updateScope(tu);
		}
		
		for (DateLine d : this.lineReference.values()) {		
			d.requestView(demands);			
		}	
	}
	
	private void removeDateLine(DateLine dl){
		lineReference.remove(dl.getTimeUnit());
		this.getNode().getChildren().remove(dl.getNode());
	}
	
	private void addDateLine(TimelineParameters tp,int tu){ //lager dateline og legger i datelines	
		DateLine dl=new DateLine(tp,width,tu,anchorTimestamp);
		dl.getNode().prefHeightProperty().bind(this.getNode().heightProperty()); 
		lineReference.put(tu,dl);
		this.getNode().getChildren().add(dl.getNode());
		
	}
	
	public DateLines(TimelineParameters p,double w){
		super(p,w);	
		setTimeUnit(findTimeUnit(p.scope,w,minWidth));	
		pickLines();
	}
}