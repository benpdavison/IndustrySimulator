//Exam Candidate Number Y3586958
package japrc2014;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class IndustrySimulation implements IndustrySimulationInterface {
	private Integer nextRegionID = 1;
	private int simHourCount = 0;
	private int simDayCount = 0;
	private boolean simulationRunning = false;
	private SimulationUpdateCallbackInterface guiObj;

	// Entities
	private Integer nextEntityID = 1;
	private ArrayList<Region> regionList = new ArrayList<Region>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private ArrayList<ArrayList<Boolean>> connectionArray = new ArrayList<ArrayList<Boolean>>();

	private boolean[][] connectivityMatrix;

	public IndustrySimulation() {
		super();
	}

	@Override
	public String getSimState() {

		String simValues = String
				.format("SavedSimStateFile,NextRegionID,%d,SimHourCount,%d,SimDayCount,%d,nextEntityID,%d,Regions,",
						nextRegionID, simHourCount, simDayCount, nextEntityID);
		StringBuilder simState = new StringBuilder(simValues);
		Collections.sort(regionList);
		for (Region r : regionList) {
			simState.append(r);
		}
		simState.append("Entities,");
		Collections.sort(entityList);
		for (Entity e : entityList) {
			simState.append(e);
		}
		simState.append("ConnectivityMatrix,"
				+ Arrays.deepToString(getConnectivityMatrix()));
		return simState.toString();
	}

	@Override
	public void setSimState(String state) {
		try {
			String[] tokens = state.split(",");
			if (tokens[0].equals("SavedSimStateFile") && !(state.equals(""))) {
				// Clear Arrays
				regionList.clear();
				entityList.clear();
				connectionArray.clear();
				// Reset Variables
				simulationRunning = false;
				nextRegionID = 1;
				simHourCount = 0;
				simDayCount = 0;
				nextEntityID = 1;

				int i = 10;
				while (tokens[i].equals("RegionName")) {
					Region region;
					i++;
					createRegion(tokens[i]);
					i++;
					if (!(tokens[i].equals("RegionID"))) {
						throw new IndustrySimulationException(
								"Error: Format of file does not match Sim State File Format (RegionID)");
					}
					i++;
					try {
						region = regionList.get(regionList.size() - 1);
						region.setRegionID(Integer.parseInt(tokens[i]));
					} catch (IndexOutOfBoundsException e) {
						throw new IndustrySimulationException(
								"No regions are in the regions list");
					}
					i++;
					if (!(tokens[i].equals("ENTITY_MINE_COUNT"))) {
						throw new IndustrySimulationException(
								"Error: Format of file does not match Sim State File Format (Entity_Counts)");
					}
					i += 12;
					if (!(tokens[i].equals("Resource Amounts"))) {
						throw new IndustrySimulationException(
								"Error: Format of file does not match Sim State File Format (Resource Amounts)");
					}
					i++;
					for (int j = 0; j < 6; j++) {
						StringBuilder resourceTypeString = new StringBuilder(
								tokens[i]);
						String resourceType = resourceTypeString.substring(0,
								resourceTypeString.length() - 7);
						i++;
						if (Integer.parseInt(tokens[i]) == 0) {
							i++;
							continue;
						}
						for (int k = 0; k < Integer.parseInt(tokens[i]); k++) {
							region.addResource(ResourceTypes
									.valueOf(resourceType));
						}
						i++;
					}
					if (!(tokens[i].equals("Resource Demands"))) {
						throw new IndustrySimulationException(
								"Error: Format of file does not match Sim State File Format (Resource Demands)");
					}
					i++;
					for (int j = 0; j < 6; j++) {
						StringBuilder demandTypeString = new StringBuilder(
								tokens[i]);
						String demandType = demandTypeString.substring(0,
								demandTypeString.length() - 7);
						i++;
						if (Integer.parseInt(tokens[i]) == 0) {
							i++;
							continue;
						}
						region.addResourceDemand(ResourceTypes
									.valueOf(demandType), Integer.parseInt(tokens[i]));
						
						i++;
					}
				}
				if (!(tokens[i].equals("Entities"))
						|| !(tokens[i + 1].equals("EntityType"))) {
					throw new IndustrySimulationException(
							"Error: Format of file does not match Sim State File Format (Entity Format)");
				}
				i++;
				while (tokens[i].equals("EntityType")) {
					int regionID = 0;
					i++;
					EntityTypes entityType = EntityTypes.valueOf(tokens[i]);
					i += 2;
					for (Region region : regionList) {
						if (region.getRegionName().equals(tokens[i])) {
							regionID = region.getRegionID();
						}

					}
					createEntity(entityType, regionID);
					i += 2;
					entityList.get(entityList.size() - 1).setEntityID(
							Integer.parseInt(tokens[i]));
					i++;
				}
				if (!(tokens[i].equals("ConnectivityMatrix"))) {
					throw new IndustrySimulationException(
							"Error: Format of file does not match Sim State File Format (Connectivity Matrix)");
				}
				i++;

				for (int j = 0; j < connectionArray.size(); j++) {
					for (int k = 0; k < connectionArray.get(j).size(); k++) {
						if (tokens[i].contains("false")) {
							connectionArray.get(j).set(k, false);
						} else {
							connectionArray.get(j).set(k, true);
						}
						i++;
					}
				}
				//Set Global Variables
				nextRegionID = Integer.parseInt(tokens[2]);
				simHourCount = Integer.parseInt(tokens[4]);
				simDayCount = Integer.parseInt(tokens[6]);
				nextEntityID = Integer.parseInt(tokens[8]);
			} else {
				throw new IndustrySimulationException(
						"Error reading Sim State File.");
			}
		} catch (Exception e) {
			throw new IndustrySimulationException("" + e);
		}

	}

	@Override
	public int createRegion(String name) {
		for (Region region : regionList) {
			if (region.getRegionName().toUpperCase().equals(name.toUpperCase())) {
				throw new IndustrySimulationException("Region with this name ("
						+ name + ") already exists");
			}
		}

		regionList.add(new Region(name, nextRegionID));
		// add new connectivitymatrix values for each new added region
		connectionArray.add(new ArrayList<Boolean>());
		// fill the inner arraylist so that it matches regionsize
		for (ArrayList<Boolean> arrayList : connectionArray) {
			while (arrayList.size() < regionList.size()) {
				arrayList.add(false);
			}
		}
		return nextRegionID++; // note the postincrement
	}

	@Override
	public void connectRegions(int region1, int region2) {
		if (region1 == region2) {
			throw new IndustrySimulationException(
					"To connect Regions they must be different");
		}
		try {
			// change the input values to that they match the index they are
			// store din the connectionmatrix
			connectionArray.get((region1 - 1)).set((region2 - 1), true);
			connectionArray.get((region2 - 1)).set((region1 - 1), true);
		} catch (IndexOutOfBoundsException e) {
			throw new IndustrySimulationException(
					"The region ID you entered was not found in the array. Region ID = Index + 1. "
							+ e.getMessage());
		}
	}

	@Override
	public int createEntity(IndustrySimulationInterface.EntityTypes type,
			int region) {
		Entity e = null;
		for (Region r : regionList) {
			if (r.getRegionID() == region) {

				switch (type) {
				case ENTITY_MINE:
					e = new Mine(nextEntityID, r);
					break;
				case ENTITY_REFINERY:
					e = new Refinery(nextEntityID, r);
					break;
				case ENTITY_SMELTER:
					e = new Smelter(nextEntityID, r);
					break;
				case ENTITY_CHEMICAL_PLANT:
					e = new ChemicalPlant(nextEntityID, r);
					break;
				case ENTITY_FACTORY:
					e = new ElectronicsFactory(nextEntityID, r);
					break;
				case ENTITY_DISTRIBUTOR:
					e = new RetailDistributor(nextEntityID, r);
					break;

				default:
					throw new IndustrySimulationException(
							"Unrecognised Entity Type");
				}

				r.addEntity(type);
				entityList.add(e);
				return nextEntityID++;
			}
		}
		throw new IndustrySimulationException("The region ID you entered ("
				+ region + ") was not found in the array.");
	}

	@Override
	public void removeEntity(int inputID) {
		for (Entity entity : entityList) {
			if (entity.getEntityID() == inputID) {
				Region containerRegion = entity.getContainingRegion();
				containerRegion.removeEntity(entity.getEntityType());
				entityList.remove(entityList.indexOf(entity));
				return;
			}
		}
		throw new IndustrySimulationException("No entity with the ID ("
				+ inputID + ") was found");
	}

	@Override
	public int getRegionCount() {
		return regionList.size();
	}

	@Override
	public int getEntityCount() {
		return entityList.size();
	}

	@Override
	public int getEntityTypeCount(EntityTypes type) {
		int count = 0;
		for (Region r : regionList) {
			count += r.getEntityTypeCount(type);
		}
		return count;
	}

	@Override
	public int getResourceCount(ResourceTypes type) {
		int count = 0;
		for (Region r : regionList) {
			count += r.getResourceTypeCount(type);
		}
		return count;
	}

	@Override
	public int getDemandCount(ResourceTypes type) {

		int count = 0;
		for (Region r : regionList) {
			count += r.getResourceDemandCount(type);
		}
		return count;

	}

	@Override
	public void runBlocking(int hours) {
		if (hours <= 0) {
			throw new IndustrySimulationException(
					"Hour amount must be a positive integer > 0");
		}
		if (simulationRunning) {
			throw new IndustrySimulationException(
					"Simulation is running, cannot Tick");
		}
		simulationRunning = true;
		Collections.shuffle(entityList);
		for (int h = 1; h <= hours; h++) {
			for (Entity e : entityList) {
				if (e instanceof Mine) {
					Mine m = (Mine) e;
					m.produce();
				} else if (e instanceof Refinery) {
					Refinery r = (Refinery) e;
					r.produce();
				} else if (e instanceof Smelter) {
					Smelter s = (Smelter) e;
					s.produce();
				} else if (e instanceof ChemicalPlant) {
					ChemicalPlant cP = (ChemicalPlant) e;
					cP.produce();
				} else if (e instanceof ElectronicsFactory) {
					ElectronicsFactory eF = (ElectronicsFactory) e;
					eF.produce();
				} else if (e instanceof RetailDistributor) {
					RetailDistributor rD = (RetailDistributor) e;
					rD.produce();
				}
			}
			updateTime();
		}
		simulationRunning = false;
	}

	@Override
	public void runAsynchronous() {
		if (simulationRunning) {
			throw new IndustrySimulationException(
					"Simulation is already Running");
		}
		simulationRunning = true;
		Thread rAs = new Thread() {
			@Override
			public void run() {
				while (simulationRunning) {
					Collections.shuffle(entityList);
					for (Entity e : entityList) {
						if (e instanceof Mine) {
							Mine m = (Mine) e;
							m.produce();
						} else if (e instanceof Refinery) {
							Refinery r = (Refinery) e;
							r.produce();
						} else if (e instanceof Smelter) {
							Smelter s = (Smelter) e;
							s.produce();
						} else if (e instanceof ChemicalPlant) {
							ChemicalPlant cP = (ChemicalPlant) e;
							cP.produce();
						} else if (e instanceof ElectronicsFactory) {
							ElectronicsFactory eF = (ElectronicsFactory) e;
							eF.produce();
						} else if (e instanceof RetailDistributor) {
							RetailDistributor rD = (RetailDistributor) e;
							rD.produce();
						}
					}
					updateTime();
				}
			}
		};
		rAs.start();
	}

	@Override
	public void stopRun() {
		simulationRunning = false;
	}

	@Override
	public void registerEndOfDayCallback(SimulationUpdateCallbackInterface c) {
		guiObj = c;
	}

	@Override
	public boolean[][] getConnectivityMatrix() {
		connectivityMatrix = new boolean[connectionArray.size()][];
		int i = 0;
		for (ArrayList<Boolean> booleanArray : connectionArray) {
			connectivityMatrix[i] = new boolean[booleanArray.size()];
			int j = 0;
			for (Boolean value : booleanArray) {
				connectivityMatrix[i][j] = value;
				j++;
			}
			i++;
		}

		return connectivityMatrix;
	}

	private void updateTime() {
		simHourCount++;
		while (simHourCount >= 24) {
			simHourCount -= 24;
			simDayCount++;
			endOfDay();
		}

	}

	private void endOfDay() {
		ArrayList<Region> connectedRegionList = new ArrayList<Region>();
		// Fill the connectedRegion array list with the true and false
		// values of the connectionArray list, it does this incrementally
		// by "column".
		Collections.shuffle(regionList);
		for (Region region : regionList) {
			connectedRegionList.clear();
			// If the Arraylist "regionConnections" contains a true value,
			// add the region to the connectedRegion Arraylist.
			// region is obtained based on index of value.
			int i = region.getRegionID() - 1;
			for (int j = 0; j < connectionArray.get(i).size(); j++) {
				if (connectionArray.get(i).get(j) == true) {
					connectedRegionList.add(recursiveBinarySearchRegionByID(
							insertionSortRegionArrayListByID(), 0,
							regionList.size(), j + 1));
				}
			}
			// Then for each resource type and each connected region,
			// while there is demand, and enough resource to fill that
			// demand, move the resources. after which, the remaining demand
			// is divided between the connected regions.
			for (ResourceTypes resource : ResourceTypes.values()) {
				// Check to see if there is a demand
				if ((region.getResourceDemandCount(resource)) > 0
						&& (connectedRegionList.size() != 0)) {
					// If there is demand, check if each connected region has
					// the resource.
					for (Region connectedRegion : connectedRegionList) {
						while ((connectedRegion.getResourceTypeCount(resource) > 0)
								&& (region.getResourceDemandCount(resource) > 0)) {
							// while there is still a demand and there is
							// resources available to fill that demand
							// move the resource to the country with the demand
							connectedRegion.removeResource(resource);
							region.removeResourceDemand(resource);
							region.addResource(resource);
						}
					}
					// After checking all regions and all resources have been
					// moved, get the remaining dema
					int remainingDemandOfResource = region
							.getResourceDemandCount(resource);
					int resourceDemandPerRegion = remainingDemandOfResource
							/ connectedRegionList.size();
					for (Region connectedRegion : connectedRegionList) {
						connectedRegion.addResourceDemand(resource,
								resourceDemandPerRegion);
					}
					// After distributing the demand to all resources, remove
					// resourceDemand from original region
					while (region.getResourceDemandCount(resource) != 0) {
						region.removeResourceDemand(resource);
					}
				}
			}
		}
		if (guiObj != null) {
			// call the end of day function in the GUI.
			guiObj.endOfDay(this);
		}
	}

	private ArrayList<Region> insertionSortRegionArrayListByID() {
		// This is a sorting method that was adapted
		// from a web page detailing sorting methods
		// in java. The original source web page was
		// accessed on 17-12-2014 and can be found here:
		// http://www.java2novice.com/java-sorting-algorithms/insertion-sort/

		ArrayList<Region> sortedList = new ArrayList<Region>();
		for (Region region : regionList) {
			sortedList.add(region);
		}

		for (int i = 1; i < sortedList.size(); i++) {
			for (int j = i; j > 0; j--) {
				if (sortedList.get(j).getRegionID() < sortedList.get(j - 1)
						.getRegionID()) {
					Region tempRegionHolder = sortedList.get(j);
					sortedList.set(j, sortedList.get(j - 1));
					sortedList.set(j - 1, tempRegionHolder);
				}
			}
		}
		return sortedList;
	}

	private Region recursiveBinarySearchRegionByID(
			ArrayList<Region> sortedRegionArrayList, int lowRange,
			int highRange, int IDKey) {
		Region foundRegion = null;
		if (lowRange < highRange) {
			int midRange = lowRange + (highRange - lowRange) / 2;
			if (IDKey < sortedRegionArrayList.get(midRange).getRegionID()) {
				return recursiveBinarySearchRegionByID(sortedRegionArrayList,
						lowRange, midRange, IDKey);
			} else if (IDKey > sortedRegionArrayList.get(midRange)
					.getRegionID()) {
				return recursiveBinarySearchRegionByID(sortedRegionArrayList,
						midRange, highRange, IDKey);
			} else {
				return sortedRegionArrayList.get(midRange);
			}
		}
		return foundRegion;
	}

}
