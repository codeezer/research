package us.bsu.cs;

// import java.util.Calendar;

public class testAPI {
	public static String CHEM_ONT_FILE = "/home/ezer/Documents/research/research/ontology/models/caseStudy.owl";
	// public static String TEMP_ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	// public static String TEMP_ONT_FILE = "/Users/ezer/Desktop/research/implementation/research/files/temporal.owl";
	// public static String CHEM_ONT_FILE = "/Users/ezer/Desktop/research/implementation/research/files/chemicalProcess.owl";
	
	public static void main(String[] args) {
		OWLAPI owlAPI = new OWLAPI(CHEM_ONT_FILE);
		owlAPI.startReasoner();
		owlAPI.isConsistent();
		System.out.println("Program Ends!");
	}
}