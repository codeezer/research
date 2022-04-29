package demo;

import java.io.IOException;
import com.uppaal.engine.CannotEvaluateException;
import com.uppaal.engine.EngineException;


public class TestAPI {
    
	public static void main(String[] args) {
        if (args.length<1) {
            System.out.println("This is a demo of Uppaal model.jar API");
            System.out.println("Use one of the following arguments:");
            System.out.println("  hardcoded");
            System.out.println("  <URL>");
            System.out.println("  <path/file.xml>");
            System.exit(0);
        }
        
        try {
        	String modelFile = args[0];
        	UppaalAPI uppaalAPI = new UppaalAPI(modelFile);

			// SymbolicState state = engine.getInitialState(system);
			
			//	String query1  =  "E<> intakePump1.running";
			//	char queryStatus1 = uppaalAPI.getQueryStatus(query1, "will intakePump be running?"); 
			//	System.out.println(query1 + ": " + queryStatus1);
			//	
			//	String query2  =  "E<> emergencyFlush.running";
			//	char queryStatus2 = uppaalAPI.getQueryStatus(query2, "will emergencyFlush be running?"); 
			//	System.out.println(query2 + ": " + queryStatus2);
        	
        	// uppaalAPI.printCurrentState();
        	uppaalAPI.symbolicSimulation(50);
        	
        	// testAPI.symbolicSimulation(uppaalAPI, 10000, true);
        	
        	System.out.println("-".repeat(20));
			uppaalAPI.endConnection();
			
        } catch (EngineException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (CannotEvaluateException e) {
			e.printStackTrace();
		}
    }
}
