/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class represents the MMU hardware and implements the interface as 
 * specified in the project write up. The beginning of trying to find the best way to 
 * "simulate" hardware as a bunch of software classes. I admit this is probably trying 
 * to do too much with one class but can improve upon it if time permits. 
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MMUHardware implements MMU {

    final static int LRU = 0;                       // For Least Recently Used
    final static int FIFO = 1;                      // For First In First Out
    final int NUMBER_OF_PAGES = 16;
    final int NUMBER_OF_FRAMES = 4;
    final int PAGE_SIZE = 1024;
    
    private PageTable pageTable;                    // Tells which pages are in which frames.
    private FileSystem fileSystem;                  // Represents our virtual pages.
    private String referenceString;                 // The pages that caused a fault, in order.
    private Memory memory;                          // Represents our physical frames. 
    private int numPageFaults;                      // Number of times a page is not in memory.
    private int replacementMethod;                  // Either LRU or FIFO, must Choose one.
    private PrintWriter pWriter;                    // Used to output the contents of everything.
    
    
    /**
     * This will construct a new MMU along with the other bits of hardware needed.
     * 
     * @param method - The type of replacement policy to follow which can be found as static 
     * class variables LRU and FIFO.
     */
    public MMUHardware(int method) {
        
        // These are the items most likely part of the MMU itself.
        numPageFaults = 0;
        referenceString = "Order of page faults: ";
        pageTable = new PageTable(NUMBER_OF_PAGES);
        replacementMethod = method;
        
        // These are the items outside the MMU but it works with and accesses.
        memory = new Memory(NUMBER_OF_FRAMES, PAGE_SIZE);
        fileSystem = new FileSystem(NUMBER_OF_PAGES, PAGE_SIZE);
        
        // This way memory can work together with MMU which primarily just gives orders.
        memory.setFileSystem(fileSystem);
        memory.setPageTable(pageTable);
        
        // This will be so we can visually see each location has the correct value.
        setupWriter();
        
    } // end MMUHardware constructor
    
    
    /**
     * This method will create a new file in which to write line sums to.
     * 
     * @return A boolean true if creation goes smoothly, false otherwise.
     */
    private Boolean setupWriter() {
        
        try {
            
            pWriter = new PrintWriter(new FileWriter("SimulationOutput.txt"));
            return true;
        
        } catch(IOException e) {
            
            System.out.println("Error creating print writer.");
            
        }
        
        return false;
        
    } // end setupWriter
    
    
    /**
     * This method will write a particular value to an address in the virtual file system.
     * 
     * @param address - The address to write to.
     * 
     * @param value - The value to be written.
     * 
     * @return - A boolean true if value was written correctly, false otherwise.
     */
    @Override
    public boolean writeMemory(int address, int value) {
        
        // Get the page number and offset location to write the value.
        int newPage = address / 1024;
        int offset = address % 1024;
        
        // If the page isn't in memory we need to bring it into memory.
        checkForPageOrSwap(newPage);
        
        // now that we know the page is in a frame, we need to know which.
        int frameNumber = pageTable.getFrameNumber(newPage);
        
        // Once we have a valid frame, let's write it all.
        return memory.writeToFrame(frameNumber, offset, value);
        
    } // end writeMemory
    

    /**
     * This method will read a value from an address in the virtual file system.
     * 
     * @param address - The address to read from.
     * 
     * @return - The value at that address.
     */
    @Override
    public int readMemory(int address) {
        
        // Get the page number and offset location to write the value.
        int newPage = address / 1024;
        int offset = address % 1024;
        
        // If the page isn't in memory we need to bring it into memory.
        checkForPageOrSwap(newPage);

        // now that we know the page is in a frame, we need to know which.
        int frameNumber = pageTable.getFrameNumber(newPage);
        
        // Once we have a valid frame, let's write it all.
        return memory.readFromFrame(frameNumber, offset);
        
    }
    
    
    /**
     * This method will make sure the specified page is placed in memory if not
     * already there.
     * 
     * @param newPage - The page that needs to be in memory.
     */
    private void checkForPageOrSwap(int newPage) {
        
        // If the page isn't in memory we need to bring it into memory.
        if(!pageTable.isValid(newPage)) {
            
            switch (replacementMethod) {
            
                case LRU:   swapViaLRU(newPage);
                            break;
                        
                case FIFO:  swapViaFIFO(newPage);
                            break;
                        
                default:    System.out.println("Invalid replacement method specified!");
                            System.exit(-1); // CRASH!
            
            } // end switch
            
            // Since we had to swap, update fault and reference string.
            numPageFaults++;
            referenceString += newPage + ", ";
            
        }
        
    } // end checkForPageOrSwap
    
    
    /**
     * If the page is not in memory, this method is used to get it there when
     * LRU is selected.
     * 
     * @param newPage - The page that needs to be in memory.
     */
    private void swapViaLRU(int newPage) {
        
        // Find out which frame to swap out.
        int LRUFrame = memory.getLRUFrame();
        int victimPage = pageTable.getPageNumber(LRUFrame);
        
        // if victimPage is -1 then the frame is free.
        if(victimPage == -1) {
            
            // Since the original unused frames are not dirty, they won't be "swapped" back or overwrite anything.
            memory.swapFrameLRU(newPage, LRUFrame);
            
        } else {
            
            // Otherwise we now know new page and old so swap out.
            memory.swapFrameLRU(newPage, victimPage);
            
        }
        
    } // end swapViaLRU
    
    
    /**
     * If the page is not in memory, this method is used to get it there when
     * FIFO is selected.
     * 
     * @param newPage - The page that needs to be in memory.
     */
    private void swapViaFIFO(int newPage) {
        
        // Find out which frame to swap out.
        int FIFOFrame = memory.getFIFOFrame();
        int victimPage = pageTable.getPageNumber(FIFOFrame);
        
        // if victimPage is -1 then the frame is free.
        if(victimPage == -1) {
            
            // Since the original unused frames are not dirty, they won't be "swapped" back or overwrite anything.
            memory.swapFrameFIFO(newPage, FIFOFrame);
            
        } else {
            
            // Otherwise we now know new page and old so swap out.
            memory.swapFrameFIFO(newPage, victimPage);
            
        }
        
    } // end swapViaFIFO

    
    /**
     * This method basically just puts the simulated hardware through its' paces. Reading 
     * and Writing to each location, keeping tabs on page faults and their order.
     */
    @Override
    public void startSimulation() {

        // Taking the methodology from the UnitTest example, no user input, just
        // auto generate values seems to be a good way to test. 
        
        System.out.print("Simulation running!");
        
        // Fill each virtual memory location with it's own address.
        for (int i = 0 ; i < 16384 ; i++) {
            
            writeMemory(i, i);
            
        }
        
        // Read to make sure each location holds it's own address.
        for (int i = 0 ; i < 16384 ; i++) {
            
            if (readMemory(i) != i) {
                
                // A warning will appear if not as expected.
                System.out.println("Address contains the wrong value!\n");
                System.exit(-1); // CRASH!
                
            } else {
                
                // If correct, add values to an output file to view later.
                pWriter.println("Value at address " + i + ": " + readMemory(i));
                
            }
            
        }
        
        pWriter.close();
        System.out.println("\n");
        
    } // end startSimulation

    
    /**
     * This method will print out a general overview of system performance.
     */
    @Override
    public void stopSimulation() {

        System.out.println("Simulation ended!");
        System.out.println("Number of page faults: " + numPageFaults);
        System.out.println(referenceString);
        
    } // end stopSimulation
    

    /**
     * This will return the reference string which lists the page faults in the order
     * in which they happened.
     * 
     * @return - A string listing off each page that caused a fault and in what order.
     */
    @Override
    public String getReferenceString() {
        
        return referenceString;
        
    } // end getReferenceString

    
    /**
     * This will return the total number of page faults.
     * 
     * @return - The total number of page faults.
     */
    @Override
    public int getTotalPageFaults() {
        
        return numPageFaults;
        
    } // end getTotalPageFaults
    
} // end MMUHardware