package timeline;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import timeline.frame.LineContainer;
import timeline.timelineDates.DateLines;

//Lifeline er basisklasse for de forskjellige former for Lifeline. Andre lifelineklasser må ekstendere denne
//En lifelineklasse skal gi rammeverk for en lifeline viewport og omliggende kontrollører

public class TimelinesView{ 
	
	private final ArrayList<Timeline> timelines;
	
	private TimelineParameters par;
	private double viewPortWidth; //bredde på viewingport
	private DateLines background;
	
	private ScrollPane viewPort;
	private Pane node;
	private ZoomTimer zoomTimer;
	
	public double getViewPortWidth() {
		return viewPortWidth;
	}

	public void setViewingPortWidth(double viewPortWidth) {
		this.viewPortWidth = viewPortWidth;
	}

	public void setTimePar(TimelineParameters p){
		par=p;
	}
	
	public TimelineParameters getPar(){
		return par;
	}
	
	public Pane getNode(){
		return node;
	}
	
	private void moveTimeline(double d){
		for (Timeline t : timelines) {		
			t.getNode().setTranslateX(d+t.getXPos());	
		}
	}
	
	private void zoom(double scale,double mouseXPos){		
		long oldScope=par.getScope();
		par.setScope((long)(par.getScope()*scale)); 
		
		double proportion=(mouseXPos/viewPortWidth);
		par.setStartTime(par.getStartTime()+(long)(oldScope*proportion)-(long)(par.getScope()*proportion)); //startTime settes slik at cursor vil stå på samme tid etter zoom er utført
		par.setEndTime(par.getStartTime()+par.getScope());
		
		updateTimeline();
	}
	
	private void updateTimeline(){
		for (Timeline t : timelines) {		
			t.requestView(par);
		}
	}
	
	private long epochDelta(double d){return (long)((d/viewPortWidth)*(double)par.getScope());}//omregner forandring i pixel til forandring i timestamp
	
	
	public static void main(String[] args) {
		//ingenting her
	}
	
	private void updateStartTime(double d){ //oppdaterer start time og tidslinje basert på hvor langt tidslinje er flyttet. 
		long newStartTime=par.getStartTime()-epochDelta(d);
  	  	
  	  	par.setStartTime(newStartTime);
  	  	par.setEndTime(newStartTime+par.getScope());
  	  	updateTimeline();
	}
	
	private void enableDrag() { //legger på eventfiltre over overordnet node slik at event aldri når ned i scrollpane.
	    final Delta dragDelta  = new Delta(); //initierer et eget definert deltaobjekt. 
	    
	    node.addEventFilter(
            MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
            	@Override public void handle(MouseEvent mouseEvent) {// overstyrer handle. Overfører nå bevegelser til deltaobjekt  
        		dragDelta.x = mouseEvent.getSceneX()- node.getTranslateX(); 
        		node.getScene().setCursor(Cursor.MOVE); //getscene finner hash til scene objekt. Setter layot for når over scene
        		mouseEvent.consume();
        	}
	    });
	    
	    node.addEventFilter(
            MouseEvent.MOUSE_RELEASED,
            new EventHandler<MouseEvent>() {
            	@Override public void handle(MouseEvent mouseEvent) {
            		updateStartTime(mouseEvent.getSceneX()-dragDelta.x);
            		dragDelta.x=0;
            		node.getScene().setCursor(Cursor.HAND); //getscene finner hash til scene objekt.  
            		mouseEvent.consume();
            	}
	    });
	    
	    node.addEventFilter(
            MouseEvent.MOUSE_DRAGGED,
            new EventHandler<MouseEvent>() {
            	@Override public void handle(MouseEvent mouseEvent) {	 
            		moveTimeline(mouseEvent.getSceneX()-dragDelta.x);
            		mouseEvent.consume();
            	}
	    });
	    
	    node.addEventFilter( //om ikke event filter konsumeres event i scrollpane tror jeg 
           ScrollEvent.ANY,
           new EventHandler<ScrollEvent>() {
            	@Override public void handle(ScrollEvent scrollEvent) {
        		if (!scrollEvent.isInertia()) {
        			if(scrollEvent.getDeltaY()>0){
        				zoom(0.85,scrollEvent.getX());
        			}else{
        				zoom(1.15,scrollEvent.getX()); 		
        			}
        		} 
        		scrollEvent.consume();
        	}
	    });
	    
	    node.addEventFilter(
            MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
            	@Override public void handle(MouseEvent mouseEvent) {
            	if (!mouseEvent.isPrimaryButtonDown()) {
            		node.getScene().setCursor(Cursor.HAND);
            		mouseEvent.consume();
            	}
            }
	    });
	    
	    node.addEventFilter(
    		MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
    			@Override public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					node.getScene().setCursor(Cursor.DEFAULT);
				}
				mouseEvent.consume();
			}
	    });
	}
	
	class Delta {double x;} //En bitteliten klasse. Er dette bedre enn arrayList?
	
	class ZoomTimer extends Timer{ //zoomtimer utfører en oppgave så etter en hvis tid der den ikke bes tilbakestilles
		
		private TimerTask timerTask;
		private long delay=700;
		private boolean running=false;

		public void startTimer() {
			running=true;
			timerTask = new TimerTask() {
				public void run() {
					running=false;
					Platform.runLater(new Runnable() {
						public void run() {
			      			
						}
					});
				}
			};
			
			this.schedule(timerTask,delay);
		}
		
		public void resetTimer() {
			if(running){
				timerTask.cancel();
				this.purge();
			}
	    }	
	}
	
	public Pane createNode(){ //lager xml. Håper å senere få lagt dette i xml fil.
		Pane p=new Pane();
		node=p; //lagrer referanse til node.
		
		viewPort=new ScrollPane();
		viewPort.getStyleClass().add("viewport");
		viewPort.prefWidthProperty().bind(node.widthProperty()); 
		viewPort.prefHeightProperty().bind(node.heightProperty()); 
		
		LineContainer lines=new LineContainer();
		lines.prefWidthProperty().bind(node.widthProperty()); 
		lines.prefHeightProperty().bind(node.heightProperty()); 
		
		background=new DateLines(par,viewPortWidth);
		timelines.add(background);//lagrer referanse til timeline
		background.getNode().prefHeightProperty().bind(node.heightProperty()); 
		
    	final Pane content=new Pane(); //elementer i scrollpane må legges i pane for å kunne dras ut av vindu uforstyrret
        content.setPrefSize(0,0); //sørger for at man ikke kan scrolle noen retning
        content.getChildren().addAll(lines,background.getNode()); 	
    
    	viewPort.setContent(content); //legger elementer for timeline i vindu
    	
    	node.getChildren().add(viewPort);
		
		node.getStyleClass().add("timeline");

		return p;
	}
	
	public TimelinesView(double w){
		viewPortWidth=w;
		timelines=new ArrayList<Timeline>();
	}
}