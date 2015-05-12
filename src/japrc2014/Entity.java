//Exam Candidate Number Y3586958
package japrc2014;

import java.util.Random;

public abstract class Entity implements Comparable<Object> {
	private static final Random rng = new Random();

	protected Region containingRegion;

	private int entityID;

	private IndustrySimulation.EntityTypes type;

	public Entity(int entID, IndustrySimulation.EntityTypes inputType,
			Region region) {
		entityID = entID;
		type = inputType;
		containingRegion = region;
	}

	@Override
	public int compareTo(Object otherEntity) {
		int compareID = ((Entity) otherEntity).getEntityID();
		return this.getEntityID() - compareID;
	}

	@Override
	public String toString() {
		String entityString = "EntityType," + type + ",ContainingRegion,"
				+ containingRegion.getRegionName() + ",EntityID," + entityID
				+ ",";
		return entityString;
	}

	public int getEntityID() {
		return entityID;
	}

	public void setEntityID(int inputID) {
		entityID = inputID;
	}

	public IndustrySimulation.EntityTypes getEntityType() {
		return type;
	}

	public int getProductionTarget() {
		return rng.nextInt(4); // upper limit is exclusive
	}

	public Region getContainingRegion() {
		return containingRegion;
	}

	public void setRegion(Region region) {
		containingRegion = region;
	}

	abstract void produce();
}
