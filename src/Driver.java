/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class is just a driver, creates an MMU, starts and stops
 * the simulation.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public class Driver {
    
    public static void main(String[] args) {
        
        // You can change the type of replacement policy from LRU to FIFO here.
        // They are just final static ints in the MMUHardware class.
        MMUHardware mmu = new MMUHardware(MMUHardware.LRU);
        
        mmu.startSimulation();
        mmu.stopSimulation();
        
    } // end main
    
} // end Driver