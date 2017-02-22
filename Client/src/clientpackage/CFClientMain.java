package clientpackage;

public class CFClientMain {

    public static void main(String[] args) {
        System.out.println("Running the Client.");

        System.out.println("-Graphical Connect Four Game-");
        /*
        do {
            System.out.println("How many players are going to play? (1 or 2):");
            numberOfPlayers = keyboard.nextInt();

            if (numberOfPlayers < 1 || numberOfPlayers > 2) {
                System.out.println("Error Invalid, number of players. Please try again");
            }
        } while (numberOfPlayers < 1 || numberOfPlayers > 2);
         */

        new CFClientFrame(2);
    }
}
