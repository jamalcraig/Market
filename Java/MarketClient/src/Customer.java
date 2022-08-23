import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Customer implements AutoCloseable {

    final int port = 8888;

    private final Scanner reader;
    private final PrintWriter writer;
    public int customerID;
    public int whoHasTheStock = -1;

    public Customer() throws Exception {
        // Connecting to the server and creating objects for communications
        Socket socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());

        // Automatically flushes the stream with every command
        writer = new PrintWriter(socket.getOutputStream(), true);

        // Parsing the response
        String line = reader.nextLine();
        customerID = Integer.parseInt(line);
        line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0)
            throw new Exception("Exception caused in constructor - " + line);

    }

    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }

    public String checkForUpdates(){
        writer.println("check_update");

        if (reader == null){
            System.out.println("Reader null");
        }

        try {
            if (reader.hasNext() && reader.hasNextLine()) {
                //System.out.println("has next");
                String l = reader.nextLine();
                return l;
            }
        } catch (IndexOutOfBoundsException ieee){
            System.out.println("Index out of bounds ex");
        }

        return "nothing in reader line";

        //return false;
    }

    public int getID(){
        writer.println("id");

        return Integer.parseInt(reader.nextLine());
    }

    List<String> listOfKnownTraders = new ArrayList<>();
    List<String> listOfNewTraders = new ArrayList<>();
    List<String> listOfTradersThatLeft = new ArrayList<>();
    public String ShowAllTraders(){
        writer.println("show_all_traders");
        String s = reader.nextLine();
        String[] updatedListofTraders = s.split(" ");
        for (int i = 0; i < updatedListofTraders.length; i++){
            if (!listOfKnownTraders.contains(updatedListofTraders[i])){
                listOfNewTraders.add(updatedListofTraders[i]);
            }
        }
        for (int i = 0; i < listOfKnownTraders.size(); i++){
            if (!s.contains(listOfKnownTraders.get(i))){
                listOfTradersThatLeft.add(listOfKnownTraders.get(i));
            }
        }
        listOfKnownTraders.clear();
        listOfKnownTraders.addAll(Arrays.asList(updatedListofTraders));
        return s;
    }

    public void ShowNewTraders(){
        if (listOfNewTraders.size() > 0){
            System.out.println("New Traders Joined " + listOfNewTraders);
        }
        listOfNewTraders.clear();
    }

    public void ShowTradersWhoLeft(){
        if (listOfTradersThatLeft.size() > 0){
            System.out.println("Traders that left " + listOfTradersThatLeft);
        }
        listOfTradersThatLeft.clear();
    }

    String whoHadTheStockLast = "";
    String nowWhoHasTheStock = "";
    public String CheckWhoHasTheStock(){
        writer.println("who_has_the_stock");
        String s = reader.nextLine();
        nowWhoHasTheStock = s;
        if (!whoHadTheStockLast.isEmpty() && !whoHadTheStockLast.equals(nowWhoHasTheStock)){
            System.out.println("Trader " + whoHadTheStockLast + " has given the stock to Trader " + nowWhoHasTheStock);
        }
        whoHadTheStockLast = s;

        return s;
    }

    public String GiveStockTo(int id){
        writer.println("give_stock_to " + id);
        return reader.nextLine();
    }

}
