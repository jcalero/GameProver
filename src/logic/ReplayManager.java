package logic;

import gui.DisplayFrame;

public class ReplayManager {
	private SavedProof proof;
	private StepManager sm;
	private DisplayFrame df;
	private int step = 0;
	
	public ReplayManager(DisplayFrame df) {
		this.df = df;
		this.sm = new StepManager(df);
	}
	
	public void load(SavedProof proof) {
		this.proof = proof;
	}
	
	public void next() {
		LogicStep ls = proof.get(step);
		sm.applyRule(ls);
		df.updateFrame();
		step++;
		if (step == proof.size()) {
			df.setReplaying(false);
		}
	}
	
	public void prev() {
		if (step - 1 < 0) {
			step = 0;
		} else {
			step--;
		}
	}
}
