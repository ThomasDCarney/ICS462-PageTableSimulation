/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class represents a the file system where virtual pages are located.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public class FileSystem {
    
    // This will represent the virtual memory found on the HD
    private int[][] pages;
    
    
    /**
     * This will construct a new file system with the specified number of virtual pages, 
     * each of which have the specified size.
     * 
     * @param numPages - The number of virtual pages.
     * 
     * @param pageSize - The size of each virtual page.
     */
    public FileSystem(int numPages, int pageSize) {
        
        pages = new int[numPages][pageSize];
        
    } // end FileSystem Constructor
   

    
    /**
     * This method allows you to write a value to a specified address of a specified page.
     * 
     * @param pageNum - The specific page number.
     * 
     * @param offset - The specific cell.
     * 
     * @param value - The value to write.
     */
    public void writeToPage(int pageNum, int offset, int value) {
       
            pages[pageNum][offset] = value;
        
    } // end writeToPage
    
    
    /**
     * This method allows you to read a value from a specified page and address.
     * 
     * @param pageNum - The page in question.
     * 
     * @param offset - The address in question.
     * 
     * @return - The value in the address of that cell.
     */
    public int readFromPage(int pageNum, int offset) {
        
        return pages[pageNum][offset];
        
    } // end readFromPage
    
    
    /**
     * This method will return a string representation of the file system.
     */
    public String toString() {
        
        String returnString = "";
        
        for(int i = 0 ; i < pages.length ; i++) {
            
            // not terribly useful but also not a priority right now.
            returnString += pages[i] + "\n";
            
        }
        
        return returnString;
    
    } // end toString
    
} // end FileSystem