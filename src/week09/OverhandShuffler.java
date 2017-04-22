package week09;

import java.util.Arrays;

/**
 * Implements the Overhand interface for manipulating a deck of cards using
 * overhand shuffling.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony
 */
public class OverhandShuffler implements Overhand {
    private int[] deck;

    public void makeNew(int size) {
        this.deck = new int[size];

        for (int i = 0; i < size; i++) {
            this.deck[i] = i;
        }
    }

    public int[] getCurrent() {
        return Arrays.copyOf(this.deck, this.deck.length);
    }

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
        int[] nextState = new int[deck.length];
        // The current working position in the deck. Starts at one position past the last element.
        int cursor  = deck.length;
        // Where to insert into the new deck.
        int insertCursor  = 0;

        for (int blockSize : blocks) {
            // The index to start copying from.
            int left = cursor - blockSize;

            // Insert the elements in between the left and right (left + blockSize) indices...
            System.arraycopy(deck, left, nextState, insertCursor, blockSize);

            // Move the insertCursor to the right.
            insertCursor += blockSize;
            // Move the cursor to the left.
            cursor -= blockSize;
        }

        this.deck = nextState;
    }

    public int order(int[] blocks) {
        // Store the state of the deck so we can restore it later.
        int[] originalState = this.getCurrent();
        boolean isMatching = false;
        int result = 0;

        while(!isMatching) {
            this.shuffle(blocks);
            result++;

            // First, assume the decks are matching.
            isMatching = true;
            // Then, check if decks are actually matching.
            for (int i = 0; i < this.deck.length; i++) {
                if (this.deck[i] != originalState[i]) {
                    isMatching = false;
                    break;
                }
            }
        }

        // Restore the original state of the deck.
        this.deck = originalState;

        return result;
    }

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
        for (int i = 0; i < this.deck.length; i++) {
            // calculate how much each card has moved from its original position,
            // int deltaIndex = i - deck[i]

            // get the card that many positions away from the current index i,
            // this.deck[i - deltaIndex]

            // and store that card in the predictedState at array index i.
            predictedState[i] = this.deck[i - (i - this.deck[i])];
        }

        return predictedState;
    }

    /**
     * Perform a random overhand shuffle using a break probability of 0.1.
     */
    public void randomShuffle() {
        double breakChance = 0.1;
        int blockSize = 0;
        int[] blocks = {};

        // Generate blocks, with breakChance chance of starting a new block.
        for (int i = 0; i < this.deck.length; i++) {
            blockSize++;

            if (Math.random() < breakChance) {
                blocks = Arrays.copyOf(blocks, blocks.length  + 1);
                blocks[blocks.length - 1] = blockSize;
                blockSize = 0;
            }
        }

        int blockSum = 0;
        // Check the block sum
        for (int block : blocks) {
            blockSum += block;
        }

        // If the block sum is less than the deck length, insert a block so
        // that the block sum will now be equal to that of the deck length.
        if (blockSum < this.deck.length) {
            blocks = Arrays.copyOf(blocks, blocks.length  + 1);
            blocks[blocks.length - 1] = deck.length - blockSum;
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
        if (unbrokenPairs < 0) return -1; // Unbroken pairs must be positive.
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
