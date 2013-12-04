package timeline.timelineLab;

import java.util.*;


public class ImportTests {
	
  // JDBC driver name and database URL
   static final String USER = "lifeline";
   static final String PASS = "nsep";
   
   private Map<Integer, Map<String,Object>> testArray =new HashMap<Integer,Map<String,Object>>(); //inneholder bare id på tester som er innenfor et eventuelt timespan. Full test_array lages av annen klasse.
   private ArrayList<Integer> currentTests =new ArrayList<Integer>();
   private Integer patientId= null;
   private Integer startTime= null;
   private Integer endTime= null;
   private DatabaseUtils db=null;
   
   public static void main(String[] args) {
			
   }
   
   public void setPatientId(Integer pi)
   {
	   patientId = pi;
   }
   
   public void setStartTime(Integer st)
   {
	   startTime = st;
   }
   
   
   public void setEndTime(Integer et)
   {
	   endTime = et;
   }
   
   public  ArrayList<Integer> getCurrentTests() //returns test inside timespan
   {   
	   return currentTests; 
   }
   
   private String queryStringTimespan(int st, int et) //returns string to specify timespan in query
   {
	   String tq=" tr.timestamp >="+Integer.toString(st)+" AND tr.timestamp <="+Integer.toString(et) ;
	   
	   return tq;
   }
   
   private String queryStringPatient() //returns string to specify patient in query
   {
	   String pq="";
	   pq=" tr.patient_id ="+Integer.toString(patientId)+" ";
	 
	   return pq;
   }
   
   private String queryStringSelection() //returns string to specify timespan in query
   {
	   String query=" ";
 
	   try
	   {
		   if(!currentTests.isEmpty() && currentTests!=null){
			   
			   query="AND (";
			   
			   for (Integer tId : currentTests) {     
				   query=query+"t.id = "+tId+" OR ";
			   } 
			   
			   query = query.substring(0, query.length()-3);
			   
			   query=query+")";
		   }  
	   }catch(Exception e)
	   {
		   System.out.println(e.getMessage());
	   }
		
	   
	   return query;
   }
   
   private String buildCurrentTestQuery(int startTime,int endTime){ //returns query witch returns current tests based on patient id and timespan
    
	   String query = 	"SELECT t.id " +
	   					"FROM tests t " +
	   					"WHERE " +
	   					"	t.id IN(" +
	   					"	SELECT tr.test_id " +
	   					"	FROM test_results AS tr " +
	   					"	WHERE "+
	   							queryStringTimespan(startTime,endTime)+" AND "+
	   							queryStringPatient()+
	   					"	)";    
	  return query;
   }
   
   private String buildCurrentTestQuery() //returns query witch returns current tests based on patient id
   {
	   String query = 	"SELECT t.id " +
	   					"FROM tests t " +
	   					"WHERE " +
	   					"	t.id IN(" +
	   					"	SELECT tr.test_id " +
	   					"	FROM test_results AS tr " +
	   					"	WHERE "+
	   							queryStringPatient()+
	   					")"; 
	   return query;
   }
   
   public void buildCurrentTests()
   {   
	   currentTests.clear();
	  
	   if(startTime!=null && endTime!=null && startTime!=endTime)
	   {
		   createCurrentTests(buildCurrentTestQuery(startTime,endTime));
	   }else{
		   createCurrentTests(buildCurrentTestQuery());
	   }  
   }
   
   private void createCurrentTests(String query)
   {   
	   List<Map<String,Object>> queryResult = null;
	   
	   try
	   {   
		   queryResult = db.query(query);	  
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	 
	   for (Map<String,Object> t : queryResult) {  
		   String key="ID";
		   Integer tId=(Integer) t.get(key);
		   currentTests.add(tId); 
	   }
   }
   
   private String createTestQuery() //returns string to return tests
   {
	   String query;
	   
	   if(currentTests==null || currentTests.isEmpty())
	   {
		   buildCurrentTests();
	   }
	   
	   String testSelectionQuery=queryStringSelection();
  
	   query=	"SELECT " +
	   			"	t.*, " +
	   			"	(SELECT ISNULL(MAX(result_int),0) FROM test_results WHERE test_id=t.id AND patient_id='"+patientId+"') as max_value, " +
	   			"	(SELECT ISNULL(MIN(result_int),0) FROM test_results WHERE test_id=t.id AND patient_id='"+patientId+"') as min_value " +
	   			"FROM " +
	   			"	tests AS t " +
	   			"WHERE 1=1 " +
	   			"	"+testSelectionQuery+
	   			"ORDER BY t.id";
	 
	   return query; 
   }
   
   public void buildTestArray() //lager test array
   {
	   db = new DatabaseUtils();
	   List<Map<String,Object>> queryResult = null;
	   
	   try
	   {
		   db.createConnection();
		   queryResult = db.query(createTestQuery());
		   db.closeConnection();
		   
		   for (Map<String,Object> r : queryResult) {  
			   
			   Map<String,Object> t = new HashMap<String,Object>(); //lager object t som skal legges i testArrray
			   
			   t.put("id",r.get("ID"));
			   t.put("name",r.get("NAME"));
			   t.put("topRange",r.get("POS1"));
			   t.put("bottomRange",r.get("NEG1"));
			   t.put("topLimit",r.get("POS2"));
			   t.put("bottomLimit",r.get("NEG2"));
			   t.put("topResult",r.get("MAX_VALUE"));
			   t.put("bottomResult",r.get("MIN_VALUE"));
			   t.put("status",false);
			   
			   //System.out.println(t.toString());
			   
			   Integer middleRange=(((Integer)t.get("topRange")-(Integer)t.get("bottomRange"))/2)+(Integer)t.get("bottomRange");
			   t.put("middleRange",middleRange);
			   
			   testArray.put((Integer)t.get("id"),t);
			   
			   t=null;//sletter referanse til object t etter at det er lagret i testArray
		   }
		   
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
   }
   
}
