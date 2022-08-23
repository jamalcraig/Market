import java.util.Scanner;

public class CustomerProgram {

    public static void main(String[] args) {

        try {
            Scanner in = new Scanner(System.in);

            try (Customer client = new Customer()) {
                System.out.println("Logged in successfully.");
                System.out.println("Traders Connected " + client.ShowAllTraders());
                client.ShowNewTraders();

                while (true) {

                    if (client.customerID != Integer.parseInt(client.CheckWhoHasTheStock())) {
                        System.out.println();
                        System.out.println("Your ID: " + client.getID());
                        System.out.println("Traders Connected " + client.ShowAllTraders());
                        client.ShowTradersWhoLeft();
                        client.ShowNewTraders();
                        System.out.println("[Press Any Key] To Update");
                        String input = in.nextLine();

                    } else {
                        System.out.println();
                        System.out.println("You have the stock");
                        System.out.println("Your ID: " + client.getID());
                        System.out.println("Traders Connected " + client.ShowAllTraders());
                        client.ShowNewTraders();
                        client.ShowTradersWhoLeft();
                        System.out.println("Give stock to: ");
                        try {
                            int input = Integer.parseInt(in.nextLine());
                            System.out.println(client.GiveStockTo(input));
                        } catch (NumberFormatException e){
                            System.out.println("Invalid input - You did not type an integer");
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
