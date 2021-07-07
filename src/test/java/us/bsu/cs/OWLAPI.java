package us.bsu.cs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
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
	
	public void loadClasses() {
		ArrayList<OWLClass> classes = new ArrayList<OWLClass>();
		ontology.getClassesInSignature().forEach(classes::add);
		
		for (OWLClass owlClass: classes) {
			System.out.println(owlClass);
		}
	}
	
	public void loadIndividuals() {
		ArrayList<OWLIndividual> individuals = new ArrayList<OWLIndividual>();
		ontology.getIndividualsInSignature().forEach(individuals::add);
		
		for (OWLIndividual owlIndividual: individuals) {
			System.out.println(owlIndividual);
		}
	}
	
	public void loadDataProperty() {
		ArrayList<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		ontology.getDataPropertiesInSignature().forEach(dataProperties::add);
		
		for (OWLDataProperty owlDataProperty: dataProperties) {
			System.out.println(owlDataProperty);
		}
	}
	
	public void loadObjectProperty() {
		ArrayList<OWLObjectProperty> ObjectProperties = new ArrayList<OWLObjectProperty>();
		ontology.getObjectPropertiesInSignature().forEach(ObjectProperties::add);
		
		for (OWLObjectProperty owlObjectProperty: ObjectProperties) {
			System.out.println(owlObjectProperty);
		}
	}
	
	public void loadDataPropertyValue(OWLIndividual owlIndividual, OWLDataProperty owlDataProperty) {
		EntitySearcher.getDataPropertyValues(owlIndividual, owlDataProperty, ontology).forEach(System.out::println);
	}
	
	public void loadDataPropertyValue(String owlIndividualS, String owlDataPropertyS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(ONT_IRI + "#" + owlIndividualS);
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(ONT_IRI + "#" + owlDataPropertyS);
		loadDataPropertyValue(owlIndividual, owlDataProperty);
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
	
	public void addIndividual(OWLClass owlClass, OWLIndividual owlIndividual) {
		OWLClassAssertionAxiom classAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
		ontManager.addAxiom(ontology, classAssertionAxiom);
	}
	
	public void addIndividual(OWLClass owlClass, String owlIndividualS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(ONT_IRI + "#" + owlIndividualS);
		addIndividual(owlClass, owlIndividual);
	}
	
	public void addIndividual(String owlClassS, String owlIndividualS) {
		OWLClass owlClass = dataFactory.getOWLClass(ONT_IRI + "#" + owlClassS);
		addIndividual(owlClass, owlIndividualS);
	}
	
	
	
	
	public void saveOntology() throws OWLOntologyStorageException {
		ontManager.saveOntology(ontology);
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