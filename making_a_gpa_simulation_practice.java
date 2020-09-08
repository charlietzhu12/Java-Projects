import java.util.Scanner;

public class TS_Admissions {

    public static final int    MIN_SAT            = 200;
    public static final int    MAX_SAT            = 800;

    public static final int    MIN_ESSAY          = 2;
    public static final int    MAX_ESSAY          = 8;

    public static final double MIN_GPA            = 0.0;

    public static final int    ENG_MATH_MIN       = 500;
    public static final int    ENG_MATH_ADMIT     = 700;
    public static final int    ENG_READING_MIN    = 400;
    public static final int    ENG_READING_ADMIT  = 600;
    public static final double ENG_GPA_MIN        = 3.8;

    public static final int    JOUR_READING_MIN   = 400;
    public static final int    JOUR_READING_ADMIT = 600;
    public static final int    JOUR_ESSAY_MIN     = 6;
    public static final int    JOUR_ESSAY_ADMIT   = 7;
    public static final double JOUR_GPA_MIN       = 3.0;

    public static final double FA_GPA_MIN         = 2.8;

    public static final int    MIN_ALUMNI         = 2;

    public static void main ( String[] args ) {

        System.out.println( "\n           Welcome to the College Admissions Program!" );
        System.out.println( "When prompted, please enter the applicant's name, and the school" );
        System.out.println( "to which he/she is applying  -  E (Engineering), J (Journalism)," );
        System.out.println( "or F (Fine Arts). Depending on the school, you will be prompted" );
        System.out.println( "to enter SAT scores (Math, Reading/Writing, Essay),  high school " );
        System.out.println( "GPA (weighted/unweighted), or a Portfolio rating - E (Excellent)," );
        System.out.println( "G (Good), F (Fair), P (Poor). You will also be prompted to enter " );
        System.out.println( "the number of alumni family members. The applicant's admission " );
        System.out.println( "status of Admit, Defer, or Deny will then be displayed." );

        Scanner console = new Scanner( System.in );
        System.out.print( "\nApplicant Name: " );
        String name = console.nextLine();

        System.out.print( "E (Engineering), J (Journalism), or F (Fine Arts): " );
        String schoolName = console.next();
        char school = schoolName.charAt( 0 );
        if ( schoolName.length() > 1 || school != 'E' && school != 'e' && school != 'J' && school != 'j'
                && school != 'F' && school != 'f' ) {
            System.out.println( "Invalid school" );
            System.exit( 1 );
        }

        if ( school == 'E' || school == 'e' ) {
            System.out.print( "Math SAT score (200-800): " );
            int mathSAT = console.nextInt();
            if ( mathSAT < MIN_SAT || mathSAT > MAX_SAT ) {
                System.out.println( "Invalid SAT score" );
                System.exit( 1 );
            }
            System.out.print( "Reading/Writing SAT score (200-800): " );
            int readingWritingSAT = console.nextInt();
            if ( readingWritingSAT < MIN_SAT || readingWritingSAT > MAX_SAT ) {
                System.out.println( "Invalid SAT score" );
                System.exit( 1 );
            }
            System.out.print( "Weighted high school GPA: " );
            double gpa = console.nextDouble();
            if ( gpa < 0 ) {
                System.out.println( "Invalid GPA" );
                System.exit( 1 );
            }
            System.out.print( "Number of alumni family members: " );
            int numberOfAlumni = console.nextInt();
            if ( numberOfAlumni < 0 ) {
                System.out.println( "Invalid number of alumni" );
                System.exit( 1 );
            }
            String status = getEngineeringAdmissionStatus( mathSAT, readingWritingSAT, gpa, numberOfAlumni );
            System.out.println( "\nAdmission Status: " + status + "\n" );
        }

        if ( school == 'J' || school == 'j' ) {
            System.out.print( "Reading/Writing SAT score (200-800): " );
            int readingWritingSAT = console.nextInt();
            if ( readingWritingSAT < MIN_SAT || readingWritingSAT > MAX_SAT ) {
                System.out.println( "Invalid SAT score" );
                System.exit( 1 );
            }
            System.out.print( "SAT Essay Writing score (2-8): " );
            int essayWritingSAT = console.nextInt();
            if ( essayWritingSAT < MIN_ESSAY || essayWritingSAT > MAX_ESSAY ) {
                System.out.println( "Invalid SAT essay score" );
                System.exit( 1 );
            }
            System.out.print( "Unweighted high school GPA: " );
            double gpa = console.nextDouble();
            if ( gpa < 0 ) {
                System.out.println( "Invalid GPA" );
                System.exit( 1 );
            }
            System.out.print( "Number of alumni family members: " );
            int numberOfAlumni = console.nextInt();
            if ( numberOfAlumni < 0 ) {
                System.out.println( "Invalid number of alumni" );
                System.exit( 1 );
            }
            String status = getJournalismAdmissionStatus( readingWritingSAT, essayWritingSAT, gpa, numberOfAlumni );
            System.out.println( "\nAdmission Status: " + status + "\n" );
        }

        if ( school == 'F' || school == 'f' ) {

            System.out.print( "Portfolio rating (E (Excellent), G (Good), F (Fair), or P (Poor)): " );
            String rating = console.next();
            char portfolioRating = rating.charAt( 0 );
            if ( rating.length() > 1 || portfolioRating != 'E' && portfolioRating != 'e' && portfolioRating != 'G'
                    && portfolioRating != 'g' && portfolioRating != 'F' && portfolioRating != 'f'
                    && portfolioRating != 'P' && portfolioRating != 'p' ) {
                System.out.println( "Invalid portfolio rating" );
                System.exit( 1 );
            }
            System.out.print( "Unweighted high school GPA: " );
            double gpa = console.nextDouble();
            if ( gpa < 0 ) {
                System.out.println( "Invalid GPA" );
                System.exit( 1 );
            }
            System.out.print( "Number of alumni family members: " );
            int numberOfAlumni = console.nextInt();
            if ( numberOfAlumni < 0 ) {
                System.out.println( "Invalid number of alumni" );
                System.exit( 1 );
            }
            String status = getFineArtsAdmissionStatus( portfolioRating, gpa, numberOfAlumni );
            System.out.println( "\nAdmission Status: " + status + "\n" );
        }

    }

