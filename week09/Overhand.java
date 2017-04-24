package week09;

/**
 * Interface for manipulating a deck of cards using overhand shuffling.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony
 * @see <a href="http://www.cs.otago.ac.nz/cosc241/pdf/asgn.pdf">http://www.cs.otago.ac.nz</a>
 */
public interface Overhand {
    /**
     * Makes a new deck consisting of size cards numbered from 0 up to size-1
     * from top to bottom.
     * @param size The size of the deck to create.
     */
    void makeNew(int size);

    /**
     *  Returns the current state of the deck as an int[] (modifying the
     *  result shouldn't effect the original deck).
     * @return The a copy of the current state of the deck
     */
    int[] getCurrent();

    /**
     * Shuffles the current state of the deck according to the array of block
     * sizes given.
     * @param blocks Array of block sizes.
     */
    void shuffle(int[] blocks) throws BlockSizeException;

    /**
     * Returns the minimum number of times that the deck could be shuffled
     * (from its initial state) using the same set of block sizes each time
     * (as given by its argument) in order to return the deck to its initial
     * state.
     * @param blocks Array of block sizes.
     * @return Minimum number of times the deck can be shuffled.
     */
    int order(int[] blocks);

    /**
     * Returns the number of pairs of cards which were consecutive in the
     * original deck, and are still consecutive (and in the same order) in the
     * current state of the deck.
     * @return Number of unbroken pairs of cards.
     */
    int unbrokenPairs();
}

