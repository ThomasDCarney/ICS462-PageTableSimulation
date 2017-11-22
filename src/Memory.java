/**
 * Class: ICS 462 - Operating Systems <br>
 * Instructor: Michael Dorin <br>
 * Description: Program 3, Simulating Memory Management. <br>
 * Due: 04/12/2016 <br><br>
 * 
 * This particular class represents a memory module which has a variable amount of frames
 * to store virtual pages as needed.
 * 
 * @author Tom Carney
 * @version 1.0
 * @since 04/01/2016
 */

public class Memory {
    
    private Frame[] frames;         // An array of frames to store pages
    private FileSystem fileSystem;  // Where the pages are stored
    private PageTable pageTable;    // Where everyting is organized
    private int FIFOFrame = 0;      // For use when FIFO replacement is used.
    private int LRUFrame = 0;       // For use when LRU replacement is used.
    
    
    /**
     * This will construct a new memory unit with the specified number of frames, 
     * each having the specified frame size.
     * 
     * @param numFrames - The number of frames desired.
     * 
     * @param frameSize - The size of each frame.
     */
    public Memory(int numFrames, int frameSize) {
        
        frames = new Frame[numFrames];
        
        for(int i = 0 ; i < frames.length ; i++) {
            
            frames[i] = new Frame(frameSize);
            
        } 
        
    } // end no argument Memory Constructor
    
    
    /**
     * This will give the memory unit access to the file system where pages
     * are stored.
     * 
     * @param newFileSystem - The file system memory needs access to.
     */
    public void setFileSystem(FileSystem newFileSystem) {
        
        fileSystem = newFileSystem;
        
    } // end setFileSystem
    
    
    /**
     * This will give the memory unit access to the page table that keeps
     * tabs on which pages are in which frames. 
     * 
     * @param newPageTable - The page table.
     */
    public void setPageTable(PageTable newPageTable) {
        
        pageTable = newPageTable;
        
    } // end setPageTable
    
    
    /**
     * This method will take care of swapping out one page for another via FIFO replacement
     * rules.
     * 
     * @param newPage - The page that needs into memory.
     * 
     * @param currentPage - The page to store away.
     */
    public void swapFrameFIFO(int newPage, int currentPage) {
        
        // All frames are instantiated as clean but if a frame has been written to, it 
        // should be saved back before swapping out.
        if(frames[FIFOFrame].isDirty()) {
            
            // Write the frame back to its page.
            for(int i = 0 ; i < frames[FIFOFrame].getLength() ; i++) {
                
                fileSystem.writeToPage(currentPage, i, frames[FIFOFrame].readFromAddress(i));
                
            }
            
            // It's no longer dirty because it has been written back to the page.
            frames[FIFOFrame].setNotDirty();
            
        }
        
        // Now that dirty frame issues have been dealt with move the needed page into place.
        for(int i = 0 ; i < frames[FIFOFrame].getLength() ; i++) {
            
            // This overwrites the victim frame with the one requested.
            frames[FIFOFrame].writeToAddress(i, fileSystem.readFromPage(newPage, i));
            
        }
        
        // Update the page table that the victim frame is no longer in memory.
        pageTable.markInvalid(currentPage);
        
        // Update the page table that the requested frame is in memory and where.
        pageTable.addPageToFrame(newPage, FIFOFrame);
        
        // Update the next victim frame, 0 through frames.length - 1.
        FIFOFrame = (FIFOFrame + 1) % frames.length;
        
    } // end swapFrameFIFO
    
    
    /**
     * This method will take care of swapping out one page for another via LRU replacement
     * rules.
     * 
     * @param newPage - The page that needs into memory.
     * 
     * @param currentPage - The page to store away.
     */
    public void swapFrameLRU(int newPage, int currentPage) {
        
        // All frames are instantiated as clean but if a frame has been written to, it 
        // should be saved back before swapping out.
        if(frames[LRUFrame].isDirty()) {
            
            // Write the frame back to its page.
            for(int i = 0 ; i < frames[LRUFrame].getLength() ; i++) {
                
                fileSystem.writeToPage(currentPage, i, frames[LRUFrame].readFromAddress(i));
                
            }
            
            // It's no longer dirty because it has been written back to the page.
            frames[LRUFrame].setNotDirty();
            
        }
        
        // Now that dirty frame issues have been dealt with move the needed page into place.
        for(int i = 0 ; i < frames[LRUFrame].getLength() ; i++) {
            
            // This overwrites the victim frame with the one requested.
            frames[LRUFrame].writeToAddress(i, fileSystem.readFromPage(newPage, i));
            
        }
        
        // Update the page table that the victim frame is no longer in memory.
        pageTable.markInvalid(currentPage);
        
        // Update the page table that the requested frame is in memory and where.
        pageTable.addPageToFrame(newPage, LRUFrame);
        
    } // end swapFrameFIFO
    
    
    /**
     * This method returns the current victim frame via FIFO replacement rules.
     * 
     * @return - The next frame to be replaced via FIFO.
     */
    public int getFIFOFrame() {
        
        return FIFOFrame;
        
    } // end getFIFOFrame
    
    
    /**
     * This method will get the least recently used (oldest) frame based on each frames 
     * timestamp, updated each time it is accessed.
     * 
     * @return - The LRU frame number.
     */
    public int getLRUFrame() {
        
        // Initially all timestamps are 0, replaced with actual time in milliseconds 
        // when written to. 
        
        LRUFrame = 0; // until proven otherwise
        
        for(int i = 1 ; i < frames.length ; i++) {
            
            if(frames[i].getTimeStamp() < frames[LRUFrame].getTimeStamp()) {
                
                LRUFrame = i;
                
            }
            
        }
        
        return LRUFrame;
        
    } // end getOldestFrame
    
    
    /**
     * This method allows you to write to a particular frame.
     * 
     * @param frameNumber - The frame you want to write to.
     * 
     * @param offset - The cell in the frame you wish to write to.
     * 
     * @param value - The value to write.
     * 
     * @return - A boolean true if all went well, false otherwise.
     */
    public boolean writeToFrame(int frameNumber, int offset, int value) {
        
        if(frameNumber >= 0 && frameNumber < frames.length) {
            
            frames[frameNumber].writeToAddress(offset, value);
            frames[frameNumber].setDirty();
            
            // This is only used by certain replacement methods, LRU specifically.
            frames[frameNumber].setTimeStamp();
            return true;
            
        } else {
            
            System.out.println("Writing frame number " + frameNumber + " out of range!");
            return false;
            
        }
        
    } // end writeToFrame
    
    
    /**
     * This method allows you to read from a particular frame.
     * 
     * @param frameNumber - The frame you wish to read from.
     * 
     * @param offset - The particular cell you wish to read.
     * 
     * @return - The value found in that frame's cell.
     */
    public int readFromFrame(int frameNumber, int offset) {
        
        if(frameNumber >= 0 && frameNumber < frames.length) {
            
            // This is only used by certain replacement methods, LRU specifically.
            frames[frameNumber].setTimeStamp();
            
            return frames[frameNumber].readFromAddress(offset);
            
        } else {
            
            System.out.println("Reading frame number " + frameNumber + " out of range!");
            
            return -9999; // totally invalid value for our testing
            
        }
        
    } // end readFromFrame
    
    
    @Override
    public String toString() {
        
        String returnString = "This memory unit contains " + frames.length + " Frames,\n\n";
        
        for(int i = 0 ; i < frames.length ; i++) {
            
            returnString += "Frame " + i + ":\n ";
            returnString += frames[i];
            
        }
        
        return returnString;
        
    } // end toString
    
} // end Memory