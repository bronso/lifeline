package timeline;


import java.util.ArrayList;

import timeline.timelineDates.DateLine;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class Timeline{
	
	protected TimelineParameters par; //timeline parametre, representerer faktisk status for tidslinje
	public TimelineParameters demands; //representerer hvilke krav som stilles til tidslinje
	public ArrayList<TimelineContainer> containerReference=new ArrayList<TimelineContainer>(); 
	protected final long anchorTimestamp;
	protected final double width;
	protected double xPos;
	private final int timelinePadding=1;
	protected Pane node;
	
	//setters
	public void setPar(TimelineParameters p){
		par=p;
	}
	
	protected void setDemands(TimelineParameters p){
		demands=p;
	}
	
	private void setXPos(double xp){
		xPos=xp;
	}
	
	//getters
	public Pane getNode(){
		return node;
	}
	
	public TimelineParameters getPar(){
		return par;
	}
	
	public double getXPos(){
		return xPos;
	}
	
	//annet
	public void addStartTime(long st){ //hvis long er mindre enn eksisterende start limit oppdaters par	
		if(st<demands.getStartTime()){	
			par.setStartTime(st);
		}
		
	}
	
	public void addEndTime(long et){ //hvis long er mindre enn eksisterende start limit oppdaters par
		if(et>demands.getEndTime()){
			par.setEndTime(et);
		}
	}
	
	protected void fillTimeline(){ //fyller timeline
		createTimelineContainer(demands.getStartTime(),demands.getEndTime());
	}
	
	protected void addStart(){ //update timeline oppdaterer timeline 
		createTimelineContainer(demands.getStartTime(),par.getStartTime()-1);//henter inn minimum en viewport bredde
	}
	
	protected void addEnd(){ //update timeline oppdaterer timeline 
		createTimelineContainer(par.getEndTime()+1,demands.getEndTime());//henter inn minimum en viewport bredde
	}
	
	protected void createTimelineContainer(long st,long et){ //timeline er fylt av timelineContainere 		
		
	}
	
	protected void addTimelineContainer(TimelineContainer c){
		containerReference.add(c);
		getNode().getChildren().add(c.getNode());
	    c.getNode().prefHeightProperty().bind(this.getNode().heightProperty()); 
	    addStartTime(c.getPar().getStartTime());
	    addEndTime(c.getPar().getEndTime());
	    updateBounds(c);
	}
	
	public TimelineDirectives checkStatus(TimelineParameters p){ //sammenligner input timelineparameter objekt med det som er i timeline og lager et demand objekt.
		
		TimelineDirectives d=new TimelineDirectives();
		
		if(par.getStartTime()==par.getEndTime()){
			d.fill();
		}else{			
			if(p.getStartTime()<par.getStartTime()){
				d.addStart();
			}
			
			if(p.getEndTime()>par.getEndTime()){
				d.addEnd();
			}
			
			if(p.getScope()!=par.getScope()){
				d.reScope();
			}
		}
		
		return d;
	}
	
	public void executeUpdate(TimelineDirectives todo){ //sørger for at timeline står i samsvar med parametre i demands		
		
		if(todo.getFill()){
			fillTimeline();
		}else{
			if(todo.getReScope()){
				zoom();
			}
			
			if(todo.getAddStart()){
				addStart();
			}
			
			if(todo.getAddEnd()){
				addEnd();
			}
		}	
		
	}
	
	public void requestView(TimelineParameters p){ //ber om at timeline stiller seg etter krav spesifisert i p
		setDemands(p);
		refreshTimeline();
	}
	
	public void refreshTimeline(){				
		this.updateXPos();
		
		TimelineDirectives todo=checkStatus(demands); //analyserer nye parametre og ser om det må gjøres oppdatering i timeline
		
		if(todo.getUpdate()){ //hvis det må gjøres oppdateringer utføres executeUpdate
			executeUpdate(todo);
		}
		
		par.setScope(demands.getScope());
	}
	
	public void updateBounds(TimelineContainer c){
		updateXPos(c);
		updateWidth(c);
	}
	
	public void updateBounds(){
		updateXPos();
	}
	
	/*public void updateXPos(TimelineParameters tp){ //oppdaterer posisjon til spesifikk timelineContainer
		this.setTranslateX(((double)startTime-tp.getStartTime())/scope*width);			
		this.setXPos(this.getTranslateX());	
	}*/
	
	public void updateXPos(TimelineContainer c){ //oppdaterer posisjon til spesifikk timelineContainer
		c.getNode().setTranslateX(((double)c.getPar().getStartTime()-anchorTimestamp)/demands.getScope()*width);			
	}
	
	public void updateXPos(){ //oppdaterer posisjon til hele timeline
		//System.out.println("1:"+this.getTranslateX());
		node.setTranslateX(((double)anchorTimestamp-demands.getStartTime())/demands.getScope()*width);			
		this.setXPos(node.getTranslateX());	
		//System.out.println("2:"+this.getTranslateX());
	}
	
	public void updateWidth(TimelineContainer c){		
		//System.out.println(getTimespanWidth(c.getPar().getStartTime(),c.getPar().getEndTime()));
		c.getNode().setPrefWidth(getTimespanWidth(c.getPar().getStartTime(),c.getPar().getEndTime()));	
		//System.out.println("start:"+c.getPar().getStartTime()+" slutt:"+c.getPar().getEndTime()+" scope:"+demands.getScope()+" width:"+width);
		//System.out.println("bredde:"+getTimespanWidth(c.getPar().getStartTime(),c.getPar().getEndTime()));
	}
	
	public void zoom(){
		for (TimelineContainer c : containerReference) {					
			updateWidth(c);
			updateXPos(c);
		}	
	}
		
	protected double getTimespanWidth(long st,long et){
		return ((double)(et-st)/demands.getScope())*width;
		
	}
	
	public Pane createNode(){
		node=new Pane();
		
		return node;
	}
	
	public Timeline(TimelineParameters p,double w,long at){
		setDemands(p);
		setPar(new TimelineParameters());
		anchorTimestamp=at;
		width=w;
		
		createNode().getStyleClass().add("timeline");
		
		//this.prefHeightProperty().bind(((Pane)this.getParent()).heightProperty());
		//System.out.println("original pref"+this.getPrefHeight());
	}
	
	
	
	public Timeline(TimelineParameters p,double w){
		
		setDemands(p);
		setPar(new TimelineParameters());
		anchorTimestamp=demands.getStartTime();
		width=w;
		createNode().getStyleClass().add("timeline");
		
	}
}
