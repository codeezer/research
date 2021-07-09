package us.bsu.cs;

import java.io.File;
import java.util.ArrayList;

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
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
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
	SWRLRuleEngine swrlRuleEngine;
	
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
	
	public ArrayList<OWLClass> loadClasses() {
		ArrayList<OWLClass> classes = new ArrayList<OWLClass>();
		ontology.getClassesInSignature().forEach(classes::add);
		return classes;
	}
	
	public void printClasses() {
		for (OWLClass owlClass: loadClasses()) {
			System.out.println(owlClass);
		}
	}
	
	public ArrayList<OWLIndividual> loadIndividuals() {
		ArrayList<OWLIndividual> individuals = new ArrayList<OWLIndividual>();
		ontology.getIndividualsInSignature().forEach(individuals::add);
		return individuals;
	}
	
	public void printIndividuals() {
		for (OWLIndividual owlIndividual: loadIndividuals()) {
			System.out.println(owlIndividual);
		}
	}
	
	public ArrayList<OWLDataProperty> loadDataProperties() {
		ArrayList<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		ontology.getDataPropertiesInSignature().forEach(dataProperties::add);
		return dataProperties;
	}
	
	public void printDataProperties() {
		for (OWLDataProperty owlDataProperty: loadDataProperties()) {
			System.out.println(owlDataProperty);
		}
	}
	
	public ArrayList<OWLObjectProperty> loadObjectProperties() {
		ArrayList<OWLObjectProperty> objectProperties = new ArrayList<OWLObjectProperty>();
		ontology.getObjectPropertiesInSignature().forEach(objectProperties::add);
		return objectProperties;
	}
	
	public void printObjectProperties() {
		for (OWLObjectProperty owlObjectProperty: loadObjectProperties()) {
			System.out.println(owlObjectProperty);
		}
	}
	
	public ArrayList<OWLLiteral> loadDataPropertyValue(OWLIndividual owlIndividual, OWLDataProperty owlDataProperty) {
		ArrayList<OWLLiteral> dataPropertiesLiteralValues = new ArrayList<OWLLiteral>();
		EntitySearcher.getDataPropertyValues(owlIndividual, owlDataProperty, ontology).forEach(dataPropertiesLiteralValues::add);
		return dataPropertiesLiteralValues;
	}
	
	public ArrayList<OWLLiteral> loadDataPropertyValue(String owlIndividualS, String owlDataPropertyS) {
		// OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(ONT_IRI + "#" + owlIndividualS);
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		
		// OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(ONT_IRI + "#" + owlDataPropertyS);
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		return loadDataPropertyValue(owlIndividual, owlDataProperty);
	}
	
	public void printDataPropertyValue(String owlIndividualS, String owlDataPropertyS) {
		for (OWLLiteral owlLiteralValue: loadDataPropertyValue(owlIndividualS, owlDataPropertyS)) {
			System.out.println(owlLiteralValue.getLiteral());
		}
	}
	
	public ArrayList<OWLIndividual> loadObjectPropertyValue(OWLIndividual owlIndividual, OWLObjectProperty owlObjectProperty) {
		ArrayList<OWLIndividual> objectPropertiesValues = new ArrayList<OWLIndividual>();
		EntitySearcher.getObjectPropertyValues(owlIndividual, owlObjectProperty, ontology).forEach(objectPropertiesValues::add);
		return objectPropertiesValues;
	}
	
//	public ArrayList<OWLIndividual> loadObjectPropertyValue(String owlIndividualS, String owlObjectPropertyS) {
//		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(ONT_IRI + "#" + owlIndividualS);
//		OWLObjectProperty owlObjectProperty = dataFactory.getOWLObjectProperty(ONT_IRI + "#" + owlObjectPropertyS);
//		return loadObjectPropertyValue(owlIndividual, owlObjectProperty);
//	}
	
//	public void printObjectPropertyValue(String owlIndividualS, String owlObjectPropertyS) {
//		for (OWLIndividual owlIndividual: loadObjectPropertyValue(owlIndividualS, owlObjectPropertyS)) {
//			System.out.println(owlIndividual.asOWLNamedIndividual().getIRI().getShortForm());
//		}
//	}
	
	public void startReasoner() {
		reasoner = reasonerFactory.createReasoner(ontology);
	}
	
	public void stopReasoner() {
		reasoner.dispose();
	}
	
	public boolean isConsistent() {
		return reasoner.isConsistent();
	}
	
	public void printIfConsistent() {
		System.out.println("Consistent Ontology: " + isConsistent());
	}
	
	public void addIndividual(OWLClass owlClass, OWLIndividual owlIndividual) {
		OWLClassAssertionAxiom classAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
		ontManager.addAxiom(ontology, classAssertionAxiom);
	}
	
//	public void addIndividual(OWLClass owlClass, String owlIndividualS) {
//		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(ONT_IRI + "#" + owlIndividualS);
//		addIndividual(owlClass, owlIndividual);
//	}
//	
//	public void addIndividual(String owlClassS, String owlIndividualS) {
//		OWLClass owlClass = dataFactory.getOWLClass(ONT_IRI + "#" + owlClassS);
//		addIndividual(owlClass, owlIndividualS);
//	}
	
	public void saveOntology() throws OWLOntologyStorageException {
		ontManager.saveOntology(ontology);
	}
	
	public void startSWRLRuleEngine() {
		swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
	}
	
	public void addSWRLRule(String ruleNameS, String ruleS) {
		startSWRLRuleEngine();
		try {
			SWRLAPIRule swrlRule = swrlRuleEngine.createSWRLRule(ruleNameS, ruleS);
			System.out.println(swrlRule.getRuleName() + " rule added successfully.");
		} catch (SWRLParseException e) {
			e.printStackTrace();
		} catch (SWRLBuiltInException e) {
			e.printStackTrace();
		}
	}
	
	public void startDrool() {
		swrlRuleEngine.infer();
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