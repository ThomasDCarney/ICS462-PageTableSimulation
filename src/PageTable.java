/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class represents the page table which state which frame a page was
 * last located and if it is still valid.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public class PageTable {
    
    // Array row index = physical page number (0 -> numPages.
    // Column 0 = Frame in which the page resides in physical memory.
    // Column 1 = valid bit (0 = invalid, 1 = valid).
    private int[][] table;
    
    
    /**
     * This constructs a new page table based on the specified number of pages.
     * 
     * @param numPages - The specified number of pages.
     */
    public PageTable(int numPages) {
        
        table = new int[numPages][2];
        initializeTable();
        
    } // end PageTable constructor
    
    
    /**
     * This is a private method setting up the page table as if new and unused.
     */
    private void initializeTable() {
        
        for(int i = 0 ; i < table.length ; i++) {
            
            table[i][0] = -1;   // Mark all with an invalid frame.
            table[i][1] = 0;    // Mark all as invalid to start, no page in a frame.
            
        }
        
    } // end initializeTable
    
    
    /**
     * This method is used to get the frame number where a page has been copied into.
     * 
     * @param pageNumber - The page you want the frame number for.
     * 
     * @return - The frame number if the page resides in one, -1 if it does not.
     */
    public int getFrameNumber(int pageNumber) {
        
        // If the page is valid.
        if(table[pageNumber][1] == 1) {
            
            // Return the frame number.
            return table[pageNumber][0];
            
        }
        
        return -1;  // Return negative if page not in memory.
        
    } // end getFrameNumber
    
    
    /**
     * This method will return the page number based on a given frame.
     * 
     * @param frameNumber - The frame number that holds a particular page.
     * 
     * @return - The page in that frame, or -1 if no page is in that frame.
     */
    public int getPageNumber(int frameNumber) {
        
        for(int i = 0 ; i < table.length ; i++) {
            
            if(table[i][0] == frameNumber && table[i][1] == 1) {
                
                // Index i is the page in that frame.
                return i;
                
            }
            
        }
        
        // Should happen each time a frame is used for the first time.
        return -1;
        
    } // end getPageNumber
    
    
    /**
     * This will tell you if a particular page number is in a frame.
     * 
     * @param pageNumber - The page number in question.
     * 
     * @return - A boolean true if the page is in a frame, false otherwise.
     */
    public boolean isValid(int pageNumber) {
        
        return table[pageNumber][1] == 1;
        
    } // end isValid
    
    
    /**
     * This will mark a page as invalid or no longer residing in a frame.
     * 
     * @param pageNumber - The page number that is no longer in a frame.
     */
    public void markInvalid(int pageNumber) {
        
        table[pageNumber][1] = 0;
        
    } // end clearFrame
    
    
    /**
     * This method marks a page as being loaded into a particular frame.
     * 
     * @param pageNumber - The page number that has been loaded.
     * 
     * @param frameNumber - The frame in which it resides.
     */
    public void addPageToFrame(int pageNumber, int frameNumber) {
        
        table[pageNumber][0] = frameNumber;
        table[pageNumber][1] = 1;
        
    } // end updateFrame
    
    
    /**
     * This will return a String representation of the page table object.
     */
    @Override 
    public String toString() {
        
        String returnString = "";
        
        for(int i = 0 ; i < table.length ; i++) {
            
            returnString += "page " + i + " was in frame " + table[i][0] + " is valid: " + table[i][1];
            
        }
        
        return returnString;
        
    } // end toString
    
} // end PageTable