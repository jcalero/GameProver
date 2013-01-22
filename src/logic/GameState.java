package logic;

import java.util.ArrayList;

public class GameState {
	private ArrayList<ProofState> proofStateList = new ArrayList<ProofState>();
	private int displayStateIndex = 0;
	
	public void setProofStateList(ArrayList<ProofState> proofStateList) {
		ProofState topState = (ProofState) proofStateList.get(0).cloneps();
		ArrayList<ProofState> proofStateListClone = new ArrayList<ProofState>();
		ProofState lastPS = topState;
		for (int i = 0; i < proofStateList.size(); i++) {
			ProofState ps;
			if (i > 0) {
				ps = lastPS.getSubstate(0);
				lastPS = ps;
			} else {
				ps = topState;
			}
			proofStateListClone.add(ps);
		}
		this.proofStateList = proofStateListClone;
	}
	
	public ArrayList<ProofState> getProofStateList() { 
		return this.proofStateList;
	}
	
	public ProofState getDisplayState() {
		return this.proofStateList.get(displayStateIndex);
	}
	
	public void setDisplayStateIndex(int index) {
		this.displayStateIndex = index;
	}
	
	public int getDisplayStateIndex() {
		return displayStateIndex;
	}
}
