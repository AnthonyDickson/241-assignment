package week09;

/**
 * Exception to be thrown by shuffle(int[] blocks) and order(int[] blocks) if
 * blocks contains any negative integers, or the sum of its entries is not
 * equal to the total size of the deck.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony Dickson, Johnny Mann, Maurice Andrews
 */
@SuppressWarnings("unchecked") // Suppress warnings about serialVersionUID
public class BlockSizeException extends RuntimeException {
    /**
     * Default constructor, initialises the super class with the
     * default message.
     */
    public BlockSizeException(){
        super("Block sizes must be positive, and the sum equal to the deck " +
            "size.");
    }
}
