//Exam Candidate Number Y3586958
package japrc2014;

public final class RetailDistributor extends Entity {
	public RetailDistributor(int entID, Region region) {
		super(entID, IndustrySimulation.EntityTypes.ENTITY_DISTRIBUTOR, region);
	}

	@Override
	public void produce() {
		int target = getProductionTarget();
		int availableComputers = this.containingRegion
				.getResourceTypeCount(IndustrySimulationInterface.ResourceTypes.RESOURCE_COMPUTERS);
		while (availableComputers > 0 && target > 0) {
			this.containingRegion
					.addResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_MONEY);
			this.containingRegion
					.removeResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_COMPUTERS);
			target--;
			availableComputers--;
		}
		if (target > 0) {
			this.containingRegion
					.addResourceDemand(
							IndustrySimulationInterface.ResourceTypes.RESOURCE_COMPUTERS,
							target);
		}
	}

}
