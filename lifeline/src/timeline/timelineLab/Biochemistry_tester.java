package timeline.timelineLab;

import java.util.*;


public class Biochemistry_tester {

	
	public static void main(String[] args) {
		
		ImportTests tests= new ImportTests();
		tests.setPatientId(1);
		tests.setStartTime(0);
		tests.setEndTime(1370211998);
		
		tests.buildTestArray();
		
		ImportTestResults testResults= new ImportTestResults();
		testResults.setPatientId(1);
		testResults.setStartTime(0);
		testResults.setEndTime(1370211998);
		testResults.setCurrentTests(tests.getCurrentTests());
		
		Map<Integer,Map<Integer,Integer>> rI =testResults.getResultInt();
	
		
		/*DatabaseUtils db = new DatabaseUtils();
		
		List<Map<String,Object>> queryResult = null;
		
		String query="SELECT * FROM tests";
		
		try
		{
			db.createConnection();
			queryResult = db.query(query);
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
		   
		}
		 
		for (Map<String,Object> t : queryResult) {  
			
			Integer tId=(Integer) t.get("ID");
			
			Integer neg1=((Integer)t.get("NEG1")!=null)?(Integer)t.get("NEG1")*100:0;
			Integer neg2=((Integer)t.get("NEG2")!=null)?(Integer)t.get("NEG2")*100:0;
			Integer pos1=((Integer)t.get("POS1")!=null)?(Integer)t.get("POS1")*100:0;
			Integer pos2=((Integer)t.get("POS2")!=null)?(Integer)t.get("POS2")*100:0;
			
			try
			{
				if(pos1!=null){db.update("UPDATE tests SET pos1='"+pos1+"' WHERE id='"+tId+"'");}
				if(pos2!=null){db.update("UPDATE tests SET pos2='"+pos2+"' WHERE id='"+tId+"'");}
				if(neg1!=null){db.update("UPDATE tests SET neg1='"+neg1+"' WHERE id='"+tId+"'");}
				if(neg2!=null){db.update("UPDATE tests SET neg2='"+neg2+"' WHERE id='"+tId+"'");}
				
				System.out.println("Suksess!");
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally{
			   
			}
			
		}
		
		try
		{
			db.closeConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
		   
		}
		*/
	}

}
