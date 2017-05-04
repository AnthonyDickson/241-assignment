package week09;

import java.util.Arrays;

/**
 * Implements the Overhand interface for manipulating a deck of cards using
 * overhand shuffling.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony Dickson, Johnny Mann, Maurice Andrews
 */
public class OverhandShuffler implements Overhand {
    /** The deck. */
    private int[] deck = {};

    /**
     * Makes a new deck consisting of size cards numbered from 0 up to size-1
     * from top to bottom.
     * @param size The size of the deck to create.
     */
    public void makeNew(int size) {
        this.deck = new int[size];

        for (int i = 0; i < size; i++) {
            this.deck[i] = i;
        }
    }

    /**
     *  Returns the current state of the deck as an int[] (modifying the
     *  result shouldn't effect the original deck).
     * @return The a copy of the current state of the deck
     */
    public int[] getCurrent() {
        return Arrays.copyOf(this.deck, this.deck.length);
    }

    /**
     * Shuffles the current state of the deck according to the array of block
     * sizes given.
     * @param blocks Array of block sizes.
     */
    public void shuffle(int[] blocks) {
        // Ensure that blocks:
        //  contains no negative numbers
        //  has a sum equal to the deck size
        int blocksSum = 0;

        for (int block : blocks) {
            if (block < 0) {
                throw new BlockSizeException();
            }

            blocksSum += block;
        }

        if (blocksSum != deck.length) {
            throw new BlockSizeException();
        }

        // The next state of the deck.
        int[] newDeck = new int[deck.length];
        // Where to copy from in the deck.
        int copyFrom  = 0;
        // Where to insert into the new deck.
        int insertAt = deck.length;

        for (int blockSize : blocks) {
            // Move the insertAt to the left.
            insertAt -= blockSize;

            // Copy the block of the cards into the new deck.
            System.arraycopy(deck, copyFrom, newDeck, insertAt, blockSize);

            // Move copyFrom to the right.
            copyFrom += blockSize;
        }

        this.deck = newDeck;
    }

    /**
     * Returns the minimum number of times that the deck could be shuffled
     * (from its initial state) using the same set of block sizes each time
     * (as given by its argument) in order to return the deck to its initial
     * state.
     * @param blocks Array of block sizes.
     * @return Minimum number of times the deck can be shuffled.
     */
    public int order(int[] blocks) {
        // Store the state of the deck so we can restore it later.
        int[] originalState = this.getCurrent();
        boolean isMatching = false;
        int result = 0;
        int matches = 0;

        while(!isMatching) {
            this.shuffle(blocks);
            result++;
            
            // Check if decks are matching.
            matches = 0;
            for (int i = 0; i < this.deck.length; i++) {
                if (this.deck[i] == originalState[i]) {
                    matches++;
                }
            }

            if (matches == this.deck.length) {
                isMatching = true;
            }
        }

        // Restore the original state of the deck.
        this.deck = originalState;

        return result;
    }

    /**
     * Returns the number of pairs of cards which were consecutive in the
     * original deck, and are still consecutive (and in the same order) in the
     * current state of the deck.
     * @return Number of unbroken pairs of cards.
     */
    public int unbrokenPairs() {
        int result = 0;
        // In the original state of the deck, there will always be 1 less pairs
        // than the length.
        int numPairs = this.deck.length - 1;

        int a;
        int b;

        for (int j = 0; j < numPairs; j++) {
            // Loop through each pair, a & b, that would have been in the
            // original state of the deck.
            // Pair a,b == a,a+1 where a = j
            a = j;
            b = a + 1;

            // We will be accessing i + 1 so we should loop 1 less time.
            for (int i = 0; i < this.deck.length - 1; i++) {
                if (this.deck[i] == a && this.deck[i + 1] == b) {
                    result++;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Tries to figure out what sequence of shuffles were performed upon the
     * deck and what the state would be if the same sequence of shuffles
     * were repeated.
     * @return The predicted state.
     */
    public int[] tryRepeat() {
        // The next state of the deck can be predicted by calculating the net
        // change, or difference, of each elements' index from its original.
        // Moving the element in that original index the same number of indexes
        // will result in the same net change and thus the next state can be
        // predicted.
        int[] predictedState = new int[this.deck.length];

        // Loop through the current state of the deck,
        // calculate how much each card has moved from its original position,
        // get the card that many positions away from the current index i,
        // and store that card in the predictedState at array index i.
        for (int i = 0; i < this.deck.length; i++) {
            predictedState[i] = this.deck[i - (i - this.deck[i])];
        }

        return predictedState;
    }

    /**
     * Perform a random overhand shuffle using a break probability of 0.1.
     */
    public void randomShuffle() {
        final double breakChance = 0.1;
        int block = 0;  // size of current block
        int[] blocks = {};
        int blockSum = 0; 
        int insert = 0; // insert point  
        
        while (blockSum < this.deck.length) {
            block++;

            // cuts of block at given probability 
            if (Math.random() <= breakChance) {  
                // makes sure block will fit in array                        
                if (blockSum + block > this.deck.length ) { 
                    block = this.deck.length - blockSum;
                }
              
                blocks = Arrays.copyOf(blocks, blocks.length + 1);
                blocks[insert] = block; //inserts block
                blockSum += block; 
                insert++;
                block = 0; // resets size of block
            }         
        }

        this.shuffle(blocks);
    }

    /**
     * Return how many random shuffles need to be done before the number of
     * unbroken pairs is less than the given parameter.
     * @param unbrokenPairs How many unbroken pairs we should look for.
     * @return Number of random shuffles that need to be done before the
     * number of unbroken pairs is less than the given parameter.
     */
    public int countShuffles(int unbrokenPairs) {
        if (unbrokenPairs < 0) { // Unbroken pairs must be positive.
            return -1;
        } 
        // Store the state of the deck so we can restore it later.
        final int[] originalState = this.getCurrent();

        int result = 0;

        while (this.unbrokenPairs() > unbrokenPairs) {
            this.randomShuffle();
            result++;
        }

        // Restore the original state of the deck.
        this.deck = originalState;

        return result;
    }

    /**
     * Load the deck using the given numbers. No error checking is done.
     * @param cards The space separated string of ints (cards).
     */
    public void load(String[] cards) {
        this.deck = new int[cards.length];

        for (int i = 0; i < cards.length; i++) {
            deck[i] = Integer.parseInt(cards[i]);
        }
    }

    /**
     * Stringify the current state of the deck.
     * @return The current state of the deck as a string.
     */
    public String toString() {
        return Arrays.toString(this.deck);
    }
}
