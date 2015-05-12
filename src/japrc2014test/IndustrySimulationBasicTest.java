package japrc2014test;


import japrc2014.IndustrySimulationException;
import japrc2014.IndustrySimulationInterface;
import japrc2014.IndustrySimulation;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class IndustrySimulationBasicTest extends TestCase
{
    IndustrySimulationInterface sim;

    

    @Before
    public void setUp() throws Exception
    {
        sim = new IndustrySimulation();
    }
    
    @Test
    public void testEmptySim() {
        assertEquals( sim.getEntityCount(), 0 );
        assertEquals( sim.getRegionCount(), 0 );
    }
    
    @Test
    public void testAddRegion() {
        sim.createRegion("region 1");
        assertEquals( 1, sim.getRegionCount() );
    }

    @Test
    public void testAddEntity() {
        int regionID = sim.createRegion("region 1");
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_MINE, regionID);
        assertEquals( 1, sim.getEntityCount() );
    }
    
    @Test
    public void testEntityTypes() {
        int regionID = sim.createRegion("region 1");
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_MINE, regionID);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_DISTRIBUTOR, regionID);
        assertEquals( 1, sim.getEntityTypeCount(IndustrySimulationInterface.EntityTypes.ENTITY_MINE) );        
        assertEquals( 1, sim.getEntityTypeCount(IndustrySimulationInterface.EntityTypes.ENTITY_DISTRIBUTOR) );        
    }
    
    //Level 1
    
   
    @Test 
    public void testConnectRegions(){
    	int region1 = sim.createRegion("Region 1");
    	int region2 = sim.createRegion("Region 2");
    	sim.connectRegions(region1, region2);
    	
    }
    @Test
    public void testConnectRegionsDuplicateID(){
    	try{
    		sim.connectRegions(1, 1);
    	}
    	catch (Exception e){
    		assertTrue(e instanceof IndustrySimulationException);
    	}
    }
    
    @Test
    public void testConnectRegionsIndexOutOfBounds(){
    	try{
    		sim.connectRegions(1, 10);
    	}
    	catch (Exception e){
    		assertTrue(e instanceof IndustrySimulationException);
    	}
    }
    
    @Test
    public void tetsCreateRegionDuplicateName(){
    	try{
        	sim.createRegion("Region 1");
        	sim.createRegion("Region 1");
    	}
    	catch (Exception e){
    		assertTrue(e instanceof IndustrySimulationException);
    	}
    }
    @Test
    public void runEmptySim(){
    	try{
    	sim.runBlocking(26);
    	}catch (Exception e){
    		fail("Exception was thrown" + e);
    	}
    }
    
    public void createBasicSim(){
        int rID1 = sim.createRegion("region 1");
        int rID2 = sim.createRegion("region 2");
        int rID3 = sim.createRegion("region 3");
        int rID4 = sim.createRegion("region 4");
        int rID5 = sim.createRegion("region 5");
        int rID6 = sim.createRegion("region 6");

        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_MINE, rID1);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_MINE, rID1);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_REFINERY, rID2);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_REFINERY, rID2);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_SMELTER, rID3);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_SMELTER, rID3);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_CHEMICAL_PLANT, rID4);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_CHEMICAL_PLANT, rID4);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_FACTORY, rID5);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_FACTORY, rID5);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_DISTRIBUTOR, rID6);
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_DISTRIBUTOR, rID6);
    }
    
    @Test
    public void testRunBlockingNegativeHours(){
    	try{
    		sim.runBlocking(-1);
    	}catch (Exception e){
    		assertTrue(e instanceof IndustrySimulationException);
    	}
    }
    
    @Test
    public void test_MineProduction_ResourceOil(){
    	createBasicSim();
    	sim.runBlocking(23);
    	assert(sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_ORE)>0 && sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_ORE)<=138);
    }
    public void test_RefineryProduction_ResourceOil(){
    	createBasicSim();
    	sim.runBlocking(23);
    	assert(sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_OIL)>0 && sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_OIL)<=138);
    }
    
    public void test_FactoryDemand_ResourceMetal(){
    	//Test that factories are producing demand when they do not have necessary resources for production
    	createBasicSim();
    	sim.runBlocking(10);
    	assert(sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL)>0 && sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_PLASTIC)>=0);
    }
    
    public void test_endOfDayMovement_resourceDemand(){
    	//Tests that the demand after the end of day movement of resources is higher.
    	createBasicSim();
    	sim.connectRegions(3,4);
    	sim.connectRegions(3,5);
    	sim.runBlocking(23);
    	int beforeMovementDemand = sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL);
    	sim.runBlocking(1);
    	assert(beforeMovementDemand<=sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL));
    }
    
    public void test_ChemicalPlantProduction_ResourcePlastic(){
    	createBasicSim();
    	sim.connectRegions(2, 4);
    	sim.runBlocking(24);
    	int availableResourceOil = sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_OIL);
    	sim.runBlocking(23);
    	int plasticCount = sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_PLASTIC);
    	assert(plasticCount<=availableResourceOil && plasticCount!=0);
    	
    }

}
