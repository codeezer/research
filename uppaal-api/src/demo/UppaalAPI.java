package demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.uppaal.engine.CannotEvaluateException;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.EngineStub;
import com.uppaal.engine.Problem;
import com.uppaal.engine.QueryFeedback;
import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.PrototypeDocument;
import com.uppaal.model.core2.Query;
import com.uppaal.model.system.SystemEdge;
import com.uppaal.model.system.SystemLocation;
import com.uppaal.model.system.UppaalSystem;
import com.uppaal.model.system.concrete.ConcreteTrace;
import com.uppaal.model.system.symbolic.SymbolicState;
import com.uppaal.model.system.symbolic.SymbolicTrace;
import com.uppaal.model.system.symbolic.SymbolicTransition;

public class UppaalAPI {
	Document doc = null;
	Engine engine = null;
	UppaalSystem system = null;
	SymbolicState state = null;
	
	public UppaalAPI(String modelFile) throws IOException, EngineException, CannotEvaluateException {
		doc = loadModel(modelFile);
		engine = connectToEngine();
		system = compile(engine, doc);
		state = engine.getInitialState(system);
	}
	
	
	public final String options = "order 0\n"
			+ "reduction 1\n"
			+ "representation 0\n"
			+ "trace 0\n"
			+ "extrapolation 0\n"
			+ "hashsize 27\n"
			+ "reuse 1\n"
			+ "smcparametric 1\n"
			+ "modest 0\n"
			+ "statistical 0.01 0.01 0.05 0.05 0.05 0.9 1.1 0.0 0.0 1280.0 0.01";

	
	public QueryFeedback qf = new QueryFeedback() { 
		@Override
		public void setProgressAvail(boolean availability) {
		}

		@Override
		public void setProgress(int load, long vm, long rss, long cached, long avail, long swap, long swapfree, long user, long sys, long timestamp) {
		}

		@Override
		public void setSystemInfo(long vmsize, long physsize, long swapsize){
		}

		@Override
		public void setLength(int length) {
		}

		@Override
		public void setCurrent(int pos) {
		}

		@Override
		public void setTrace(char result, String feedback,
								SymbolicTrace trace, QueryResult queryVerificationResult) {
		}

		public void setTrace(char result, String feedback,
								ConcreteTrace trace, QueryResult queryVerificationResult) {
		}
		@Override
		public void setFeedback(String feedback) {
//					if (feedback != null && feedback.length() > 0) {
//						System.out.println(feedback);
//					}
		}

		@Override
		public void appendText(String s) {
//					if (s != null && s.length() > 0) {
//						System.out.println(s);
//					}
		}

		@Override
		public void setResultText(String s) {
//					if (s != null && s.length() > 0) {
//						System.out.println(s);
//					}
		}
	};


	public int getIndexOfVariable(String v) {
		return system.getVariables().indexOf(v);
	}
	
	public void updateVariableValue(String v, int newValue) {
		state.getVariableValues()[getIndexOfVariable(v)] = newValue;
	}
	
	public int getVariableValue(String v) {
		return state.getVariableValues()[getIndexOfVariable(v)];
	}
	
	public String getConstraints() {
		String res = "";
		List<String> constraints = new ArrayList<String>();
        state.getPolyhedron().getAllConstraints(constraints);
        for (String cs : constraints) {
            res += cs + ", ";
        }
        return res;
	}
	
	
	public void printCurrentState() {
		System.out.print("(");
		for (SystemLocation l: state.getLocations()) {
            System.out.print(l.getName()+", ");
        }
        int val[] = state.getVariableValues();
        for (int i=0; i<system.getNoOfVariables(); i++) {
            System.out.print(system.getVariableName(i)+"="+val[i]+", ");
        }
        List<String> constraints = new ArrayList<String>();
        state.getPolyhedron().getAllConstraints(constraints);
        for (String cs : constraints) {
            System.out.print(cs+", ");
        }
        System.out.println(")");
	}
	
	
	public SymbolicTrace symbolicSimulation(int noOfIterations) throws EngineException, IOException, CannotEvaluateException {
		SymbolicTrace trace = new SymbolicTrace();
		
		trace.add(new SymbolicTransition(null, null, state));
		
		for (int i = 0; i < noOfIterations; i++) {
			int varValues[] = state.getVariableValues();
			
			// compute the successors (including "deadlock"):
			ArrayList<SymbolicTransition> trans = engine.getTransitions(system, state);
			
			// select a random transition:
			int n = (int) Math.floor(Math.random() * trans.size());
			
			SymbolicTransition tr = trans.get(n);
			
			// check the number of edges involved:
			if (tr.getSize() == 0) {
				// no edges, something special (like "deadlock"):
				System.out.println(tr.getEdgeDescription());
			} else {
				for (SystemEdge e: tr.getEdges()) {
					String processName = e.getProcessName();
					Object fromState = e.getEdge().getSource().getPropertyValue("name");
					Object toState = e.getEdge().getTarget().getPropertyValue("name");
					
					if (fromState.toString() != "" || toState.toString() != "") {
						System.out.println(processName + ": " + fromState + " \u2192 " + toState + ", ");
					}
				}
			}
			// jump to a successor state (null in case of deadlock):
			state = tr.getTarget();
			
			// if successful, add the transition to the trace:
			if (state != null) trace.add(tr);
		}
		return trace;
	}
	

	public SymbolicTrace symbolicSimulation() throws EngineException, IOException, CannotEvaluateException {
		return symbolicSimulation(100);
	}
	
	public QueryResult performQuery(String query, String queryDescription) throws EngineException {
		Query query1 = new Query(query, queryDescription);
		return engine.query(system, options, query1, qf);
	}
	
	public char getQueryStatus(String query, String queryDescription) throws EngineException {
		return performQuery(query, queryDescription).getResult();
	}
	
	
	public Document loadModel(String location) throws IOException {
		try {
			// try URL scheme (useful to fetch from Internet):
			return new PrototypeDocument().load(new URL(location));
		} catch (MalformedURLException ex) {
			// not URL, retry as it were a local file path:
			return new PrototypeDocument().load(new URL("file", null, location));
		}
    }
	
	
	public UppaalSystem compile(Engine engine, Document doc)
			throws EngineException, IOException {
			// compile the model into system:
			ArrayList<Problem> problems = new ArrayList<Problem>();
			UppaalSystem sys = engine.getSystem(doc, problems);
			if (!problems.isEmpty()) {
				boolean fatal = false;
				System.out.println("There are problems with the document:");
				for (Problem p : problems) {
					System.out.println(p.toString());
					if (!"warning".equals(p.getType())) { // ignore warnings
						fatal = true;
					}
				}
				if (fatal) {
					System.exit(1);
				}
			}
			return sys;
	    }
	
	
	public Engine connectToEngine() throws EngineException, IOException {
		String os = System.getProperty("os.name");
		String here = System.getProperty("user.dir");
		String path = null;
		if ("Linux".equals(os)) {
			path = here+"/ubin/bin-Linux/server";
		} else if ("Mac OS X".equals(os)) {
			path = here + "/ubin/bin-Darwin/server";
		} else {
			path = here+"\\ubin\\bin-Windows\\server.exe";
		}
		Engine engine = new Engine();
		engine.setServerPath(path);
		engine.setServerHost("localhost");
		engine.setConnectionMode(EngineStub.BOTH);
		engine.connect();
		return engine;
    }
	
	public void endConnection() {
		engine.disconnect();
	}	
}