    // Determines and returns the engineering school admission status (Deny,
    // Defer, Admit)
    // based on the criteria listed above
    // Throws an IllegalArgumentException with the message "Invalid SAT score"
    // if
    // mathSAT or readingWritingSAT is less than 200 or greater than 800
    // Throws an IllegalArgumentException with the message "Invalid GPA" if
    // gpa is less than 0
    // Throws an IllegalArgumentException with the message "Invalid number of
    // alumni" if
    // numberOfAlumni is less than 0
    public static String getEngineeringAdmissionStatus ( int mathSAT, int readingWritingSAT, double gpa,
            int numberOfAlumni ) {
        if ( mathSAT < MIN_SAT || mathSAT > MAX_SAT ) {
            throw new IllegalArgumentException( "Invalid SAT score" );
        }
        if ( readingWritingSAT < MIN_SAT || readingWritingSAT > MAX_SAT ) {
            throw new IllegalArgumentException( "Invalid SAT score" );
        }
        if ( gpa < MIN_GPA ) {
            throw new IllegalArgumentException( "Invalid GPA" );
        }
        if ( numberOfAlumni < 0 ) {
            throw new IllegalArgumentException( "Invalid number of alumni" );
        }
        String status = "Defer";
        if ( mathSAT < ENG_MATH_MIN || readingWritingSAT < ENG_READING_MIN || gpa < ENG_GPA_MIN ) {
            status = "Deny";
        }
        else if ( mathSAT >= ENG_MATH_ADMIT && readingWritingSAT >= ENG_READING_ADMIT ) {
            status = "Admit";
        }
        if ( status.equals( "Defer" ) && numberOfAlumni >= MIN_ALUMNI ) {
            status = "Admit";
        }
        return status;
    }

