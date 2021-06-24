package us.bsu.cs;
import java.io.File;

import us.bsu.cs.OWLAPI;

public class CHEMIMPL {
	public static String ONT_FILE = "/home/ezer/Documents/research/implementation/files/temporal.owl";
	
	public static void main(String[] args) {
		OWLAPI owlAPI = new OWLAPI(ONT_FILE);
		owlAPI.loadClassesFromOntology();
	}
}