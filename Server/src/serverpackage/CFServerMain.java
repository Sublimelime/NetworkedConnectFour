package serverpackage;

public class CFServerMain {

    public static void main(String[] args) {
        System.out.println("Running the Server.");

        /*int numberOfPlayers = 0;
        System.out.println("-Graphical Connect Four Game-");

        do {
            System.out.println("How many players are going to play? (1 or 2):");
            numberOfPlayers = keyboard.nextInt();

            if (numberOfPlayers < 1 || numberOfPlayers > 2) {
                System.out.println("Error Invalid, number of players. Please try again");
            }
        } while (numberOfPlayers < 1 || numberOfPlayers > 2);
         */
        new CFServerFrame(2); //always two players
    }
}
