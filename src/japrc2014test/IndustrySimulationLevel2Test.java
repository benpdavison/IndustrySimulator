//Exam Candidate Number Y3586958
package japrc2014test;

import static org.junit.Assert.*;

import japrc2014.IndustrySimulation;
import japrc2014.IndustrySimulationInterface;

import org.junit.Before;
import org.junit.Test;

public class IndustrySimulationLevel2Test {

	IndustrySimulationInterface sim;
	int rID1 = 0;
	int rID2 = 0;
	int rID3 = 0;
	int rID4 = 0;
	int rID5 = 0;
	int rID6 = 0;
	
	
    @Before
    public void setUp() throws Exception
    {
        sim = new IndustrySimulation();
        rID1 = sim.createRegion("region 1");
        rID2 = sim.createRegion("region 2");
        rID3 = sim.createRegion("region 3");
        rID4 = sim.createRegion("region 4");
        rID5 = sim.createRegion("region 5");
        rID6 = sim.createRegion("region 6");
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_MINE, rID1);
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_REFINERY, rID2);
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_SMELTER, rID3);
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_CHEMICAL_PLANT, rID4);
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_FACTORY, rID5);
    	sim.createEntity(IndustrySimulation.EntityTypes.ENTITY_DISTRIBUTOR, rID6);

    	sim.connectRegions(rID1, rID3);
    	sim.connectRegions(rID2, rID4);
    	sim.connectRegions(rID3, rID5);
    	sim.connectRegions(rID4, rID5);
    	sim.connectRegions(rID5, rID6);
    }



    @Test
    public void testEndOfDayMovement(){
    	boolean simHasMoney = false;
    	sim.runBlocking(200);
    	if (sim.getResourceCount(IndustrySimulation.ResourceTypes.RESOURCE_MONEY)!=0){
    		simHasMoney = true;
    	}
    	assertTrue(simHasMoney);
    	
    }
    
    @Test
    public void testGetSetSimState(){
    	sim.runBlocking(1000);
       	String stateString1 = sim.getSimState();
       	sim = new IndustrySimulation();
    	sim.setSimState(stateString1);
    	String stateString2 = sim.getSimState();
    	assertEquals(stateString1, stateString2);
    }

    
    @Test
    public void testGetConnectivityMatrix(){
    	boolean[][] expected2DArray = new boolean[6][];
    	expected2DArray[0] = new boolean[]{false, false, true, false, false, false};
    	expected2DArray[1] = new boolean[]{false, false, false, true, false, false};
    	expected2DArray[2] = new boolean[]{true, false, false, false, true, false};
    	expected2DArray[3] = new boolean[]{false, true, false, false, true, false}; 
    	expected2DArray[4] = new boolean[]{false, false, true, true, false, true};
    	expected2DArray[5] = new boolean[]{false, false, false, false, true, false};
    	boolean[][] actual2DArray = sim.getConnectivityMatrix();
    	for (int i=0; i<6; i++){
    		for (int j = 0; j<6; j++){
    			boolean expected = expected2DArray[i][j];
    			boolean actual = actual2DArray[i][j];
    			assertEquals(expected, actual);
    		}
    	}
    	
    					
    }
}
