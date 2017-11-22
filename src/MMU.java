/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This is the interface specified in the program documentation.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public interface MMU {
    
    boolean writeMemory(int address, int value);
    
    int readMemory(int address);
    
    void startSimulation();
    
    void stopSimulation();
    
    String getReferenceString();
    
    int getTotalPageFaults();
    
} // end MMU Interface