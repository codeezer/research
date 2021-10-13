import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.EngineStub;
import com.uppaal.engine.Problem;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.PrototypeDocument;
import com.uppaal.model.system.UppaalSystem;
import com.uppaal.model.system.Process;

public class testAPI {
	public static Document loadModel(String location) throws IOException
    {
		try {
			// try URL scheme (useful to fetch from Internet):
			return new PrototypeDocument().load(new URL(location));
		} catch (MalformedURLException ex) {
			// not URL, retry as it were a local filepath:
			return new PrototypeDocument().load(new URL("file", null, location));
		}
    }

    public static Engine connectToEngine() throws EngineException, IOException
    {
		String os = System.getProperty("os.name");
		String here = System.getProperty("user.dir");
		String path = null;
		if ("Linux".equals(os)) {
			path = here+"/bin-Linux/server";
		} else {
			path = here+"\\bin-Windows\\server.exe";
		}
		Engine engine = new Engine();
		engine.setServerPath(path);
		engine.setServerHost("localhost");
		engine.setConnectionMode(EngineStub.BOTH);
		engine.connect();
		return engine;
    }

    public static UppaalSystem compile(Engine engine, Document doc)
		throws EngineException, IOException
    {
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
			Document doc = null;
			// load model from a file/internet:
			doc = loadModel(args[0]);

			// connect to the engine server:
			Engine engine = connectToEngine();

            // create a link to a local Uppaal process:
			UppaalSystem system = compile(engine, doc);

			for (String variable : system.getVariables()) {
				System.out.println(variable);
			}


			engine.disconnect();
        } catch (EngineException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
