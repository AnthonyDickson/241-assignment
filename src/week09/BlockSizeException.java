package week09;

/**
 * Exception to be thrown by shuffle(int[] blocks) and order(int[] blocks) if
 * blocks contains any negative integers, or the sum of its entries is not
 * equal to the total size of the deck.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony
 */
public class BlockSizeException extends Exception {
    /**
     * Default constructor.
     */
    public BlockSizeException(){
        super();
    }

    /**
     * Constructor overload with String message.
     * @param message The message to display.
     */
    public BlockSizeException(String message){
        super(message);
    }
}
