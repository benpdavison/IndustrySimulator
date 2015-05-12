//Exam Candidate Number Y3586958
package japrc2014test;

import static org.junit.Assert.*;
import japrc2014.IndustrySimulation;
import japrc2014.IndustrySimulationException;
import japrc2014.IndustrySimulationInterface;

import org.junit.Before;
import org.junit.Test;

public class IndustrySimulationLevel1Test {

	IndustrySimulationInterface sim;
	int rID1 = 0;
	
	
    @Before
    public void setUp() throws Exception
    {
        sim = new IndustrySimulation();
        rID1 = sim.createRegion("region 1");
    }
    
    @Test
    public void testFactoryDemand(){
    	//Test that factories are producing demand when they do not have necessary resources for production
    	boolean metalDemandGreaterThanZero = false;
    	boolean plasticDemandGreaterThanZero = false;
    	boolean plasticDemandandMetalDemandEqual = false;
        sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_FACTORY, rID1);
    	sim.runBlocking(10);
    	if (sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL)>0){
    		metalDemandGreaterThanZero = true;
    	}
    	if (sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_PLASTIC)>0){
    		plasticDemandGreaterThanZero = true;
    	}
    	if (sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_PLASTIC)==sim.getDemandCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL)){
    		plasticDemandandMetalDemandEqual = true;
    	}
    	assertTrue(metalDemandGreaterThanZero);
    	assertTrue(plasticDemandGreaterThanZero);
    	assertTrue(plasticDemandandMetalDemandEqual);
    }
    
    @Test
    public void testConsumptionOfResources(){
	   	boolean metalProducedLessThanOreProduced = false;
	   	boolean oreProducedGreaterThanOreRemaining = false;
	   	boolean metalProducedGreaterThanZero = false;
	   	int expectedMetalProduction = 0;
	   	//Create a Mine and runBlocking so that it creates sine Ore
    	int eID1 = sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_MINE, rID1);
    	sim.runBlocking(10);
    	//Get amount of ore that was produced
    	int oreProduced = sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_ORE);
    	//Remove Mine and Add Smelter to consume ore and produce Metal
    	sim.removeEntity(eID1);
    	sim.createEntity(IndustrySimulationInterface.EntityTypes.ENTITY_SMELTER, rID1);
    	sim.runBlocking(30);
    	//Get amounts of metal and ore remaining
    	int metalProduced = sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_METAL);
    	int oreRemaining = sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_ORE);
    	
    	if (metalProduced<=oreProduced){
    		metalProducedLessThanOreProduced = true;
    	}
    	if (metalProduced>0){
    		metalProducedGreaterThanZero = true;
    	}
    	if (metalProducedGreaterThanZero){
    		expectedMetalProduction = oreProduced - oreRemaining;
    	}
    	if (oreProduced>oreRemaining){
    		oreProducedGreaterThanOreRemaining = true;
    	}
    	assertTrue(metalProducedLessThanOreProduced);
    	assertTrue(oreProducedGreaterThanOreRemaining);
    	assertTrue(metalProducedGreaterThanZero);
    	assertEquals(expectedMetalProduction, metalProduced);
    }
    
    @Test(expected = IndustrySimulationException.class)	
    public void testRegionNameDuplication(){
    	sim.createRegion("region");
    	sim.createRegion("fillerRegion1");
    	sim.createRegion("fillerRegion2");
    	sim.createRegion("reGION");
    }


}
