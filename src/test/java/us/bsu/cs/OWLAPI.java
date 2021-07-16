package us.bsu.cs;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

// import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class OWLAPI {
	File ONT_FILE;
	IRI ONT_IRI;
	OWLOntology ontology;
	OWLOntologyManager ontManager;
	OWLDataFactory dataFactory;
	OWLReasonerFactory reasonerFactory = new PelletReasonerFactory();
	OWLReasoner reasoner;
	SWRLRuleEngine swrlRuleEngine;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
	
	public void flushAndReload() {
		try {
			ontManager.loadOntologyFromOntologyDocument(ONT_FILE);
		} catch (OWLOntologyCreationException e) {
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
	
	public ArrayList<String> getIndividuals(String owlClassS) {
		OWLClass owlClass = dataFactory.getOWLClass(IRI.create(ONT_IRI + "#" + owlClassS));
		// ArrayList<OWLIndividual> owlIndividuals = new ArrayList<OWLIndividual>();
		ArrayList<String> owlIndividualsS = new ArrayList<String>();
		// EntitySearcher.getIndividuals(owlClass, ontology).forEach(owlIndividuals::add);
		EntitySearcher.getIndividuals(owlClass, ontology).stream().map(x -> x.asOWLNamedIndividual().getIRI().getFragment()).forEach(owlIndividualsS::add);
		return owlIndividualsS;
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
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
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
	
	public ArrayList<OWLIndividual> loadObjectPropertyValue(String owlIndividualS, String owlObjectPropertyS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLObjectProperty owlObjectProperty = dataFactory.getOWLObjectProperty(IRI.create(ONT_IRI + "#" + owlObjectPropertyS));
		return loadObjectPropertyValue(owlIndividual, owlObjectProperty);
	}
	
	public void printObjectPropertyValue(String owlIndividualS, String owlObjectPropertyS) {
		for (OWLIndividual owlIndividual: loadObjectPropertyValue(owlIndividualS, owlObjectPropertyS)) {
			System.out.println(owlIndividual.asOWLNamedIndividual().getIRI().getShortForm());
		}
	}
	
	// ------------------------------ REASONER BEGIN --------------------------------------
	public void startReasoner() {
		// reasoner = reasonerFactory.createReasoner(ontology);
		reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
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
	
	public void provideExplanations() {
		// InconsistentOntologyExplanationGeneratorFactory f = 
		//		new InconsistentOntologyExplanationGeneratorFactory(reasonerFactory, 0);
		// ExplanationGenerator<OWLAxiom> gen = f.createExplanationGenerator(ontology);
		// OWLAxiom entailment = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLThing(), dataFactory.getOWLNothing());
		// System.out.println(entailment);
		// gen.getExplanations(entailment);
	}
	// ------------------------------ REASONER END --------------------------------------
	
	// ------------------------------ SWRL BEGIN --------------------------------------
	public void startSWRLRuleEngine() {
		swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
	}
	
	public void addSWRLRule(String ruleNameS, String ruleS) {
		if (! droolRunning()) {
			startSWRLRuleEngine();
		}
		try {
			SWRLAPIRule swrlRule = swrlRuleEngine.createSWRLRule(ruleNameS, ruleS);
			System.out.println(swrlRule.getRuleName() + " rule added successfully.");
		} catch (SWRLParseException e) {
			e.printStackTrace();
		} catch (SWRLBuiltInException e) {
			e.printStackTrace();
		}
	}
	
	public boolean droolRunning() {
		try {
			swrlRuleEngine.getRuleEngineName();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void startInferring() {
		if (! droolRunning()) {
			startSWRLRuleEngine();
		}
		swrlRuleEngine.infer();
	}
	
	
	// ------------------------------ SWRL END --------------------------------------
	
	// ------------------------------ UPDATE ONTOLOGY --------------------------------------
	public void addIndividual(OWLClass owlClass, OWLIndividual owlIndividual) {
		OWLClassAssertionAxiom classAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(owlClass, owlIndividual);
		ontManager.addAxiom(ontology, classAssertionAxiom);
	}
	
	public void addIndividual(OWLClass owlClass, String owlIndividualS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		addIndividual(owlClass, owlIndividual);
	}
	
	public void addIndividual(String owlClassS, String owlIndividualS) {
		OWLClass owlClass = dataFactory.getOWLClass(IRI.create(ONT_IRI + "#" + owlClassS));
		addIndividual(owlClass, owlIndividualS);
	}
	
	public void objectPropertyAssertion(OWLIndividual owlIndividual1, OWLObjectProperty owlObjectProperty, OWLIndividual owlIndividual2) {
		OWLObjectPropertyAssertionAxiom objectPropertyAssertionAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(owlObjectProperty, owlIndividual1, owlIndividual2);
		ontManager.addAxiom(ontology, objectPropertyAssertionAxiom);
	}
	
	public void objectPropertyAssertion(String owlIndividual1S, String owlObjectPropertyS, String owlIndividual2S) {
		OWLIndividual owlIndividual1 = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividual1S));
		OWLObjectProperty owlObjectProperty = dataFactory.getOWLObjectProperty(IRI.create(ONT_IRI + "#" + owlObjectPropertyS));
		OWLIndividual owlIndividual2 = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividual2S));
		objectPropertyAssertion(owlIndividual1, owlObjectProperty, owlIndividual2);
	}
	
	public void dataPropertyAssertion(OWLIndividual owlIndividual, OWLDataProperty owlDataProperty, OWLLiteral owlLiteral) {
		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(owlDataProperty, owlIndividual, owlLiteral);
		ontManager.addAxiom(ontology, dataPropertyAssertionAxiom);
	}
	
	public void dataPropertyAssertion(String owlIndividualS, String owlDataPropertyS, String owlLiteralS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralS);
		
		dataPropertyAssertion(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void dataPropertyAssertion(String owlIndividualS, String owlDataPropertyS, int owlLiteralI) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralI);
		
		dataPropertyAssertion(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void dataPropertyAssertion(String owlIndividualS, String owlDataPropertyS, boolean owlLiteralB) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralB);
		
		dataPropertyAssertion(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void dataPropertyAssertion(String owlIndividualS, String owlDataPropertyS, Calendar owlLiteralC) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(dateFormat.format(owlLiteralC.getTime()), OWL2Datatype.XSD_DATE_TIME);
		
		dataPropertyAssertion(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void removeDataProperty(OWLIndividual owlIndividual, OWLDataProperty owlDataProperty, OWLLiteral owlLiteral) {
		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(owlDataProperty, owlIndividual, owlLiteral);
		ontManager.removeAxiom(ontology, dataPropertyAssertionAxiom);
	}
	
	public void removeDataProperty(String owlIndividualS, String owlDataPropertyS, String owlLiteralS) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralS);
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		removeDataProperty(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void removeDataProperty(String owlIndividualS, String owlDataPropertyS, int owlLiteralI) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralI);
		
		removeDataProperty(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void removeDataProperty(String owlIndividualS, String owlDataPropertyS, boolean owlLiteralB) {
		OWLIndividual owlIndividual = dataFactory.getOWLNamedIndividual(IRI.create(ONT_IRI + "#" + owlIndividualS));
		OWLDataProperty owlDataProperty = dataFactory.getOWLDataProperty(IRI.create(ONT_IRI + "#" + owlDataPropertyS));
		OWLLiteral owlLiteral = dataFactory.getOWLLiteral(owlLiteralB);
		
		removeDataProperty(owlIndividual, owlDataProperty, owlLiteral);
	}
	
	public void saveOntology() {
		try {
			ontManager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
}