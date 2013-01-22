/**
 * 'Main' JFrame for program. All states,
 * buttons, boxes etc are added to this Frame
 * 
 */
package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import logic.Expression;
import logic.GameState;
import logic.LogicStep;
import logic.ProofState;
import logic.ReplayManager;
import logic.Rule;
import logic.SavedProof;
import logic.StepManager;
import parser.MyExpressionParser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.StaxDriver;

@SuppressWarnings({ "serial" })
public class DisplayFrame extends JFrame {

	private DefaultListModel listModel;
	private ProofStatePanel currentDisplayPanel;
	private ReplayManager replayManager;
	private StepManager stepManager;
	private ArrayList<SavedProof> savedProofs = new ArrayList<SavedProof>();
	public boolean wasCleanup = false;
	private boolean isReplaying = false;

	public Expression toProve;
	private ProofStatePanel done;
	private JList myList;

	private String prevTheorem = "";
	private String[] theorems;
	private ArrayList<String> userTheorems = new ArrayList<String>();

	private RWFrame rwframe;
	private Expression rwexp;
	private JButton btnUndo;
	private JButton btnRepo;
	private JButton btnReplay;
	public boolean help;
	private JLabel helpwindow;
	private JLabel helpicon;
	public JLabel rwIn;
	public JLabel throwIn;
	// 2012
	public JScrollPane scrollPanel;

	private ImageIcon throwInIcon = new ImageIcon(getClass().getResource(
			"images/throw_in.png"));
	private ImageIcon helpmark = new ImageIcon(getClass().getResource(
			"images/helpmark.png"));
	private ImageIcon footballIcon = new ImageIcon(getClass().getResource(
			"images/football.png"));
	private ImageIcon rwIcon = new ImageIcon(getClass().getResource(
			"images/rw.png"));

	private static ArrayList<GameState> gameList = new ArrayList<GameState>();

	// sounds
	// private URL win = getClass().getResource("sounds/win.wav");
	// private URL cheer = getClass().getResource("sounds/cheer.wav");
	// private URL wrong = getClass().getResource("sounds/wrong.wav");

	public DisplayFrame() {
		setTitle("GoalProver 1.0");
		setSize(new Dimension(1100, 800));
		getContentPane().setLayout(null);

		JLabel throwinicon = new JLabel();
		throwinicon = new JLabel(new ImageIcon(footballIcon .getImage()
				.getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
		JLabel throwintittle = new JLabel();
		throwintittle.setText("Throw in:");

		throwinicon.setBounds(new Rectangle(900, 105, 30, 30));
		throwintittle.setBounds(new Rectangle(940, 111, 89, 23));
		getContentPane().add(throwinicon);
		getContentPane().add(throwintittle);

		rwexp = null;
		rwframe = new RWFrame(rwexp, this);

		scrollPanel = new JScrollPane();
		scrollPanel.setBounds(new Rectangle(10, 130, 890, 600));
		getContentPane().add(scrollPanel);

		helpwindow = new JLabel();
		helpwindow.setHorizontalAlignment(SwingConstants.LEFT);
		helpwindow
				.setText("Help: move mouse over objects see allowed operation");
		helpwindow.setBounds(new Rectangle(40, 736, 780, 26));
		getContentPane().add(helpwindow);
		helpicon = new JLabel(new ImageIcon(helpmark.getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
		helpicon.setBounds(new Rectangle(10, 734, 30, 30));
		getContentPane().add(helpicon);

		JButton btnSetGoal = new JButton("New Goal");
		btnSetGoal.setBounds(90, 12, 100, 30);

		btnRepo = new JButton("Cleanup");
		btnRepo.setBounds(700, 12, 100, 30);

		btnUndo = new JButton("Undo");
		btnUndo.setBounds(700, 42, 100, 30);
		btnUndo.setEnabled(false);

		JScrollPane pnlScrollPane = new JScrollPane();

		// These are "default" theorems for throw in. Should be added in the new
		// way,
		// with SavedProof instead. --- Jakob
		theorems = new String[] { "0+1=1", "x+0=x",
				"x+(y+1)=(x+y)+1",
				"x*0=0",
				"x*(y+1)=(x*y)+x" };
		listModel = new DefaultListModel();

		for (String s : theorems) {
			Expression exp;
			try {
				exp = MyExpressionParser.parse(s);
				SavedProof sp = new SavedProof(exp);
				listModel.addElement(sp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		myList = new JList(listModel);
		pnlScrollPane.getViewport().setView(myList);
		pnlScrollPane.setBounds(900, 130, 185, 576);

		for (MouseListener m : myList.getMouseListeners()) {
			myList.removeMouseListener(m);
		}

		for (MouseMotionListener m : myList.getMouseMotionListeners()) {
			myList.removeMouseMotionListener(m);
		}

		myList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listClickHandler(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		getContentPane().add(pnlScrollPane);

		btnSetGoal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGoalHandler(null);
			}
		});

		btnRepo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cleanupHandler();
			}
		});

		btnUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undoHandler();
			}
		});

