package week09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Overhand application entry point.
 * <br />
 * Created on 17/04/2017.
 * @author Anthony
 */
public class OverhandApp {
    /** Instance of OverhandShuffler */
    private static OverhandShuffler app = new OverhandShuffler();

    /**
     * Entry point for the application.
     * Reads commands from the command line and executes
     * the appropriate command(s).
     * @param args The commands from the command line.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            handleCommand(input.nextLine());
        }
    }

    /**
     * Get ints from input stream.
     * @param input The scanner input stream.
     * @return An array of ints.
     */
    private static int[] getNums(Scanner input) {
        List<Integer> numbersList= new ArrayList<>();
        while (input.hasNextInt()) {
            numbersList.add(input.nextInt());
        }
        int[] numbers = new int[numbersList.size()];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = numbersList.get(i);
        }
        return numbers;
    }

    /**
     * Handles commands entered from command line.
     * @param input The input stream.
     */
    public static void handleCommand(String input) {
        Scanner scan = new Scanner(input);
        if (scan.hasNext()) {
            String command = scan.next();
            switch (command) {
                case "make-new": case "m": {
                        int[] args = getNums(scan);

                        if (args.length < 1) {
                            System.out.println("Command 'make-new' is missing its arguments.");
                            break;
                        }

                    app.makeNew(args[0]);
                } break;

                case "print": case "p":
                    System.out.println(app.toString());
                    break;

                case "shuffle": case "s":
                    try {
                        app.shuffle(getNums(scan));
                    } catch (BlockSizeException e) {
                        System.out.println(e.getMessage());
                    } break;

                case "order": case "o":
                    try {
                        System.out.println(app.order(getNums(scan)));
                    } catch (BlockSizeException e) {
                        System.out.println(e.getMessage());
                    } break;

                case "unbroken-pairs": case "u":
                    System.out.println(app.unbrokenPairs());
                    break;

                case "random-shuffle": case "r":
                    app.randomShuffle();
                    break;

                case "count-shuffles": case "c": {
                    int[] args = getNums(scan);

                        if (args.length < 1) {
                            System.out.println("Command 'count-shuffles' is missing its arguments.");
                            break;
                        }

                    System.out.println(app.countShuffles(args[0]));
                } break;

                case "load": case "l": {
                        ArrayList<String> args = new ArrayList<>();
                        while (scan.hasNext()) {
                            args.add(scan.next());
                        }
                        if (args.size() < 1) {
                            System.out.println("Command 'load' is missing its arguments.");
                        }
                        app.load(args.toArray(new String[0]));
                    } break;

                case "try-repeat": case "t":
                    System.out.println(Arrays.toString(app.tryRepeat()));
                    break;

                case "help": case "h": case "?":
                    System.out.println(
                              "Command         Arguments       Description                                      \n"
                            + "make-new        18              Make a new ordered deck from 0 to 17             \n"
                            + "print                           Print a string representation of the current deck\n"
                            + "shuffle         2 3 10 3        Call shuffle([2,3,10,3])                         \n"
                            + "order           1 4 11 2        Print the result of calling order([1,4,11,2])    \n"
                            + "unbroken-pairs                  Print result of calling unbrokenPairs()          \n"
                            + "random-shuffle                  Call randomShuffle() on the current deck         \n"
                            + "count-shuffles  15              Print the result of countShuffles(15)            \n"
                            + "load            4 3 1 0 2 5     Load deck with given numbers (no error checking) \n"
                            + "try-repeat                      Print the next state of the deck if the same     \n"
                            + "                                sequence of shuffles were repeated on this deck  \n");
                    break;

                default:
                    System.out.println("Unsupported command '" + command + "'.");
            }
        }
    }
}
