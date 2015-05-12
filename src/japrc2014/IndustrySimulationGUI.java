//Exam Candidate Number Y3586958
package japrc2014;

import japrc2014.IndustrySimulationInterface.EntityTypes;
import japrc2014.IndustrySimulationInterface.ResourceTypes;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class IndustrySimulationGUI extends JFrame implements
		SimulationUpdateCallbackInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel controlPanel;
	private JPanel overviewPanel;
	private JPanel entityPanel;
	private JPanel resourcePanel;
	private JPanel resDemandPanel;

	private JButton startButton;
	private JButton pauseButton;
	private JButton tickButton;
	private JButton addRegionButton;
	private JButton connectRegionsButton;
	private JButton addEntityButton;
	private JButton removeEntityButton;
	private JButton resetButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton exitButton;

	private JLabel dayCountLabel;
	private JLabel yearCountLabel;
	private JLabel regionCountLabel;
	// Entities
	private JLabel mineCountLabel;
	private JLabel refiCountLabel;
	private JLabel smelCountLabel;
	private JLabel chemCountLabel;
	private JLabel factCountLabel;
	private JLabel distCountLabel;
	// Resources
	private JLabel oreCountLabel;
	private JLabel oilCountLabel;
	private JLabel metCountLabel;
	private JLabel plaCountLabel;
	private JLabel compCountLabel;
	private JLabel monCountLabel;

	private JLabel oreDemandLabel;
	private JLabel oilDemandLabel;
	private JLabel metDemandLabel;
	private JLabel plaDemandLabel;
	private JLabel compDemandLabel;
	private JLabel monDemandLabel;

	private int hourCount = 0;
	private int dayCount = 0;
	private int overallDayCount = 0;
	private int yearCount = 0;

	private IndustrySimulationInterface sim;

	public IndustrySimulationGUI(IndustrySimulation simToUse) {
		super("ATM Simulation GUI");
		setLayout(new GridBagLayout());
		GridBagConstraints conc = new GridBagConstraints();

		// Set look and feel to that of OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new IndustrySimulationException(
					"Couldn't do that, using default look and feel");
		}

		this.sim = simToUse;

		// Simulation Controls Panel

		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(0, 3, 5, 5));
		controlPanel.setBorder(BorderFactory
				.createTitledBorder("Control Panel"));
		conc.gridx = 0;
		conc.gridy = 0;
		conc.gridwidth = 2;
		conc.fill = GridBagConstraints.BOTH;
		add(controlPanel, conc);

		startButton = new JButton("Start");
		startButton.setToolTipText("Run the simulation asynchronously");
		controlPanel.add(startButton);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startButton.setEnabled(false);
				tickButton.setEnabled(false);
				resetButton.setEnabled(false);
				pauseButton.setEnabled(true);
				sim.runAsynchronous();

			}
		});

		pauseButton = new JButton("Pause");
		pauseButton
				.setToolTipText("Pause the simulation running asynchronously");
		controlPanel.add(pauseButton);
		pauseButton.setEnabled(false);
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startButton.setEnabled(true);
				tickButton.setEnabled(true);
				resetButton.setEnabled(true);
				pauseButton.setEnabled(false);
				sim.stopRun();
			}
		});

		tickButton = new JButton("Tick");
		tickButton.setToolTipText("Run the simulation for one simulated hour");
		tickButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startButton.setEnabled(false);
				tickButton.setEnabled(false);
				resetButton.setEnabled(false);
				sim.runBlocking(1);
				updateDisplay();
				resetButton.setEnabled(true);
				startButton.setEnabled(true);
				tickButton.setEnabled(true);
			}
		});
		controlPanel.add(tickButton);

		addRegionButton = new JButton("Add Region");
		addRegionButton.setToolTipText("Add a region to the simulation");
		addRegionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addNewRegion();
				updateDisplay();
			}
		});
		controlPanel.add(addRegionButton);
		connectRegionsButton = new JButton("Connect Regions");
		connectRegionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				connectRegions();
				updateDisplay();
			}
		});
		connectRegionsButton
				.setToolTipText("Connect two regions within the simulation");
		controlPanel.add(connectRegionsButton);
		addEntityButton = new JButton("Add Entity");
		addEntityButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addEntity();
				updateDisplay();
			}
		});
		addEntityButton.setToolTipText("Add an entity to the simulation");
		controlPanel.add(addEntityButton);
		removeEntityButton = new JButton("Remove Entity");
		removeEntityButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeEntity();
				updateDisplay();
			}
		});
		removeEntityButton
				.setToolTipText("Remove an entity from the simulation");
		controlPanel.add(removeEntityButton);

		resetButton = new JButton("Reset");
		resetButton.setToolTipText("Reset all elements in the simulation");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					BufferedReader reader = new BufferedReader(new FileReader(
							System.getProperty("user.dir")
									+ "/DefaultSimState.txt"));
					String simSateString = reader.readLine();
					sim.setSimState(simSateString);
					reader.close();
				} catch (IOException e) {
					throw new IndustrySimulationException(
							"Error Opening file: " + e);
				}
				hourCount = 0;
				dayCount = 0;
				overallDayCount = 0;
				yearCount = 0;
				updateDisplay();
			}
		});
		controlPanel.add(resetButton);

		saveButton = new JButton("Save");
		saveButton.setToolTipText("Save the current simulation state");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveSimState();
			}
		});
		controlPanel.add(saveButton);

		loadButton = new JButton("Load");
		loadButton.setToolTipText("Load a saved simulation state");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSimState();

			}
		});
		controlPanel.add(loadButton);

		exitButton = new JButton("Exit Program");
		exitButton.setToolTipText("Exit the program");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		controlPanel.add(exitButton);

		// Simulation Overview Panel
		conc.gridwidth = 3;
		overviewPanel = new JPanel();
		overviewPanel.setLayout(new GridBagLayout());
		overviewPanel.setBorder(BorderFactory
				.createTitledBorder("Simulation Overview"));
		conc.gridy = 2;
		add(overviewPanel, conc);

		conc.gridx = 0;
		conc.gridy = 0;
		conc.gridwidth = 1;
		conc.gridheight = 1;
		dayCountLabel = new JLabel("Day: 0 (0)");
		overviewPanel.add(dayCountLabel, conc);
		conc.gridx = 1;
		yearCountLabel = new JLabel("Year: 0");
		overviewPanel.add(yearCountLabel, conc);
		conc.gridx = 2;
		regionCountLabel = new JLabel("Regions: 0");
		overviewPanel.add(regionCountLabel, conc);

		conc.gridx = 0;
		conc.gridy = 1;
		conc.gridheight = 6;
		conc.gridwidth = 1;
		entityPanel = new JPanel();
		entityPanel.setLayout(new GridLayout(0, 1));
		entityPanel.setBorder(BorderFactory.createTitledBorder("Entities"));
		overviewPanel.add(entityPanel, conc);

		mineCountLabel = new JLabel("Mines: 0");
		entityPanel.add(mineCountLabel);
		refiCountLabel = new JLabel("Refineries: 0");
		entityPanel.add(refiCountLabel);
		smelCountLabel = new JLabel("Smelters: 0");
		entityPanel.add(smelCountLabel);
		chemCountLabel = new JLabel("Chemical Plants: 0");
		entityPanel.add(chemCountLabel);
		factCountLabel = new JLabel("Factories: 0");
		entityPanel.add(factCountLabel);
		distCountLabel = new JLabel("Distributors: 0");
		entityPanel.add(distCountLabel);

		conc.gridx = 1;
		conc.gridy = 1;
		resourcePanel = new JPanel();
		resourcePanel.setLayout(new GridLayout(0, 1));
		resourcePanel.setBorder(BorderFactory.createTitledBorder("Resources"));
		overviewPanel.add(resourcePanel, conc);

		oreCountLabel = new JLabel("Ore: 0");
		resourcePanel.add(oreCountLabel);
		oilCountLabel = new JLabel("Oil: 0");
		resourcePanel.add(oilCountLabel);
		metCountLabel = new JLabel("Metal: 0");
		resourcePanel.add(metCountLabel);
		plaCountLabel = new JLabel("Plastic: 0");
		resourcePanel.add(plaCountLabel);
		compCountLabel = new JLabel("Computers: 0");
		resourcePanel.add(compCountLabel);
		monCountLabel = new JLabel("Money: 0");
		resourcePanel.add(monCountLabel);

		conc.gridx = 2;
		conc.gridy = 1;
		conc.gridwidth = 2;

		resDemandPanel = new JPanel();
		resDemandPanel.setLayout(new GridBagLayout());
		resDemandPanel.setBorder(BorderFactory
				.createTitledBorder("Resource Demand"));
		overviewPanel.add(resDemandPanel, conc);

		conc.gridwidth = 2;
		conc.gridheight = 1;
		conc.gridx = 0;
		conc.ipadx = 35;
		conc.gridy = GridBagConstraints.RELATIVE;
		oreDemandLabel = new JLabel("Ore: 0");
		resDemandPanel.add(oreDemandLabel, conc);
		oilDemandLabel = new JLabel("Oil: 0");
		resDemandPanel.add(oilDemandLabel, conc);
		metDemandLabel = new JLabel("Metal: 0");
		resDemandPanel.add(metDemandLabel, conc);
		plaDemandLabel = new JLabel("Plastic: 0");
		resDemandPanel.add(plaDemandLabel, conc);
		compDemandLabel = new JLabel("Computers: 0");
		resDemandPanel.add(compDemandLabel, conc);
		monDemandLabel = new JLabel("Money: 0");
		resDemandPanel.add(monDemandLabel, conc);

		if (sim != null) {
			sim.registerEndOfDayCallback(this);
			updateDisplay();
		}

	}

	@Override
	public void endOfDay(IndustrySimulationInterface sim) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateTime(24);
				updateDisplay();
			}
		});
	}

	public void updateTime(int hourAmount) {
		hourCount += hourAmount;
		while (hourCount >= 24) {
			hourCount -= 24;
			dayCount++;
			overallDayCount++;
		}
		while (dayCount >= 365) {
			dayCount -= 365;
			yearCount++;
		}

	}

	public void updateDisplay() {
		if (sim == null) {
			// nothing to update from, so do nothing
		} else {
			addEntityButton.setEnabled(sim.getRegionCount() > 0 ? true : false);
			connectRegionsButton.setEnabled(sim.getRegionCount() >= 2 ? true
					: false);
			removeEntityButton.setEnabled(sim.getEntityCount() >= 1 ? true
					: false);

			// Overview Display
			dayCountLabel.setText("Day: " + dayCount + " (" + overallDayCount
					+ ")");
			yearCountLabel.setText("Year: " + yearCount);
			regionCountLabel.setText("Regions: " + sim.getRegionCount());
			mineCountLabel.setText("Mines: "
					+ sim.getEntityTypeCount(EntityTypes.ENTITY_MINE));
			refiCountLabel.setText("Refineries: "
					+ sim.getEntityTypeCount(EntityTypes.ENTITY_REFINERY));
			smelCountLabel.setText("Smelters: "
					+ sim.getEntityTypeCount(EntityTypes.ENTITY_SMELTER));
			chemCountLabel
					.setText("Chemical Plants: "
							+ sim.getEntityTypeCount(EntityTypes.ENTITY_CHEMICAL_PLANT));
			factCountLabel.setText("Electronics Factories: "
					+ sim.getEntityTypeCount(EntityTypes.ENTITY_FACTORY));
			distCountLabel.setText("Retail Distibutors: "
					+ sim.getEntityTypeCount(EntityTypes.ENTITY_DISTRIBUTOR));
			oreCountLabel.setText("Ore: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_ORE));
			oilCountLabel.setText("Oil: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_OIL));
			metCountLabel.setText("Metal: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_METAL));
			plaCountLabel.setText("Plastic: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_PLASTIC));
			compCountLabel.setText("Computers: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_COMPUTERS));
			monCountLabel.setText("Money: "
					+ sim.getResourceCount(ResourceTypes.RESOURCE_MONEY));
			oreDemandLabel.setText("Ore: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_ORE));
			oilDemandLabel.setText("Oil: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_OIL));
			metDemandLabel.setText("Metal: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_METAL));
			plaDemandLabel.setText("Plastic: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_PLASTIC));
			compDemandLabel.setText("Computers: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_COMPUTERS));
			monDemandLabel.setText("Money: "
					+ sim.getDemandCount(ResourceTypes.RESOURCE_MONEY));
		}

	}

	public void addNewRegion() {
		try {
			int regionCount = sim.getRegionCount();
			String newRegionName = JOptionPane.showInputDialog(this,
					"Please enter a new Region Name");
			if (newRegionName != null && (newRegionName.length() > 0)) {
				sim.createRegion(newRegionName);
			}
			String resultInformation = (regionCount < sim.getRegionCount()) ? "Region "
					+ newRegionName + " Added Sucessfully"
					: " No Region Was Added";
			JOptionPane.showMessageDialog(this, resultInformation);
		} catch (IndustrySimulationException e) {
			JOptionPane.showMessageDialog(this, e, "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void connectRegions() {
		boolean createConnection = true;
		ArrayList<String> regionNameList = new ArrayList<String>();
		ArrayList<Integer> regionIDList = new ArrayList<Integer>();
		String[] guiTokens = sim.getSimState().split(",");
		int i = 10;
		while (guiTokens[i].equals("RegionName")) {
			i++;
			regionNameList.add(guiTokens[i]);
			i += 2;
			regionIDList.add(Integer.parseInt(guiTokens[i]));
			i += 39;
		}
		while (createConnection) {
			JComboBox<Object> regionBox1 = new JComboBox<Object>(
					regionNameList.toArray());
			JComboBox<Object> regionBox2 = new JComboBox<Object>(
					regionNameList.toArray());
			JPanel connectPanel = new JPanel(new GridLayout(0, 1, 5, 5));
			connectPanel
					.add(new JLabel("Please Select Two Regions to Connect:"));
			connectPanel.add(new JLabel("Region 1:"));
			connectPanel.add(regionBox1);
			connectPanel.add(new JLabel("Region 2:"));
			connectPanel.add(regionBox2);
			int result = JOptionPane.showConfirmDialog(this, connectPanel,
					"Connect Two Regions", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				try {
					sim.connectRegions(
							regionIDList.get(regionBox1.getSelectedIndex()),
							regionIDList.get(regionBox2.getSelectedIndex()));
					JOptionPane.showMessageDialog(
							this,
							"Region "
									+ regionNameList.get(regionBox1
											.getSelectedIndex())
									+ " is now connected to "
									+ regionNameList.get(regionBox2
											.getSelectedIndex()),
							"Connection Successful",
							JOptionPane.INFORMATION_MESSAGE);
					createConnection = false;
				} catch (IndustrySimulationException e) {
					int retry = JOptionPane.showConfirmDialog(this, e
							+ "\nWould you like to retry?",
							"Error Connecting Regions. Retry?",
							JOptionPane.YES_NO_OPTION);
					createConnection = (retry == JOptionPane.YES_OPTION) ? true
							: false;
				}
			} else {
				break;
			}
		}
	}

	public void addEntity() {
		ArrayList<String> regionNameList = new ArrayList<String>();
		ArrayList<Integer> regionIDList = new ArrayList<Integer>();
		String[] entities = { "Mine", "Refinery", "Smelter", "Chemical Plant",
				"Electronics Factory", "Retail Distributor" };
		ArrayList<EntityTypes> entityTypes = new ArrayList<EntityTypes>();
		for (EntityTypes ent : EntityTypes.values()) {
			entityTypes.add(ent);
		}
		String[] guiTokens = sim.getSimState().split(",");
		int i = 10;
		while (guiTokens[i].equals("RegionName")) {
			i++;
			regionNameList.add(guiTokens[i]);
			i += 2;
			regionIDList.add(Integer.parseInt(guiTokens[i]));
			i += 39;
		}
		JComboBox<Object> entityBox = new JComboBox<Object>(entities);
		JComboBox<Object> regionBox = new JComboBox<Object>(
				regionNameList.toArray());
		JPanel connectPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		connectPanel.add(new JLabel(
				"Please Select an Entity to Add to a Region:"));
		connectPanel.add(new JLabel("Entity:"));
		connectPanel.add(entityBox);
		connectPanel.add(new JLabel("Region:"));
		connectPanel.add(regionBox);
		int result = JOptionPane.showConfirmDialog(this, connectPanel,
				"Add an Entity", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			int entityAmount = sim.getEntityCount();
			sim.createEntity(entityTypes.get(entityBox.getSelectedIndex()),
					regionIDList.get(regionBox.getSelectedIndex()));
			if (entityAmount < sim.getEntityCount()) {
				JOptionPane.showMessageDialog(
						this,
						entityBox.getSelectedItem()
								+ " has been added to "
								+ regionNameList.get(regionBox
										.getSelectedIndex()),
						"Add Entity Successful",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void removeEntity() {
		boolean search = true;
		ArrayList<String> regionNames = new ArrayList<String>();
		String[] entities = { "Mine", "Refinery", "Smelter", "Chemical Plant",
				"Electronics Factory", "Retail Distributor" };
		String[] guiTokens = sim.getSimState().split(",");
		int i = 10;
		while (guiTokens[i].equals("RegionName")) {
			i++;
			regionNames.add(guiTokens[i]);
			i += 41;
		}

		JComboBox<Object> regionsBox = new JComboBox<Object>(
				regionNames.toArray());
		JComboBox<Object> entitiesBox = new JComboBox<Object>(entities);
		JPanel removeEntPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		removeEntPanel.add(new JLabel(
				"Please select a Region to remove an entity from:"));
		removeEntPanel.add(regionsBox);
		removeEntPanel
				.add(new JLabel("Please select Type of entity to remove:"));
		removeEntPanel.add(entitiesBox);
		int result = JOptionPane.showConfirmDialog(this, removeEntPanel,
				"Remove an Entity", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			try {
				while (search) {

					StringBuilder entityType = new StringBuilder(entitiesBox
							.getSelectedItem().toString().toLowerCase());
					String token1 = guiTokens[i].toLowerCase();
					String key1 = entityType.substring(entityType.length() - 4,
							entityType.length());
					String token2 = guiTokens[i + 2];
					String key2 = regionsBox.getSelectedItem().toString();
					;
					if ((token1.contains(key1)) && (token2.contains(key2))) {
						int countEnt = sim.getEntityCount();
						sim.removeEntity(Integer.parseInt(guiTokens[i + 4]));
						if (sim.getEntityCount() < countEnt) {
							JOptionPane.showMessageDialog(this,
									entitiesBox.getSelectedItem()
											+ " has been removed from "
											+ regionsBox.getSelectedItem(),
									"Remove Entity Successful",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(this,
									"The entity was not removed.",
									"Error Removing Entity",
									JOptionPane.ERROR_MESSAGE);
						}
						search = false;
					} else {
						i++;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane
						.showMessageDialog(
								this,
								"The selected region does not contain that entity type.",
								"Error Removing Entity",
								JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void saveSimState() {
		File selectedFile = null;
		JFileChooser saveAsChooser = new JFileChooser(
				System.getProperty("user.dir"));
		saveAsChooser.setDialogTitle("Save Sim State As...");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sim State .txt File", "txt");
		saveAsChooser.setFileFilter(filter);
		saveAsChooser.setAcceptAllFileFilterUsed(false);

		int result = saveAsChooser.showDialog(this, "Save Sim State File");
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = saveAsChooser.getSelectedFile();
			String fileName = selectedFile.getAbsolutePath();
			if (!fileName.endsWith(".txt")) {
				selectedFile = new File(fileName + ".txt");
			}
		} else {
			return;
		}

		try {
			BufferedWriter simStateWriter = new BufferedWriter(new FileWriter(
					selectedFile));
			simStateWriter.write(sim.getSimState());
			simStateWriter.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not Save Simulation",
					"Simulation Save Error", JOptionPane.ERROR_MESSAGE);
			throw new IndustrySimulationException("Could not Save Simulation");
		}

	}

	public void openSimState() {
		try {
			// Choose File
			JFileChooser simStateChooser = new JFileChooser(
					System.getProperty("user.dir"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Sim State .txt File", "txt");
			simStateChooser.setFileFilter(filter);
			simStateChooser.setAcceptAllFileFilterUsed(false);
			int result = simStateChooser.showDialog(controlPanel,
					"Load Sim State File");
			if (result == JFileChooser.CANCEL_OPTION) {
				return;
			}
			simStateChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			File simStateFile = simStateChooser.getSelectedFile();
			// Read File
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						simStateFile));
				String simStateString = "";
				while (reader.ready() && (reader.readLine() != null)) {
					simStateString = reader.readLine();
				}
				sim.setSimState(simStateString);
				String[] guiTokens = simStateString.split(",");
				overallDayCount = Integer.parseInt(guiTokens[6]);
				dayCount = Integer.parseInt(guiTokens[6]) % 365;
				yearCount = Integer.parseInt(guiTokens[6]) / 365;
				reader.close();
			} catch (IOException e) {
				Toolkit.getDefaultToolkit().beep();
				setCursor(null);
				JOptionPane.showMessageDialog(this, "Error Opening file: "
						+ simStateFile, "Simulation Open Error",
						JOptionPane.ERROR_MESSAGE);
				throw new IndustrySimulationException("Error Opening file: "
						+ simStateFile + "\n" + e);

			}
			updateDisplay();
			Toolkit.getDefaultToolkit().beep();
			setCursor(null);
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			setCursor(null);
			JOptionPane.showMessageDialog(this, "" + e,
					"Open Sim State File Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	public static void main(String[] args) {
		IndustrySimulation sim = new IndustrySimulation();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					System.getProperty("user.dir") + "/DefaultSimState.txt"));
			String simSateString = reader.readLine();
			sim.setSimState(simSateString);
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "" + e,
					"Default Sim State File Error", JOptionPane.ERROR_MESSAGE);
		}

		IndustrySimulationGUI gui = new IndustrySimulationGUI(sim);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(840, 680);

		gui.setVisible(true);

	}

}