    // Determines and returns the journalism school admission status (Deny,
    // Defer, Admit)
    // based on the criteria listed above
    // Throws an IllegalArgumentException with the message "Invalid SAT score"
    // if
    // readingWritingSAT is less than 200 or greater than 800
    // Throws an IllegalArgumentException with the message "Invalid SAT essay
    // score" if
    // essayWritingSAT is less than 2 or greater than 8
    // Throws an IllegalArgumentException with the message "Invalid GPA" if
    // gpa is less than 0
    // Throws an IllegalArgumentException with the message "Invalid number of
    // alumni" if
    // numberOfAlumni is less than 0
    public static String getJournalismAdmissionStatus ( int readingWritingSAT, int essayWritingSAT, double gpa,
            int numberOfAlumni ) {
        if ( readingWritingSAT < MIN_SAT || readingWritingSAT > MAX_SAT ) {
            throw new IllegalArgumentException( "Invalid SAT score" );
        }
        if ( essayWritingSAT < MIN_ESSAY || essayWritingSAT > MAX_ESSAY ) {
            throw new IllegalArgumentException( "Invalid SAT essay score" );
        }
        if ( gpa < MIN_GPA ) {
            throw new IllegalArgumentException( "Invalid GPA" );
        }
        if ( numberOfAlumni < 0 ) {
            throw new IllegalArgumentException( "Invalid number of alumni" );
        }
        String status = "Defer";
        if ( readingWritingSAT < JOUR_READING_MIN || essayWritingSAT < JOUR_ESSAY_MIN || gpa < JOUR_GPA_MIN ) {
            status = "Deny";
        }
        else if ( readingWritingSAT >= JOUR_READING_ADMIT && essayWritingSAT >= JOUR_ESSAY_ADMIT ) {
            status = "Admit";
        }
        if ( status.equals( "Defer" ) && numberOfAlumni >= MIN_ALUMNI ) {
            status = "Admit";
        }
        return status;
    }

    // Determines and returns the fine arts school admission status (Deny,
    // Defer, Admit)
    // based on the criteria listed above
    // Throws an IllegalArumentException with the message "Invalid portfolio
    // rating" if
    // portfolioRating is not E, e, G, g, F, f, P, or p
    // Throws an IllegalArgumentException with the message "Invalid GPA" if
    // gpa is less than 0
    // Throws an IllegalArgumentException with the message "Invalid number of
    // alumni" if
    // numberOfAlumni is less than 0
    public static String getFineArtsAdmissionStatus ( char portfolioRating, double gpa, int numberOfAlumni ) {
        if ( portfolioRating != 'E' && portfolioRating != 'e' && portfolioRating != 'G' && portfolioRating != 'g'
                && portfolioRating != 'F' && portfolioRating != 'f' && portfolioRating != 'P'
                && portfolioRating != 'p' ) {
            throw new IllegalArgumentException( "Invalid portfolio rating" );
        }
        if ( gpa < MIN_GPA ) {
            throw new IllegalArgumentException( "Invalid GPA" );
        }
        if ( numberOfAlumni < 0 ) {
            throw new IllegalArgumentException( "Invalid number of alumni" );
        }
        String status = "Defer";
        if ( portfolioRating == 'P' || portfolioRating == 'p' || portfolioRating == 'F' || portfolioRating == 'f'
                || gpa < FA_GPA_MIN ) {
            status = "Deny";
        }
        else if ( portfolioRating == 'E' || portfolioRating == 'e' ) {
            status = "Admit";
        }
        if ( status.equals( "Defer" ) && numberOfAlumni >= MIN_ALUMNI ) {
            status = "Admit";
        }
        return status;
    }
}
