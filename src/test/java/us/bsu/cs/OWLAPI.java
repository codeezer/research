package us.bsu.cs;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class OWLAPI {
	static File ONT_FILE;
	static IRI ONT_IRI;
	static OWLOntology ontology;
	static OWLOntologyManager ontManager;
	static OWLDataFactory dataFactory;
	static OWLReasonerFactory reasonerFactory = new ReasonerFactory();
	static OWLReasoner reasoner;
	
	public OWLAPI(String owlFilePath) {
		ONT_FILE = new File(owlFilePath);
		loadOntology();
	}
	
	public void loadOntology() {
		try {
			ontManager = OWLManager.createOWLOntologyManager();
			ontology = ontManager.loadOntologyFromOntologyDocument(ONT_FILE);
			ONT_IRI = ontology.getOntologyID().getOntologyIRI().get();
			dataFactory = ontManager.getOWLDataFactory();
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
	
	public void loadIndividualsFromOntology() {
		ArrayList<OWLIndividual> individuals = new ArrayList<OWLIndividual>();
		ontology.getIndividualsInSignature().forEach(individuals::add);
		
		for (OWLIndividual owlIndividual: individuals) {
			System.out.println(owlIndividual);
		}
	}
	
	public void loadDataPropertyFromOntology() {
		ArrayList<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		ontology.getDataPropertiesInSignature().forEach(dataProperties::add);
		
		for (OWLDataProperty owlDataProperty: dataProperties) {
			System.out.println(owlDataProperty);
		}
	}
	
	public void loadObjectPropertyFromOntology() {
		ArrayList<OWLObjectProperty> ObjectProperties = new ArrayList<OWLObjectProperty>();
		ontology.getObjectPropertiesInSignature().forEach(ObjectProperties::add);
		
		for (OWLObjectProperty owlObjectProperty: ObjectProperties) {
			System.out.println(owlObjectProperty);
		}
	}
	
	public void startReasoner() {
		reasoner = reasonerFactory.createReasoner(ontology);
	}
	
	public void stopReasoner() {
		reasoner.dispose();
	}
	
	public void isConsistent() {
		System.out.println("Consistent Ontology: " + reasoner.isConsistent());
	}
	
	public void provideExplanations() {
		// InconsistentOntologyExplanationGeneratorFactory f = 
		//		new InconsistentOntologyExplanationGeneratorFactory(reasonerFactory, 0);
		// ExplanationGenerator<OWLAxiom> gen = f.createExplanationGenerator(ontology);
		// OWLAxiom entailment = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(), dataFactory.getOWLNothing());
		// System.out.println(entailment);
		// gen.getExplanations(entailment);
	}
	
}