# IndustrySimulator
Industry Simulator created for Advanced Programming Concepts in Java Masters Module

Program Built in Eclipse os has .Project files.

To run use the java command line to run the "IndustrySimulationGUI.java" file found in the "src" folder.
Hand Out Document Detailing Requirements

APRC Open Assessment: “Industry Simulation Pro”


1  The Program

For this assessment, you need to implement a simulation of mining and manufacturing activities within a country, from the initial extraction of raw materials from the ground to the sale of manufactured goods to retailers.

1.1  Simulation Behaviour

The simulation models a single country, which consists of a graph of regions. A region is an area of physical space that the country’s government has defined for administrative purposes. Regions can be connected to other regions – if two regions are connected, then excess materials from one region can move into another that needs them.

For example, in a model for the UK , there might be one region for each county. There would be one for each of North, West and East Yorkshire. Each of those regions would be connected to the other two, because they are physically adjacent. “Cornwall” might be another region – it would not be connected to any of the Yorkshire regions, because it is has no physical border with them.

Each region contains entities of various types. These are buildings or facilities which consume and produce various resources. Each region may have any number of entities of each type.

For example, North Yorkshire might contain 6 Mines and 2 Chemical Plants.

You will need to simulate the passage of time. For this purpose, you can model it as a series of hours, numbered 0-23 within each day. Days are numbered from 0 onwards.

Important note: in common with most simulations of this type, there is no necessary relationship between simulation time and real time. When instructed to run continuously, the simulation should run simulated hours and days as fast as it can given the computing power available, with no concern for the real world hours and days that are passing.









