/**
 * 
 */
package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import logic.SavedProof;

/**
 * @author Jakob
 *
 */
public class StartModel {
	
	private SaveManager saveManager;
	private DefaultListModel proofListModel;
	/**
	 * 
	 */
	public StartModel(SaveManager saveManager) {
		this.saveManager = saveManager;
	}
	
	public void loadDataSetFromFile(File file) {
		try {
			saveManager.loadFromFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getBaseAxiomList() {
		return saveManager.getAxioms();
	}
	
	public DefaultListModel updateProofListModel() {
		ArrayList<SavedProof> userAxioms = saveManager.getSavedProofs();
		if ( userAxioms.size() < 1 ) {
			return null;
		}
		proofListModel = new DefaultListModel();
		for (SavedProof e : userAxioms) {
			proofListModel.addElement(e);
		}
		return proofListModel;
	}

}
