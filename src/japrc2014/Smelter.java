//Exam Candidate Number Y3586958
package japrc2014;

public final class Smelter extends Entity {

	public Smelter(int entID, Region region) {
		super(entID, IndustrySimulation.EntityTypes.ENTITY_SMELTER, region);
	}

	@Override
	public void produce() {
		int target = getProductionTarget();
		int availableOre = this.containingRegion
				.getResourceTypeCount(IndustrySimulationInterface.ResourceTypes.RESOURCE_ORE);
		while (availableOre > 0 && target > 0) {
			this.containingRegion
					.addResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_METAL);
			this.containingRegion
					.removeResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_ORE);
			target--;
			availableOre--;
		}
		// Add demand with leftover target
		if (target > 0) {
			this.containingRegion.addResourceDemand(
					IndustrySimulationInterface.ResourceTypes.RESOURCE_ORE,
					target);
		}
	}

}
