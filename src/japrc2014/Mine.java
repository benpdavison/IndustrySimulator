//Exam Candidate Number Y3586958
package japrc2014;

public final class Mine extends Entity {

	public Mine(int entID, Region region) {
		super(entID, IndustrySimulation.EntityTypes.ENTITY_MINE, region);

	}

	@Override
	public void produce() {
		for (int i = 1; i <= getProductionTarget(); i++) {
			this.containingRegion
					.addResource(IndustrySimulationInterface.ResourceTypes.RESOURCE_ORE);
		}
	}
}
