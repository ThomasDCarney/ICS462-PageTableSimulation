/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class represents a Frame in memory which is used to hold a virtual 
 * page when needed.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public class Frame {
    
    private int[] frameData;    // Holds the indexed array of data.
    private long timeStamp;     // Used for the LRU replacement strategy
    private boolean dirty;      // Keeps track whether a page has been modified while in memory.
    
    
    /**
     * Public constructor for the frame class.
     * 
     * @param frameSize - The number of addresses stored per page/frame.
     */
    public Frame(int frameSize) {
        
        frameData = new int[frameSize];
        
        // This will change only if LRU or like replacement methods are used.
        timeStamp = 0;
        dirty = false;
        
    } // end Frame constructor
    
    
    /**
     * This is used to get the length of/number of addresses per frame.
     * 
     * @return - The number of addresses per frame.
     */
    public int getLength() {
        
        return frameData.length;
        
    } // end getLength
    
    
    /**
     * This method determines whether the frame has been modified while in memory and 
     * changes need to be written back.
     * 
     * @return - A boolean true if the values were modified in memory, false otherwise.
     */
    public boolean isDirty() {
        
        return dirty;
        
    } // end isDirty
    
    
    /**
     * If the frame has been altered while in memory, it should be marked dirty so that the
     * updates can be stored before being written over.
     */
    public void setDirty() {
        
        dirty = true;
        
    } // end setDirty
    
    
    /**
     * If the frame has been written back to it's page on the file system, it is technically
     * no longer dirty so should be set as such. That does not mean it must be replaced in 
     * memory but data could be backed up periodically.
     */
    public void setNotDirty() {
        
        dirty = false;
        
    } // end setNotDirty
    
    
    /**
     * This method is used to update the value in a particular in the frame.
     * 
     * @param offset - The address location to be updated.
     * 
     * @param value - The value to be updated at that location.
     * 
     * @return - A boolean true if the value was updated, false otherwise.
     */
    public boolean writeToAddress(int offset, int value) {
        
        if(offset >= 0 && offset < frameData.length) {
            
            //System.out.println("Writing " + value + " to offset " + offset);
            frameData[offset] = value;
            return true;
            
        } else {
            
            System.out.println("Writing to address " + offset + " out of range!" );
            return false;
            
        }
        
    } // end writeToAddress
    
    
    /**
     * This method is used to read the value at a particular address in the frame.
     * 
     * @param address - The address for which you want the value at.
     * 
     * @return - The value at that address.
     */
    public int readFromAddress(int address) {
        
        if(address >= 0 && address < frameData.length) {
            
            return frameData[address];
            
        } else {
            
            System.out.println("reading from address " + address + " out of range!");
            
            return -9999; // totally invalid value for our testing
            
        }
        
    } // end readFromAddress
    
    
    /**
     * This method will update the frames timestamp in milliseconds.
     */
    public void setTimeStamp() {
        
        timeStamp = System.currentTimeMillis();
        
    } // end setTimeStamp
    
    
    /**
     * This method will return the frames current timestamp.
     * 
     * @return - The frames current timestamp in milliseconds as a long.
     */
    public long getTimeStamp() {
        
        return timeStamp;
        
    } // end getTimeStamp
    
    
    /**
     * This method returns a String representation of the frame.
     */
    @Override
    public String toString() {
        
        String returnString = "";
        
        for (int i : frameData) {
            
            returnString += (i + ", ");
            
            if(i % 15 == 0) {
                
                returnString += "\n";
                
            }
            
        }
        
        returnString += "\n";
        
        return returnString;
        
    } // end toString
    
} // end Frame