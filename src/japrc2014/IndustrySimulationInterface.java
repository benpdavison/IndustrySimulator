package japrc2014;

public interface IndustrySimulationInterface
{       
    enum EntityTypes{
        ENTITY_MINE,
        ENTITY_REFINERY,
        ENTITY_SMELTER,
        ENTITY_CHEMICAL_PLANT,
        ENTITY_FACTORY,
        ENTITY_DISTRIBUTOR,
    }
    
    enum ResourceTypes{
        RESOURCE_ORE,
        RESOURCE_OIL,
        RESOURCE_METAL,
        RESOURCE_PLASTIC,
        RESOURCE_COMPUTERS,
        RESOURCE_MONEY
    }
    
    /** Produces a string describing the state of the current simulation, such that 
     * setSimState(), when given the string, will restore the simulation to the same
     * state */
    public String getSimState();
    
    /** Replaces the current state of the simulation with a state corresponding to the 
     * string provided */
    public void setSimState(String state);
    
    /** Creates a new region in the simulation, and returns an ID number that uniquely
     *  identifies that region */
    public int createRegion(String name);
    
    /** Creates a connection between two regions (makes them adjacent) */
    public void connectRegions(int region1, int region2);
    
    /** Creates a new entity in the specified region, and returns an ID number that
     *  uniquely identifies the entity */
    public int createEntity(EntityTypes type, int region);
    
    /** Removes the entity from the simulation */
    public void removeEntity(int entity);
       
    /** Returns the number of regions in the simulation */
    public int getRegionCount();
    
    /** Returns the number of entities in the simulation */
    public int getEntityCount();
    
    /** Returns the number of entities in the simulation that are of the specified 
     * type */
    public int getEntityTypeCount(EntityTypes type); 
    
    /** Returns the total amount of resources in the simulation of the specified 
     * type */
    public int getResourceCount(ResourceTypes type);
    
    /** Returns the total amount of demand in the simulation for the specified 
     * type of resource */
    public int getDemandCount(ResourceTypes type);
    
    /** This runs the simulation for the specified number of simulated hours, using 
     * the calling thread */
    public void runBlocking(int hours);
    
    /** This runs the simulation until stopRun() is called. This method is asynchronous
     *  - control returns to the calling thread as soon as simulation starts running. */
    public void runAsynchronous(); 
    
    /** This stops the simulation when it has been running asynchronously */
    public void stopRun();    
    
    /** Registers a callback object. This object's endOfDay() method will be called at 
     * the end of every simulated day */ 
    public void registerEndOfDayCallback(SimulationUpdateCallbackInterface callback);
    
    /** Returns a matrix showing the connectivity between regions, represented as a 2D array of booleans. 
     * Both axes represent regions, with 0 on each being the region with the lowest regionID, 1 the next, 
     * and so on. (This means that the index on the axis is not necessarily the same as the regionID)
     * A True value in a cell means that the region along the first axis is connected to the region on the second axis.
     *      */
    public boolean[][] getConnectivityMatrix();
}
