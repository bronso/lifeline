package timeline.timelineDates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class TimelineDates extends GridPane{
	
	private long startTime=0;
	private long endTime=0;
	private long startLimit=0;
	private long endLimit=0;
	private long scope=0;
	private long externalScope=0;
	private double nodeWidth=1;
	private int timeUnit=0; //hva slags tidsenhet som skal vises på tidslinje
	
	
	public void setStartTime(long st){
		startTime=st;
	}
	
	public void setEndTime(long et){
		endTime=et;
	}
	
	public void setStartLimit(long sl){
		startLimit=sl;
	}
	
	public void setExternalScope(long es){
		externalScope=es;
	}
	
	public void setEndLimit(long el){
		endLimit=el;
	}
	
	public void setScope(long s){
		scope=s;
	}
	
	public void setNodeWidth(double w){
		nodeWidth=w;
	}
	
	public void setTimeUnit(int tu){
		timeUnit=tu;
	}
	
	public double getNodeWidth(){
		return nodeWidth;
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	public long getEndTime(){
		return endTime;
	}
	
	private static long getTimeUnitStart(int timeUnit,long timeStamp){ //finner første millisekund for for gitt tidsintervall. Eks hvis timestamp står til 15 mai og timeunit er måned finnes 1 mai ved midnatt
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo"));
		cal.setTimeInMillis(timeStamp); 
		
		//starter med å rullere gjennom tidseneheter fra bunn til topp. Hver enhet som beahndles nullstilles.
		
		for(int ctu = 0; ctu<=timeUnit; ctu++){

			switch(ctu){
				case 0: //sekund
					cal.clear(Calendar.MILLISECOND);
				break;
				case 2: //minutt
					cal.clear(Calendar.SECOND);
					break;
				case 4: //time
					cal.set(Calendar.MINUTE,0);
					break;
				case 6: //dag
					cal.set(Calendar.HOUR_OF_DAY,0);
					break;
				case 8: //måned
					cal.set(Calendar.DAY_OF_MONTH,1);
					break;
				case 10: //år
					cal.set(Calendar.MONTH,0);
					break;
				default:
					break;
			}
		}
		
		switch(timeUnit){
			
			case 1: //10 sek
				int tenSec=(int)Math.floor(cal.get(Calendar.SECOND)/10);
				cal.set(Calendar.SECOND,tenSec*10);
				break;
			case 3: //kvarter
				int quarter=(int)Math.floor(cal.get(Calendar.MINUTE)/15);
				cal.set(Calendar.MINUTE, quarter*15);
				break;
			case 5: //tid på dagen (morgen, ettermiddag,kveld,natt)
				int daytime=(int)Math.floor(cal.get(Calendar.HOUR)/6);
				cal.set(Calendar.HOUR,daytime*6);
				break;
			case 7: //uke
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				break;
			case 9: //sesong
				int quart=(int)Math.floor(cal.get(Calendar.MONTH)/3);
				cal.set(Calendar.MONTH,quart*3);
				break;
			case 11: //tiår
				int y =cal.get(Calendar.YEAR);				
				y=(int)(Math.floor(y/ 10.0) * 10);				
				cal.set(Calendar.YEAR,y);
				break;
		}
		
		
		return cal.getTimeInMillis();
	}
	
	private static long getTimeUnitEnd(int timeUnit,long timeStamp){ //finner siste millisekund i gitt tidsintervall 
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(getTimeUnitStart(timeUnit,timeStamp));
		
		cal=addTimeUnit(cal,timeUnit);
		
		return cal.getTimeInMillis()-1;		
	}
	
	private static Calendar addTimeUnit(Calendar cal, int timeUnit){ //legger tidsenhet til calndar objekt
		
		switch(timeUnit){
			case 0: //sekund
				cal.add(Calendar.SECOND,1);
				break;
			case 1: //10 sekund
				cal.add(Calendar.SECOND,10);
				break;
			case 2: //minutt
				cal.add(Calendar.MINUTE,1);
				break;
			case 3: //kvarter
				cal.add(Calendar.MINUTE,15);
				break;
			case 4: //time
				cal.add(Calendar.HOUR,1);
				break;
			case 5: //tid på dagen
				cal.add(Calendar.HOUR,6);
				break;
			case 6: //dag
				cal.add(Calendar.DAY_OF_MONTH,1);
				break;
			case 7: //uke
				cal.add(Calendar.DAY_OF_YEAR,7);
				break;
			case 8: //måned
				cal.add(Calendar.MONTH,1);
				break;
			case 9: //sesong
				cal.add(Calendar.MONTH,3);
				break;
			case 10: //år
				cal.add(Calendar.YEAR,1);
				break;
			case 11: //tiår
				cal.add(Calendar.YEAR,10);
				break;
		}
		
		return cal;
	}
	
	private static List<List<Long>> createTimelineTimestamps(int timeUnit, long sl, long el){ //sl=startLimit el=endLimit
		
		List<List<Long>> par=new ArrayList<List<Long>>();
		
		long currentTime=sl;
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		
		while(currentTime<=el){
			
			ArrayList<Long> unitPar=new ArrayList<Long>();
			
			unitPar.add(currentTime);
			
			cal=addTimeUnit(cal,timeUnit);
			
			currentTime=cal.getTimeInMillis();
			unitPar.add(currentTime-1);
			
			par.add(unitPar);
			
		}
		
		return par;
	}
	
	public static String formatMonth(int month) {
		Locale locale =new Locale("no");
		DateFormat formatter = new SimpleDateFormat("MMM", locale);
	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.set(Calendar.DAY_OF_MONTH,1);
	    calendar.set(Calendar.MONTH, month);
	    return formatter.format(calendar.getTime());
	}
	
	public static String formatDay(int day) {
		Locale locale =new Locale("no");
		DateFormat formatter = new SimpleDateFormat("E", locale);
	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.set(Calendar.DAY_OF_WEEK, day);
	    return formatter.format(calendar.getTime());
	}
	
	private static String getTimeUnitLabel(int timeUnit,long timeStamp){
		
		String label="";
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		
		switch(timeUnit){
			case 0: //sekund
				label= new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
				break;
			case 1: //10 sek
				label= new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
				break;
			case 2: //minutt 
				label= new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
				break;
			case 3: //kvarter
				label= new SimpleDateFormat("HH:mm").format(cal.getTime());
				break;
			case 4: //time
				label= new SimpleDateFormat("HH:mm").format(cal.getTime());
				break;
			case 5: //tid på dag
				switch(cal.get(Calendar.HOUR_OF_DAY)){
					case 0:
						label="Natt "+new SimpleDateFormat("dd/MM").format(cal.getTime());
						break;
					case 6:
						label="Morgen "+new SimpleDateFormat("dd/MM").format(cal.getTime());
						break;
					case 12:
						label="Ettermiddag "+new SimpleDateFormat("dd/MM").format(cal.getTime());
						break;
					case 18:
						label="Kveld "+new SimpleDateFormat("dd/MM").format(cal.getTime());
						break;
					default:
						label=" "+cal.get(Calendar.HOUR_OF_DAY);
						break;
				}
				break;
			case 6: //dag
				label=formatDay(cal.get(Calendar.DAY_OF_WEEK))+" "+new SimpleDateFormat("dd/MM/YY").format(cal.getTime());
				break;
			case 7: //uke
				label="Uke "+new SimpleDateFormat("ww YYYY").format(cal.getTime());
				break;
			case 8: //måned
				label=formatMonth(cal.get(Calendar.MONTH))+" "+cal.get(Calendar.YEAR);
				break;
			case 9://kvartal
				
				int quarter=(int)Math.floor(cal.get(Calendar.MONTH)/3)+1;
				label=quarter+". kvartal "+new SimpleDateFormat("YYYY").format(cal.getTime());
				
			break;
			case 10: //år
				label=""+new SimpleDateFormat("YYYY").format(cal.getTime());
				break;
			case 11: //tiår
			//	int decade=cal.get(Calendar.YEAR) % 100;
				int decade=cal.get(Calendar.YEAR);
				label= decade+"-tallet";	
				break;
			case 12: //fullt
				label=new SimpleDateFormat("dd/MM/YY kk:mm:ss").format(cal.getTime());
				break;
					
		}
		
		return label;
	}
	
	public void populate(List<List<Long>> timeStamps){ //fyller gridpane med passende celler basert på array laget i createTimelineTimestamps. 
	
		int column=0;
		int row=0;
		
		for(List<Long> ts:timeStamps){
			
			long timeUnitStart=ts.get(0);
			long timeUnitEnd=ts.get(1);
			
			Pane p=new Pane();
			p.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			p.setSnapToPixel(false);

			Label l=new Label(getTimeUnitLabel(timeUnit,timeUnitStart));
			
			p.getChildren().add(l);
			
			//l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			//l.setStyle("-fx-alignment: top-left;-fx-border-style:solid;-fx-border-width:0 1 0 0;-fx-border-color:grey;-fx-label-padding:0 0 0 5;");
			this.add(p,column,row);	
			l=null;
	
			ColumnConstraints columnConstraint = new ColumnConstraints();
			
			columnConstraint.setPercentWidth((double)(timeUnitEnd-timeUnitStart)/scope*100L);
	        this.getColumnConstraints().add(columnConstraint); //legger til bredde
	       
	        column++;
		}
	}
	
	public TimelineDates(long sl,long el, long s,int tu) {
		
		setScope(el-sl);
		setExternalScope(s);
		setStartLimit(sl);
		setEndLimit(el);
		setTimeUnit(tu); //finner og setter timeUnit, som bestemmer hva slags tidsenhet tidslinje skal deles opp i
		
		//System.out.println(sl+" "+el+" "+s+" "+tu);
		//System.out.println(getTimeUnitStart(timeUnit,startLimit));
		
		this.getStyleClass().add("date-line");
		
		setStartTime(getTimeUnitStart(timeUnit,startLimit)); //finner hvor mye tidslinje vil gå utover grenser for å passe med tidsenheter som skrives
		
		setEndTime(getTimeUnitEnd(timeUnit,endLimit));		
		
		RowConstraints rowConstraint = new RowConstraints(); //legger til styling for rad
	    rowConstraint.setPercentHeight(100);
	    this.getRowConstraints().add(rowConstraint);
    
        this.setSnapToPixel(false); 
		
        List<List<Long>> timeStamps=createTimelineTimestamps(timeUnit,startTime,endLimit);
		populate(timeStamps);
		
		setNodeWidth((double)(endTime-startTime)/externalScope*nodeWidth);	
	}
	
	

}
