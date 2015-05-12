//Exam Candidate Number Y3586958
package japrc2014;

public final class ElectronicsFactory extends Entity {

	public ElectronicsFactory(int entID, Region region) {
		super(entID, IndustrySimulation.EntityTypes.ENTITY_FACTORY, region);
	}

	public void produce() {
		int target = getProductionTarget();
		int availableMetal = this.containingRegion
				.getResourceTypeCount(IndustrySimulationInterface.ResourceTypes.RESOURCE_METAL);
		int availablePlastic = this.containingRegion
				.getResourceTypeCount(IndustrySimulationInterface.ResourceTypes.RESOURCE_PLASTIC);
		while (availableMetal > 0 && availablePlastic > 0 && target > 0) {
			this.containingRegion
					.addResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_COMPUTERS);
			this.containingRegion
					.removeResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_METAL);
			this.containingRegion
					.removeResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_PLASTIC);
			target--;
			availableMetal--;
			availablePlastic--;
		}
		if (target > 0 ){
			if (availableMetal < target){
				int difference = target - availableMetal;
				this.containingRegion.addResourceDemand(
						IndustrySimulationInterface.ResourceTypes.RESOURCE_METAL,
						difference);
			}
			if (availablePlastic < target){
				int difference = target - availablePlastic;
				this.containingRegion.addResourceDemand(
						IndustrySimulationInterface.ResourceTypes.RESOURCE_PLASTIC,
						difference);
			}
		}
	}
}
