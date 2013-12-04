package timeline.frame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import timeline.units.LabUnit;

//demo modelert etter bounds playground

public class Grid extends Application {//ekstendere application for at String args skal defineres
	
	public Grid(){ //constructor, setter tidsintervall for timeline
		
	}
	
	public static void main (String[] args) { 
		Application.launch(Grid.class, args);
	}
	
	private void addStyles(Scene s){ //legger til styling
		s.getStylesheets().add(Grid.class.getResource("../../lifeline.css").toExternalForm());
		s.getStylesheets().add(Grid.class.getResource("../../lifeline-dates.css").toExternalForm());
		s.getStylesheets().add(Grid.class.getResource("../../lifeline-timeline.css").toExternalForm());
		s.getStylesheets().add(Grid.class.getResource("../../lifeline-line.css").toExternalForm());
	}
	
	public void start(Stage stage) { //start får en stage som argument (hva er stage?)
		
		stage.setTitle("Lifeline"); //setter tittel til stage
		
		final GridPane grid = new GridPane(); //lager en gridpane. grid vil være rammeverk for hele designet
		grid.getStyleClass().add("main-frame");
	
		Label header=new Label("Lifeline");
		header.getStyleClass().add("h1");
		grid.add(header,0,0);
		
    	grid.add(new LabUnit(),0,1); //legger vindu for timeline i grid
    
    	final Scene scene = new Scene(grid, 1300, 700); //lager en scene, legger objekter
		grid.prefHeightProperty().bind(scene.heightProperty()); //setter høyde på grid. Denne er nå bundet til størrelsen på vindu
		addStyles(scene);
		
		stage.setScene(scene);
		stage.show();	
	}
}
