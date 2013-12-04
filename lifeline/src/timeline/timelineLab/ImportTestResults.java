package timeline.timelineLab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportTestResults {

	private ArrayList<Integer> currentTests =new ArrayList<Integer>();
	
	private List<Map<String, Object>> resultRaw = null;
	
	private Map<Integer,Map<Integer,Integer>> resultInt=null; //inneholder svar som integer, sortert etter testId, deretter timestamp
	private Map<Integer,Map<Integer,String>> resultString=null; //inneholder svar som string, sortert etter testId, deretter timestamp
	private Map<Integer,Map<Integer,String>> resultTable=new HashMap<Integer,Map<Integer,String>>(); //inneholder svar som string, sortert etter timestamp, deretter testId
	private ArrayList<Integer> resultKeys = new ArrayList<Integer>(); //inneholder liste over alle timestamp det er registrert prøveresultat på 
	
	private boolean limit=true; //defines if limits should be defined. If true, the class will import the first results out of bounds defined by start and en time.
	private int startTime=0; //start time for search of results
	private int endTime=0; //end time for search og results
	private int patientId=0; //set current patient
	
	public void setCurrentTests(ArrayList<Integer> ct){
		currentTests=ct;
	}
	
	public void setStartTime(int st){
		startTime=st;
	}
	
	public void setEndTime(int et){
		endTime=et;
	}
	
	public void setPatientId(int pi){
		patientId=pi;
	}
	
	public void setLimit(boolean l){
		limit=l;
	}
	
	public  Map<Integer,Map<Integer,Integer>> getResultInt(){ //fyller array hvis den er null
		
		if(resultInt==null){
			populateGraphArrays();
		}
		
		return resultInt;
	}
	
	public  Map<Integer,Map<Integer,String>> getResultString(){ //fyller array hvis den er null
		
		if(resultString==null){
			populateGraphArrays();
		}
		
		return resultString;
	}
	
	public void populateGraphArrays(){ //resultInt og resultGraph hentes ofte ut sammen. Derfor lages de sammen hvis en forsøkes å hentes ut
		if(resultRaw==null){
			populateRawResult();
		}
		
		resultString=new HashMap<Integer,Map<Integer,String>>();
		resultInt=new HashMap<Integer,Map<Integer,Integer>>();
		
		for(Integer tId:currentTests){
			resultInt.put(tId,new HashMap<Integer,Integer>());
			resultString.put(tId,new HashMap<Integer,String>());
		}
		
		for(Map<String, Object> row:resultRaw){
			int tId=(Integer)row.get("TEST_ID");
			int ts=(Integer)row.get("TIMESTAMP");
			int r=(Integer)row.get("RESULT_INT");
			String rs=(String)row.get("RESULT");

			resultInt.get(tId).put(ts, r);
			resultString.get(tId).put(ts,rs);
		}
	}
	
	private String queryStringSelection()
	{
		String query;
		
		query=" (";
		
		for(int t:currentTests){
			query=query+" r.test_id='"+t+"' OR";
		}
		
		query = query.substring(0, query.length()-2);
		
		query=query+") ";
		
		return query;
	}
	
	private String queryStringPatient()
	{
		String query=" patient_id='"+patientId+"' ";
		return query;
	}
	
	private String queryStringTimespan()
	{
		String query="SELECT r.* FROM test_results as r WHERE r.timestamp BETWEEN "+startTime+" AND "+endTime ;
		return query;
	}
	
	private String queryStringLimit()
	{
		String query;
		
		query=	"SELECT r.* FROM test_results AS r " +
				"WHERE r.timestamp " +
				"BETWEEN IFNULL((" +
				"	SELECT r2.timestamp " +
				"	FROM test_results AS r2 " +
				"	WHERE r2.timestamp<"+startTime+" " +
				"	AND r2.test_id=r.test_id " +
				"	AND r2.patient_id="+patientId+" " +
				"	ORDER BY r2.timestamp DESC " +
				"	LIMIT 1" +
				"),"+startTime+") " +
				"AND IFNULL((" +
				"	SELECT r3.timestamp " +
				"	FROM test_results AS r3 " +
				"	WHERE r3.timestamp>"+endTime+" "+
				"	AND r3.test_id=r.test_id " +
				"	AND r3.patient_id="+patientId+" " + 
				"	ORDER BY r3.timestamp ASC " +
				"LIMIT 1" +
				"),"+endTime+")" ;
		
		//System.out.println(query);
		
		return query;
	}
	
	private String createQueryTimespan() //henter alle data innen gitt timespan
	{
		String query;
		query=queryStringTimespan()+" AND "+queryStringPatient();
		return query;
	}
	
	private String createQueryTimespanSelection() //henter alle data innen gitt timespan
	{
		String query;
		query=queryStringTimespan()+" AND "+queryStringSelection()+" AND "+queryStringPatient();
		return query;
	}
	
	private String createQueryLimit(){ //henter alle rader innen timespan pluss tilgrensende resultat
	
		String query=queryStringLimit()+" AND "+queryStringPatient();			
		return query;
	}
	
	private String createQueryLimitSelection(){
		String query=queryStringLimit()+" AND "+queryStringSelection()+" AND "+queryStringPatient();
		return query;
	}
	
	private String createQueryAll() //henter alle resultater for gitt pasient
	{
		String query="SELECT * FROM test_results WHERE "+queryStringPatient();
		return query;
	}
	
	public void populateRawResult(){
		String query=" ";
		
		if(patientId!=0){
			if(startTime!=endTime){
				if(currentTests.isEmpty()){
					if(limit){
						query=createQueryLimit();
					}else{
						query=createQueryTimespan();
					}
				}else{
					if(limit){
						query=createQueryLimitSelection();
					}else{
						query=createQueryTimespanSelection();
					}
				}
			}else{
				query=createQueryAll();
			}
		}
		
		DatabaseUtils db = new DatabaseUtils();
		
		try
		{
			db.createConnection();
			resultRaw = db.query(query);
			db.closeConnection();
			
			//System.out.println(resultRaw.toString());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args) {
		

	}

}
