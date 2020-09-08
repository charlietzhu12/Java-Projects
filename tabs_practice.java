import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class TS_TabConverter {

    public static final int MIN_SPACES = 2;

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);
        header();
        Scanner input = null;
        PrintStream output = null;

        String infile = "";

        while (!infile.equalsIgnoreCase("q")) {
            System.out.print("\nEnter input filename or Q to quit: ");
            infile = console.next();
            if (!infile.equalsIgnoreCase("q")) {
                input = getInputScanner(infile);
                if (input != null) {
                    output = getOutputPrintStream(console);
                    if (output != null) {
                        System.out.print("Enter E-xpand or U-nexpand: ");
                        String option = console.next();
                        if (option.equalsIgnoreCase("E") || option.equalsIgnoreCase("U")) {
                            System.out.print("Enter number of spaces: ");
                            while (!console.hasNextInt()) {
                                System.out.println("Invalid value");
                                console.next();
                                System.out.print("Enter number of spaces: ");
                            }
                            int numberOfSpaces = console.nextInt();
                            if (numberOfSpaces < MIN_SPACES) {
                                numberOfSpaces = MIN_SPACES;
                            }

                            processFile(option.equalsIgnoreCase("E"), numberOfSpaces, input, output);
                        }
                        else {
                            System.out.println("Invalid option");
                        }
                    }
                }
            }
        }
    }

    public static void header() {
        System.out.println("\n    Welcome to the Tab Converter Program");

        System.out.println("You will be prompted for the name of a input file,");
        System.out.println("until you enter Q to quit. You will then be");
        System.out.println("prompted for the name of an output file, an option,");
        System.out.println("E-xpand or U-nexpand, and the number of spaces to");
        System.out.println("use in the expansion/unexpansion operation.");
    }



    //Returns Scanner for an input file
    //If the file does not exist, outputs "File does not exist" and returns null
    public static Scanner getInputScanner(String filename){
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e) {
            //System.out.println(e.getMessage());
            System.out.println("File does not exist");
        }
        return input;
    }

    //Prompts user to enter output filename and returns PrintStream for the file
    //If the file already exists, asks the user if it is OK to overwrite the file.
    //If the user's answer begins with y or Y, creates and returns a PrintStream for the file
    //If a FileNotFoundException occurs, outputs "Cannot create output file" and returns null
    //Returns null if it is not OK to overwrite the file
    public static PrintStream getOutputPrintStream(Scanner console){
        System.out.print("Enter output filename: ");
        String filename = console.next();
        PrintStream output = null;
        File f = new File(filename);
        if (f.exists()) {
            System.out.print(filename + " exists - OK to overwrite (y,n)?: ");
            String response = console.next();
            if (!response.toLowerCase().startsWith("y")) {
                return output;
            }
        }
        try {
                output = new PrintStream(f);
        }
        catch (FileNotFoundException e) {
            //System.out.println(e.getMessage());
            System.out.println("Cannot create output file");
        }
        return output;
    }

    //If expand is true, copies the input file to the output file replacing tabs by the given
    //number of spaces, as described above
    //If expand is false, copies the input file to the output file replacing the given number of
    //spaces with tabs, as described above
    //Throws an IllegalArgumentException with the message "Invalid number of spaces"
    //if numberOfSpaces is less than 2
    public static void processFile (boolean expand, int numberOfSpaces, Scanner input,
                                    PrintStream output){
        if (numberOfSpaces < MIN_SPACES) {
            throw new IllegalArgumentException("Invalid number of spaces");
        }
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (expand) {
                output.println(expandLine(line, numberOfSpaces));
            }
            else {
                output.println(unexpandLine(line, numberOfSpaces));
            }
        }

    }


    //Returns string containing expanded line
    //Throws an IllegalArgumentException with the message "Invalid number of spaces"
    //if numberOfSpaces is less than 2
    public static String expandLine(String line, int numberOfSpaces){
        if (numberOfSpaces < MIN_SPACES) {
            throw new IllegalArgumentException("Invalid number of spaces");
        }
        String s = "";
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\t') {
                for (int j = 1; j <= numberOfSpaces; j++) {
                    s += " ";
                }
            }
            else {
                s += line.charAt(i);
            }
        }
        return s;
    }


    //Returns string containing unexpanded line
    //Throws an IllegalArgumentException with the message "Invalid number of spaces"
    //if numberOfSpaces is less than 2
    public static String unexpandLine ( String line, int numberOfSpaces ) {
        if ( numberOfSpaces < MIN_SPACES ) {
            throw new IllegalArgumentException( "Invalid number of spaces" );
        }
        String s = "";
        for ( int i = 0; i < line.length(); i++ ) {
            if ( line.charAt( i ) != ' ' ) {
                s += line.charAt( i );
            }
            else {
                int count = 0;
                boolean spaces = true;
                for ( int j = i; j < line.length() && spaces; j++ ) {
                    if ( line.charAt( j ) == ' ' ) {
                        count++;
                    }
                    else {
                        spaces = false;
                    }
                }
                if ( count >= numberOfSpaces ) {
                    s += '\t';
                    i += numberOfSpaces - 1;
                }
                else {
                    s += ' ';
                }
            }
        }
        return s;
    }
}
