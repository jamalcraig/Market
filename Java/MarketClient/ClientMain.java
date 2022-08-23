import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientMain {


    public static void main(String[] args) {

        try (Scanner in = new Scanner(System.in)) {
            try (Customer client = new Customer()) {
                System.out.println("Logged in successfully.");
                //System.out.println("Your ID: " + client.customerID);
                /*client.t();
                Runnable r1 = new UpdateThread(client);
                Thread updateThread = new Thread(r1);
                updateThread.start();
                //int i = 0;
                System.out.println("sdfsd");
                while (true) {
                    //i++;
                    //System.out.println(i);
                    int input = Integer.parseInt(in.nextLine());
                    switch (input){
                        case 1:
                            client.somebodyCheckedId();
                            break;
                        case 2:
                            client.hey();
                            break;
                        case 3:
                            client.hi();
                            break;
                        case 4:
                            client.checkForUpdates();
                            break;
                    }
                } */





                while (true) {
                    String serverInput = client.readServer();
                    String[] serverInputArray = serverInput.split(" ");
                    System.out.println(serverInputArray.length + " sia");
                    List<String> a = new ArrayList<>();
                    for (int i = 0; i < serverInputArray.length; i++){
                        a.add(serverInputArray[i]);
                    }
                    System.out.println(a.size() + " a");
                    for(String s : a){
                        System.out.println("tr" + s);
                    }
                    if(a.size() > 0) {
                        switch (a.get(0)) {
                            case "traders":
                                String str = "Traders connected: \n";
                                for (int i = 1; i < serverInputArray.length; i++) {
                                    str += "Trader " + a.get(i) + "\n";
                                }
                                System.out.println(str);
                                break;

                            default:
                                System.out.println(serverInputArray[0]);
                                break;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
