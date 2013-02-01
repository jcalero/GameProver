/**
 * 
 */
package game;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import logic.SavedProof;

/**
 * @author Jakob
 *
 */
public class SaveManager {
	
	private String[] axioms = new String[] { "(0+1)=1", "(x+0)=x",
			"(x+(y+1))=((x+y)+1)", "(x*0)=0", "(x*(y+1))=((x*y)+x)" };
	private ArrayList<SavedProof> savedProofs = new ArrayList<SavedProof>();
	private String defaultFileName = "defaultSave.xml";

	
	public SaveManager() {
		
	}
	
	public void saveLocally(SavedProof proof) {
		savedProofs.add(proof);
	}
	
	//TODO: Get rid of this! SaveManager should be the only one handling savedProofs.
	public ArrayList<SavedProof> getSavedProofs() {
		return savedProofs;
	}
	
	public String[] getAxioms() {
		return axioms;
	}
	
	public void loadDefaultFile() throws FileNotFoundException {
		File input = new File(getUserDataDirectory() + File.separator
				+ getDefaultFileName());
		loadFromFile(input);
	}
	
	public void saveDefaultFile() throws IOException {
		File output = new File(getUserDataDirectory() + File.separator
				+ getDefaultFileName());
		saveToFile(output);
	}
	
	public void saveToFile(File output) throws IOException {
		if (!output.exists()) {
			output.getParentFile().mkdirs();
			output.createNewFile();
		}
		if (toXML(savedProofs, output)) {
			System.out.println("[DATA MANAGER]: Saved " + savedProofs.size()
					+ " proofs successfully");
		}
	}

	public void loadFromFile(File input) throws FileNotFoundException {
		if (!input.exists()) {
			return;
		}

		try {
			@SuppressWarnings("unchecked")
			ArrayList<SavedProof> loadData = (ArrayList<SavedProof>) fromXML(input);
			savedProofs = loadData;
		} catch (ClassCastException e) {
			System.out
					.println("[DATA MANAGER]: Could not load data. Save file not recognised.");
			e.printStackTrace();
			return;
		} catch (XStreamException xe) {
			System.out
					.println("[DATA MANAGER]: Could not load data. Save file not recognised.");
			xe.printStackTrace();
			return;
		}

		// TODO: Add a sanity check here and possibly version check for the
		// load data.

//		userTheorems.clear();
//
//		if (listModel.size() > theorems.length) {
//			listModel.removeRange(theorems.length, listModel.size() - 1);
//		}
//
//		for (int i = 0; i < savedProofs.size(); i++) {
//			userTheorems.add(savedProofs.get(i).toString());
//			listModel.addElement(savedProofs.get(i));
//		}

		System.out.println("[DATA MANAGER]: Loaded " + savedProofs.size()
				+ " proofs successfully");
	}

	public String getUserDataDirectory() {
		return System.getProperty("user.home") + File.separator + ".gameprover"
				+ File.separator;
	}

	public String getDefaultFileName() {
		return defaultFileName;
	}

	public static boolean toXML(Object object, File file) {
		XStream xStream = new XStream(new StaxDriver());
		OutputStream outputStream = null;
		Writer writer = null;

		try {
			outputStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(outputStream,
					Charset.forName("UTF-8"));
			xStream.toXML(object, writer);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close(writer);
			close(outputStream);
		}

		return true;
	}

	public static Object fromXML(File file) {
		XStream xStream = new XStream(new StaxDriver());
		return xStream.fromXML(file);
	}
	
	private static boolean close(Object object) {
		try {
			((Closeable) object).close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
