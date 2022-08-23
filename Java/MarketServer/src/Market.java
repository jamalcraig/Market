import java.util.ArrayList;
import java.util.List;

public class Market {

    private int shareHolderID = -1;
    public List<Integer> connectedCustomers = new ArrayList<>();

    public boolean giveShareTo(int customerID){
        if (connectedCustomers.contains(customerID)){
            shareHolderID = customerID;
            System.out.println("Stock has been given to Trader " + shareHolderID);
            return true;
        } else {
            System.out.println("Cannot give stock to Trader " + customerID + " because they are not connected");
            return false;
        }
    }
    public int getStockHolderID() {
        return shareHolderID;
    }

    public void noTradersLeft(){
        shareHolderID = -1;
    }

    public void addToConnectedCustomers(int customerID){
        connectedCustomers.add(customerID);
    }

    public void removeFromConnectedCustomers(int customerID){
        System.out.println("The current assigned stockholder is " + shareHolderID);
        connectedCustomers.remove(Integer.valueOf(customerID));
        System.out.println("Removed Trader " + customerID + " from list of connected traders");
        if (shareHolderID == customerID){
            if (connectedCustomers.size() > 1) {
                giveShareTo(connectedCustomers.get(connectedCustomers.size()-1));
                System.out.println("Stockholder (Trader " + customerID + ") disconnected, so stock was automatically given to Trader " + connectedCustomers.get(connectedCustomers.size()-1));
            } else {
                noTradersLeft();
            }
        }

    }


}
