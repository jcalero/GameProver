/**
 * 
 */
package game;

import java.util.ArrayList;

import logic.SavedProof;

/**
 * @author Jakob
 *
 */
public class SaveManager {
	
	private Game game;
	
	private static ArrayList<SavedProof> savedProofs = new ArrayList<SavedProof>();
	
	public SaveManager(Game game) {
		this.game = game;
	}
	
	public void saveLocally(SavedProof proof) {
		savedProofs.add(proof);
	}
	
//	public void addExpressionToAxiomList(Expression exp) {
//		game.getStepManager().save();
////		throwInListModel.addElement(exp);
//	}
	
	//TODO: Get rid of this! SaveManager should be the only one handling savedProofs.
	public ArrayList<SavedProof> getSavedProofs() {
		return savedProofs;
	}
}
