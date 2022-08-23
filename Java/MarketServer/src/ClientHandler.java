import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable{

    private final Socket socket;
    private final Market market;
    //private static int nextCustomerID = 0;
    private static AtomicInteger nextCustomerID = new AtomicInteger(0);
    private static Map<Integer, PrintWriter> clientMap = new HashMap<>();
    private Set<String> updateSet = new HashSet<>();

    public ClientHandler(Socket socket, Market market){
        this.socket = socket;
        this.market = market;
    }

    void showTraders(PrintWriter writer){
        writer.println(market.connectedCustomers.size() + " Traders are connected:");
        for (Integer i : market.connectedCustomers){
            writer.println("Trader " + i);
        }
        writer.println("========");
        showWhoHasTheStock(writer);
        writer.println("========");
    }

    void showWhoHasTheStock(PrintWriter writer){
        writer.println("Stock holder: Trader " + market.getStockHolderID());
    }

    @Override
    public void run() {

        int customerID = nextCustomerID.get();
        try (
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             ) {
            try {
                clientMap.put(customerID, writer);
                System.out.println("New connection; customer ID " + customerID);

                market.addToConnectedCustomers(customerID);
                if(market.getStockHolderID() == -1){
                    market.giveShareTo(customerID);
                }
                for (Integer i : clientMap.keySet()){
                    //clientMap.get(i).println("");
                    String s = "Traders connected: ";
                    for (Integer i2 : market.connectedCustomers){
                        //clientMap.get(i).println("Trader " + i2);
                        s += i2 + ", ";
                    }

                    //clientMap.get(i).println(s.substring(0, s.length()-2));
                }
                writer.println(customerID);
                writer.println("SUCCESS");

                nextCustomerID.getAndIncrement();

                /*
                // this just always sends back the connected traders
                // the client constantly reads this
                boolean f = true;
                while (f){
                    for (Integer i : clientMap.keySet()){
                        //clientMap.get(i).println("");
                        String s = "traders ";
                        for (Integer i2 : market.connectedCustomers){
                            //clientMap.get(i).println("Trader " + i2);
                            s += i2 + " ";
                        }

                        clientMap.get(i).println(s.trim());
                        System.out.println(s.trim());
                    }
                } */

                while (true) {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");
                    switch (substrings[0].toLowerCase()) {

                        case "traders":
                            showTraders(writer);
                            break;

                            //"give" isn't being used in my current implementation
                        case "give":
                            if (customerID == market.getStockHolderID()) {
                                if (substrings.length > 1) {
                                    int custID = Integer.parseInt(substrings[1]);
                                    if (market.giveShareTo(custID)) {
                                        writer.println("GAVE STOCK TO Customer " + custID);
                                        System.out.println("GAVE STOCK TO Customer " + custID);
                                    } else {
                                        writer.println("COULD NOT GIVE STOCK TO Customer " + custID + " - THEY ARE NOT CONNECTED");
                                        System.out.println("COULD NOT GIVE STOCK TO Customer " + custID + " - THEY ARE NOT CONNECTED");
                                    }
                                } else {
                                    writer.println("YOU DID NOT STATE WHO TO GIVE THE STOCK TO");
                                    System.out.println("YOU DID NOT STATE WHO TO GIVE THE STOCK TO");
                                }
                            } else {
                                writer.println("YOU DO NOT HAVE THE STOCK - TRANSFER FAILED");
                                System.out.println("YOU DO NOT HAVE THE STOCK - TRANSFER FAILED");
                            }

                            break;

                        case "id":
                            clientMap.get(customerID).println(customerID);
                            System.out.println("[id] Trader " + customerID + " requested their ID");
                            break;

                        case "show_all_traders":
                            String tradersConnected = "";
                            for (int i = 0;  i< market.connectedCustomers.size(); i++){
                                tradersConnected += market.connectedCustomers.get(i) + " ";
                            }
                            System.out.println("[show_all_traders] Output traders connected: " + tradersConnected);
                            writer.println(tradersConnected);
                            break;

                        case "who_has_the_stock":
                            writer.println(market.getStockHolderID());
                            System.out.println("[who_has_the_stock] Trader " + market.getStockHolderID() + " has the stock");
                            break;

                        case "give_stock_to":
                            if (market.giveShareTo(Integer.parseInt(substrings[1]))){
                                writer.println("Gave Stock To " + substrings[1]);
                                System.out.println("[give_stock_to] Trader " + customerID + " gave stock to trader " + substrings[1]);
                            } else {
                                writer.println("Could not give stock to " + substrings[1] + " - They are not connected");
                                System.out.println("Could not give stock to " + substrings[1] + " - They are not connected");
                            }
                            break;

                        case "check_update":
                            if (updateSet.size() > 0){
                                String s = "";
                                for (String u : updateSet){
                                    s += u + " ";
                                }
                                System.out.println("Writing out: '" + s.trim() + "'");
                                writer.println(s.trim());
                                updateSet.clear();
                            } else {
                                System.out.println("No update");
                                writer.println("No update");
                            }
                            break;

                        default:
                            System.out.println("Unknown command (" + substrings[0] +")");
                            throw new Exception("Unknown command: " + substrings[0]);
                    }

                }
            } catch (Exception e) {
                writer.println("ERROR on server side" + e.getMessage() + e.getStackTrace());
                //e.printStackTrace();
                market.removeFromConnectedCustomers(customerID);
                clientMap.remove(customerID);
                socket.close();
            }
        } catch (Exception e) {
        } finally {

            System.out.println("Trader " + customerID + " disconnected.");
        }
    }
}