		// 20120603
		getContentPane().add(btnUndo);
		// FOR TEST getContentPane().add(btnTest);
		// 20120603
		getContentPane().add(btnSetGoal);
		// getContentPane().add(toProve);
		getContentPane().add(rwframe);
		getContentPane().add(btnRepo);
		// 201207

		throwIn = new JLabel(new ImageIcon(throwInIcon.getImage()
				.getScaledInstance(180, 100, Image.SCALE_DEFAULT)));
		throwIn.setBounds(900, 12, 180, 100);
		getContentPane().add(throwIn);
		throwIn.setVisible(false);

		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(900, 704, 89, 23);
		getContentPane().add(btnLoad);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(996, 704, 89, 23);
		getContentPane().add(btnSave);

		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadHandler();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveHandler();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		rwIn = new JLabel(new ImageIcon(rwIcon.getImage().getScaledInstance(95,
				95, Image.SCALE_DEFAULT)));
		rwIn.setBounds(600, 12, 95, 95);
		getContentPane().add(rwIn);

		btnReplay = new JButton("Replay");
		btnReplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replayHandler();
			}
		});
		btnReplay.setBounds(700, 71, 100, 30);
		getContentPane().add(btnReplay);
		btnReplay.setEnabled(false);
		rwIn.setVisible(false);

		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void saveHandler() throws IOException {
		File output = new File(getUserDataDirectory() + File.separator
				+ getSaveFileName());
		if (!output.exists()) {
			output.getParentFile().mkdirs();
			output.createNewFile();
		}
		if (toXML(savedProofs, output)) {
			System.out.println("[DATA MANAGER]: Saved " + savedProofs.size()
					+ " proofs successfully");
		}
	}

	private void loadHandler() throws FileNotFoundException {
		File input = new File(getUserDataDirectory() + File.separator
				+ getSaveFileName());
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

		userTheorems.clear();

		if (listModel.size() > theorems.length) {
			listModel.removeRange(theorems.length, listModel.size() - 1);
		}

		for (int i = 0; i < savedProofs.size(); i++) {
			userTheorems.add(savedProofs.get(i).toString());
			listModel.addElement(savedProofs.get(i));
		}

		System.out.println("[DATA MANAGER]: Loaded " + savedProofs.size()
				+ " proofs successfully");
	}

	public static String getUserDataDirectory() {
		return System.getProperty("user.home") + File.separator + ".gameprover"
				+ File.separator;
	}

	public static String getSaveFileName() {
		return "savedTest.xml";
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

	public void setStateToShow(ProofState pf) {
		this.invalidate();

		if (currentDisplayPanel != null) {
			remove(currentDisplayPanel);
		}

		ProofStatePanel pfPanel = new ProofStatePanel(this, pf);
		scrollPanel.setViewportView(pfPanel);

		int s = pfPanel.getHsize();
		pfPanel.setPreferredSize(new Dimension(scrollPanel.getWidth(), s));
		currentDisplayPanel = pfPanel;
		repaint();

		this.validate();
	}

	//[A&B]->C->D
	public void updateFrame() {
		if (currentDisplayPanel != null) {
			currentDisplayPanel.update();
			// Resize and repaint
			int s = currentDisplayPanel.getHsize();
			currentDisplayPanel.setPreferredSize(new Dimension(scrollPanel
					.getWidth() + 500, s));
			currentDisplayPanel.revalidate();
			repaint();
		}

		// Check whether proof is finished
		if (isDone()) {
			doneBehaviour();
		}
		if (gameList.size() <= 1) {
			getUndobtn().setEnabled(false);
			getUndobtn().setText("Undo");
		}
	}

	public JButton getUndobtn() {
		return this.btnUndo;
	}

	public void undoFrame() {
		System.out.println("[UNDO]: Starting undo");
		setStateToShow(currentDisplayPanel.undo());
		if (!wasCleanup)
			stepManager.undo();
		if (isReplaying() && replayManager != null) {
			replayManager.prev();
		}
		wasCleanup = false;
		System.out.println("[UNDO]: Undo finished");
	}

	public void cleanup() {
		ProofStatePanel panel = currentDisplayPanel;
		ProofState proofState = panel.logicState;
		int subStateToShowIndex = -1;
		boolean doCleanup = false;
		if (proofState.isEmptyState()) {
			wasCleanup = true;
			doCleanup = true;
			proofState.setHideFlag(true);
		}

		for (int i = 0; i < proofState.getSubstates().size(); i++) {
			ProofState subState = proofState.getSubstate(i);
			boolean oldSubStateFlag = subState.getHideFlag();
			subState.cleanup();
			if (subState.getHideFlag() && !oldSubStateFlag) {
				doCleanup = true;
			}
			if (!subState.getHideFlag() && subStateToShowIndex == -1) {
				subStateToShowIndex = i;
			}
		}

		if (doCleanup) {
			if (subStateToShowIndex == -1) {
				setStateToShow(proofState);
			} else {
				setStateToShow(proofState.getSubstate(subStateToShowIndex));
			}
		}
	}

	public ProofState getCurrentDisplayState() {
		ProofState ps = new ProofState();
		if (gameList.size() > 0) {
			ps = gameList.get(gameList.size() - 1).getDisplayState();
		}
		return ps;
	}

	public ProofStatePanel getCurrentDisplayPanel() {
		return currentDisplayPanel;
	}

	public void setHelptxt(String s) {
		helpwindow.setText(s);
	}

	public void sethelpmark(ImageIcon i) {
		ImageIcon img = new ImageIcon(i.getImage().getScaledInstance(20, 20,
				Image.SCALE_DEFAULT));
		helpicon.setIcon(img);

	}

	public void setGoalHandler(Expression newGoal) {
		setHelptxt("Help: Roll mouse over objects to see allowed operations");
		rwframe.restart();

		if (done != null && done.getParent() != null) {
			remove(done);
		}
		
		ArrayList<Expression> goals = new ArrayList<Expression>();
		String s;
		if (newGoal == null) {
			s = (String) JOptionPane.showInputDialog(this,
					"Please input formula:", "Set Goal",
					JOptionPane.PLAIN_MESSAGE, null, null, prevTheorem);

			if (s == null)
				return;

			try {
				goals.add(MyExpressionParser.parse(s));
			} catch (Exception e) {
				e.printStackTrace();
			}
			toProve = goals.get(0);
		} else {
			goals.add(newGoal);
			s = newGoal.toString();
			toProve = newGoal;
		}
		getUndobtn().setEnabled(false);
		getUndobtn().setText("Undo");

		prevTheorem = s;

		ProofState state = new ProofState();
		state.setGoals(goals);
		state.getProofStateList().clear();
		state.getProofStateList().add(state);

		rwframe.setdisptext(s);
		gameList.clear();
		setStateToShow(state);

		GameState gameState = new GameState();
		gameState.setProofStateList(state.getProofStateList());
		gameState.setDisplayStateIndex(state.getDepth());
		gameList.add(gameState);

		stepManager = new StepManager(this);
		stepManager.start(toProve);

		btnRepo.setEnabled(true);
		// btnReplay.setEnabled(true);
		updateFrame();
	}

	public void cleanupHandler() {
		cleanup();
		currentDisplayPanel.record();
		updateFrame();
	}

	public void undoHandler() {
		undoFrame();
		updateUndoButton();
	}

	public void updateUndoButton() {
		if (getGameList().size() > 1) {
			getUndobtn().setEnabled(true);
			getUndobtn().setText("Undo (" + (getGameList().size() - 1) + ")");
		} else {
			getUndobtn().setText("Undo");
			getUndobtn().setEnabled(false);
		}
	}

	private void replayHandler() {
		if (!isReplaying()) {
			return;
		} else {
			replayManager.next();
		}
	}

	public boolean isDone() {
		return currentDisplayPanel.isDone();
	}

	public boolean isReplaying() {
		return isReplaying;
	}

	public void setReplaying(boolean value) {
		btnReplay.setEnabled(value);
		isReplaying = value;
	}

	public void doneBehaviour() {
		gameList.clear();
		// play(win);
		btnRepo.setEnabled(false);
		setReplaying(false);
		remove(currentDisplayPanel);
		rwframe.setdispExp(null, null);
		rwframe.restart();
		setHelptxt("Help:");
		sethelpmark(helpmark);

		done = new DonePanel(this);
		scrollPanel.setViewportView(done);
	}

	public void addToList() {
		userTheorems.add(toProve.toString());
		stepManager.save();
		listModel.addElement(savedProofs.get(savedProofs.size() - 1));
	}

	private void listClickHandler(MouseEvent e) {
		Rectangle r = myList.getCellBounds(0, myList.getLastVisibleIndex());
		int selected = -1;

		if (r != null && r.contains(e.getPoint())) {
			selected = myList.locationToIndex(e.getPoint());
		}

		if (selected != -1) {
			SavedProof proof = (SavedProof) listModel.getElementAt(selected);
			Expression exp = proof.getExpression();

			boolean theorem = false;
			if (selected < theorems.length)
				theorem = true;
			else
				theorem = false;

			// Left mouse clicked and a proof is going on => Throw in
			if (e.getButton() == MouseEvent.BUTTON1 && getGameList().size() > 0) {
				if (exp.containsTermVars()) {
					throwIn.setVisible(true);

					// Operating on chars to preserve brackets
					char[] charArray = exp.toString().toCharArray();
					String toParse = "";
					for (int i = 0; i < charArray.length; i++) {
						if (Character.isLetter(charArray[i])) {
							toParse += "?" + charArray[i];
						} else {
							toParse += charArray[i];
						}
					}
					try {
						exp = MyExpressionParser.parse(toParse);
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out
								.println("ERROR: Failed to parse variable with ? added");
					}
					LogicStep ls = new LogicStep(Rule.AddAssumVar,
							currentDisplayPanel.getAbsoluteDepth());
					ls.setNewExpression(exp);
					stepManager.applyRule(ls);
					updateFrame();
				} else {
					myList.setSelectedIndex(selected);
					System.out.println("Left clicked on: " + exp);
					throwIn.setVisible(true);
					LogicStep ls = new LogicStep(Rule.AddAssum,
							currentDisplayPanel.getAbsoluteDepth());
					ls.setNewExpression(exp);
					stepManager.applyRule(ls);
					updateFrame();
				}
			}
			// Right mouse clicked and not clicked on a theorem => Menu
			else if (e.getButton() == MouseEvent.BUTTON3 && !theorem) {
				myList.setSelectedIndex(selected);
				System.out.println("Right clicked on: " + exp);
				Object[] options = { "Throw in", "Replay", "Prove" };
				int n = JOptionPane
						.showOptionDialog(this,
								"Choose what to do with this theorem.",
								"Loading theorem",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);
				switch (n) {
				case 0:
					if (getGameList().size() < 1) {
						myList.clearSelection();
						return;
					} else {
						throwIn.setVisible(true);
						LogicStep ls = new LogicStep(Rule.AddAssum,
								currentDisplayPanel.getAbsoluteDepth());
						ls.setNewExpression(exp);
						stepManager.applyRule(ls);
						updateFrame();
					}
					break;
				case 1:
					setGoalHandler(exp);
					replayManager = new ReplayManager(this);
					replayManager.load(proof);
					setReplaying(true);
					break;
				case 2:
					setGoalHandler(exp);
					break;
				default:
					break;
				}
			} else {
				myList.setSelectedIndex(selected);
				setHelptxt("Help: Cannot do that.");
			}
		} else {
			myList.clearSelection();
		}

	}

	public RWFrame getrwframe() {
		return this.rwframe;
	}

	public void setRWexp(Expression exp, String type) {
		rwframe.setdispExp(exp, type);
	}

	public void play(URL url) {
		try {
			// TODO: Fix sound performance issues... Find out why!

			// AudioClip ac = Applet.newAudioClip(url);
			// ac.play();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ArrayList<GameState> getGameList() {
		return gameList;
	}

	public ArrayList<SavedProof> getSavedProofs() {
		return savedProofs;
	}

}
