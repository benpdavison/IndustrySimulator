//Exam Candidate Number Y3586958
package japrc2014;

public final class ChemicalPlant extends Entity {

	public ChemicalPlant(int entID, Region region) {
		super(entID, IndustrySimulation.EntityTypes.ENTITY_CHEMICAL_PLANT,
				region);
	}

	public void produce() {
		int target = getProductionTarget();
		int availableOil = this.containingRegion
				.getResourceTypeCount(IndustrySimulationInterface.ResourceTypes.RESOURCE_OIL);

		while (availableOil > 0 && target > 0) {
			this.containingRegion
					.addResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_PLASTIC);
			this.containingRegion
					.removeResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_OIL);
			target--;
			availableOil--;

		}
		// Add demand with leftover target
		if (target > 0) {
			this.containingRegion.addResourceDemand(
					IndustrySimulationInterface.ResourceTypes.RESOURCE_OIL,
					target);
		}
	}
}
