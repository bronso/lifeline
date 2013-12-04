package timeline.frame;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

//en lifeline unit representerer et rammeverk som lifelines kan legges i. Lifeline frame består av en LifelineViewPort og en LifelineSideBar på hver side. 
//Forskjellige Lifelines kan legges i LifelineFrame. Disse kontrolleres av en overordnet LifelineController som er separat fra LifelineFrame. 
//Typisk vil LifelineController dekorere LifelineFrame med Lifelines. 

public class LifelineFrame extends GridPane{

	private Region vPort=new Region();
	private Region s1=new Region();
	private Region s2=new Region();
	private double s1Width=50;
	private double s2Width=200;
	private double vPortWidth=700;
	
	public void setS1(Region s){
		s1=s;
	}
	
	public void setS2(Region s){
		s2=s;
	}
	
	public void setVPort(Region s){
		vPort=s;
	}
	
	public Region getVPort() {
		return vPort;
	}

	public void setvPort(Region vPort) {
		this.vPort = vPort;
	}

	public Region getS1() {
		return s1;
	}

	public Region getS2() {
		return s2;
	}

	public void addToGrid(Region r,int c,double w){
		this.add(r,c,0);
		ColumnConstraints columnConstraint = new ColumnConstraints();
		columnConstraint.setPrefWidth(w);
        this.getColumnConstraints().add(columnConstraint); //legger til bredde
        r.prefHeightProperty().bind(this.heightProperty()); 
	}
	
	public LifelineFrame(){
        this.setGridLinesVisible(true); //for utvikling
        this.setPrefHeight(300);
	}
}
