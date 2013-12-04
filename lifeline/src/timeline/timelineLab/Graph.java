package timeline.timelineLab;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Graph extends Application {
	
	private Map<Integer,Map<Integer,Integer>> resultInt=null; //inneholder prøvesvar som int, sortert etter testId, deretter timestamp
	private Map<Integer, Map<String,Object>> tests =new HashMap<Integer,Map<String,Object>>(); //inneholder metadata om tester
	private Map<Integer,List<List<Integer>>> graphPos =new HashMap<Integer,List<List<Integer>>>(); //inneholder posisjoner for graf elementer
	
	private int graphHeight=0;
	private int graphWidth=0;
	private int startTime=0;
	private int endTime=0;
	
	private int rangeWidth=10;
	
	private boolean relative=true;
	
	public void setStartTime(int st){
		startTime=st;
	}
	
	public void setEndTime(int et){
		endTime=et;
	}
	
	public void setGraphWith(int gw){
		graphWidth=gw;
	}
	
	public void setGraphHeight(int gh){
		graphHeight=gh;
	}
	
	public void setRangeWidth(int rw){
		rangeWidth=rw;
	}
	
	public void setResultInt(Map<Integer,Map<Integer,Integer>> ri){
		resultInt=ri;
	}
	
	public void setTests(Map<Integer, Map<String,Object>> t){
		tests=t;
	}
	
	public void setTestParameters(){
		
	}
	
	public void populateGraphPos(){
		
		for(Map<String,Object> t: tests.values()){
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLACK);
        gc.setFont(Font.getDefault());
        gc.fillText("hello   world!", 15, 50);

        gc.setLineWidth(5);
        gc.setStroke(Color.PURPLE);

        gc.strokeOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.strokeRect(30, 100, 40, 40);
        
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

		
	

}
