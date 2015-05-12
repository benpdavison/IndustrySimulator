//Exam Candidate Number Y3586958
package japrc2014;

import japrc2014.IndustrySimulationInterface.EntityTypes;
import japrc2014.IndustrySimulationInterface.ResourceTypes;

final class Region implements Comparable<Object> {
	private String regionName;
	private int regionID;
	// Region Entities
	private int mines = 0;
	private int refineries = 0;
	private int smelters = 0;
	private int plants = 0;
	private int factories = 0;
	private int distributors = 0;
	// Region Resources
	private int oreAmount = 0;
	private int oilAmount = 0;
	private int metalAmount = 0;
	private int plasticAmount = 0;
	private int computerAmount = 0;
	private int moneyAmount = 0;
	// Region Demand Resources
	private int oreDemand = 0;
	private int oilDemand = 0;
	private int metalDemand = 0;
	private int plasticDemand = 0;
	private int computerDemand = 0;
	private int moneyDemand = 0;

	public Region(String inputName, int inputID) {
		regionID = inputID;
		regionName = inputName;
	}

	@Override
	public int compareTo(Object otherRegion) {
		int compareID = ((Region) otherRegion).getRegionID();
		return this.getRegionID() - compareID;
	}

	@Override
	public String toString() {
		StringBuilder regionString = new StringBuilder();
		regionString.append(String.format("RegionName,%s,RegionID,%d,",
				regionName, regionID));
		for (EntityTypes entity : EntityTypes.values()) {
			regionString.append(entity + "_COUNT,");
			regionString.append(getEntityTypeCount(entity) + ",");
		}
		regionString.append("Resource Amounts,");
		for (ResourceTypes resource : ResourceTypes.values()) {
			regionString.append(resource + "_AMOUNT,");
			regionString.append(getResourceTypeCount(resource) + ",");
		}
		regionString.append("Resource Demands,");
		for (ResourceTypes resourceDemand : ResourceTypes.values()) {
			regionString.append(resourceDemand + "_DEMAND,");
			regionString.append(getResourceDemandCount(resourceDemand) + ",");
		}
		return regionString.toString();
	}

	public void addEntity(IndustrySimulationInterface.EntityTypes type) {
		switch (type) {
		case ENTITY_MINE:
			mines++;
			break;
		case ENTITY_REFINERY:
			refineries++;
			break;
		case ENTITY_SMELTER:
			smelters++;
			break;
		case ENTITY_CHEMICAL_PLANT:
			plants++;
			break;
		case ENTITY_FACTORY:
			factories++;
			break;
		case ENTITY_DISTRIBUTOR:
			distributors++;
			break;

		default:
			throw new IndustrySimulationException("unrecognised entity type");
		}
	}

	public void addResource(IndustrySimulationInterface.ResourceTypes type) {
		switch (type) {
		case RESOURCE_ORE:
			oreAmount++;
			break;
		case RESOURCE_OIL:
			oilAmount++;
			break;
		case RESOURCE_METAL:
			metalAmount++;
			break;
		case RESOURCE_PLASTIC:
			plasticAmount++;
			break;
		case RESOURCE_COMPUTERS:
			computerAmount++;
			break;
		case RESOURCE_MONEY:
			moneyAmount++;
			break;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

	public void addResourceDemand(
			IndustrySimulationInterface.ResourceTypes type, int amount) {
		switch (type) {
		case RESOURCE_ORE:
			oreDemand += amount;
			break;
		case RESOURCE_OIL:
			oilDemand += amount;
			break;
		case RESOURCE_METAL:
			metalDemand += amount;
			break;
		case RESOURCE_PLASTIC:
			plasticDemand += amount;
			break;
		case RESOURCE_COMPUTERS:
			computerDemand += amount;
			break;
		case RESOURCE_MONEY:
			moneyDemand += amount;
			break;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

	public void removeResource(IndustrySimulationInterface.ResourceTypes type) {
		switch (type) {
		case RESOURCE_ORE:
			oreAmount--;
			break;
		case RESOURCE_OIL:
			oilAmount--;
			break;
		case RESOURCE_METAL:
			metalAmount--;
			break;
		case RESOURCE_PLASTIC:
			plasticAmount--;
			break;
		case RESOURCE_COMPUTERS:
			computerAmount--;
			break;
		case RESOURCE_MONEY:
			moneyAmount--;
			break;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

	public void removeResourceDemand(
			IndustrySimulationInterface.ResourceTypes type) {
		switch (type) {
		case RESOURCE_ORE:
			oreDemand--;
			break;
		case RESOURCE_OIL:
			oilDemand--;
			break;
		case RESOURCE_METAL:
			metalDemand--;
			break;
		case RESOURCE_PLASTIC:
			plasticDemand--;
			break;
		case RESOURCE_COMPUTERS:
			computerDemand--;
			break;
		case RESOURCE_MONEY:
			moneyDemand--;
			break;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

	public void removeEntity(IndustrySimulationInterface.EntityTypes type) {
		switch (type) {
		case ENTITY_MINE:
			mines--;
			break;
		case ENTITY_REFINERY:
			refineries--;
			break;
		case ENTITY_SMELTER:
			smelters--;
			break;
		case ENTITY_CHEMICAL_PLANT:
			plants--;
			break;
		case ENTITY_FACTORY:
			factories--;
			break;
		case ENTITY_DISTRIBUTOR:
			distributors--;
			break;

		default:
			throw new IndustrySimulationException("unrecognised entity type");
		}
	}

	public String getRegionName() {
		return regionName;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionName(String inputName) {
		regionName = inputName;
	}

	public void setRegionID(int inputID) {
		regionID = inputID;
	}

	public int getEntityTypeCount(IndustrySimulationInterface.EntityTypes type) {
		switch (type) {
		case ENTITY_MINE:
			return mines;
		case ENTITY_REFINERY:
			return refineries;
		case ENTITY_SMELTER:
			return smelters;
		case ENTITY_CHEMICAL_PLANT:
			return plants;
		case ENTITY_FACTORY:
			return factories;
		case ENTITY_DISTRIBUTOR:
			return distributors;

		default:
			throw new IndustrySimulationException("unrecognised entity type");
		}
	}

	public int getResourceTypeCount(
			IndustrySimulationInterface.ResourceTypes type) {
		switch (type) {
		case RESOURCE_ORE:
			return oreAmount;
		case RESOURCE_OIL:
			return oilAmount;
		case RESOURCE_METAL:
			return metalAmount;
		case RESOURCE_PLASTIC:
			return plasticAmount;
		case RESOURCE_COMPUTERS:
			return computerAmount;
		case RESOURCE_MONEY:
			return moneyAmount;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

	public int getResourceDemandCount(
			IndustrySimulationInterface.ResourceTypes type) {
		switch (type) {
		case RESOURCE_ORE:
			return oreDemand;
		case RESOURCE_OIL:
			return oilDemand;
		case RESOURCE_METAL:
			return metalDemand;
		case RESOURCE_PLASTIC:
			return plasticDemand;
		case RESOURCE_COMPUTERS:
			return computerDemand;
		case RESOURCE_MONEY:
			return moneyDemand;

		default:
			throw new IndustrySimulationException("unrecognised resource type");
		}
	}

}
