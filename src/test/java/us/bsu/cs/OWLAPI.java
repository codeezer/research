package us.bsu.cs;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLClass;

public class OWLAPI {
	static File ONT_FILE;
	static IRI ONT_IRI;
	static OWLOntology ontology;
	static OWLOntologyManager ontManager;
	
	public OWLAPI(String owlFilePath) {
		ONT_FILE = new File(owlFilePath);
		loadOntology();
	}
	
	public void loadOntology() {
		try {
			ontManager = OWLManager.createOWLOntologyManager();
			ontology = ontManager.loadOntologyFromOntologyDocument(ONT_FILE);
			ONT_IRI = ontology.getOntologyID().getOntologyIRI().get();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void loadClassesFromOntology() {
		ArrayList<OWLClass> classes = new ArrayList<OWLClass>();
		ontology.getClassesInSignature().forEach(classes::add);
		
		for (OWLClass owlClass: classes) {
			System.out.println(owlClass);
		}
	}
}