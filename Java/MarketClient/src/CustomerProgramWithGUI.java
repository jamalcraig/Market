import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class CustomerProgramWithGUI extends JFrame {

    public boolean doUpdate = false;
    public JTextField jtf = new JTextField(10);
    public JButton submitButton = new JButton("Submit");
    public JButton updateButton = new JButton("Update");

    JLabel tradersConnected = new JLabel("Traders connected ");

    public CustomerProgramWithGUI(){


        //Customer client = connectToServer();
        try {
            Scanner in = new Scanner(System.in);
            try (Customer client = new Customer()) {
                System.out.println("Logged in successfully.");
                System.out.println("Traders Connected " + client.ShowAllTraders());
                client.ShowNewTraders();

                setLayout(new BorderLayout());
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                GridBagLayout gbl = new GridBagLayout();

                GridBagConstraints gbcTradersConnected = new GridBagConstraints();
                GridBagConstraints gbcJTextField = new GridBagConstraints();
                GridBagConstraints gbcSubmitButton = new GridBagConstraints();
                GridBagConstraints gbcUpdateButton = new GridBagConstraints();

                JPanel jp = new JPanel(gbl);

                gbcTradersConnected.gridy = 0;
                gbcJTextField.gridy = 1;
                gbcSubmitButton.gridy = 1;
                gbcUpdateButton.gridy = 1;


                jp.add(tradersConnected, gbcTradersConnected);
                jp.add(jtf, gbcJTextField);
                jp.add(submitButton, gbcSubmitButton);
                submitButton.setVisible(false);
                jp.add(updateButton, gbcUpdateButton);
                add(jp);
                tradersConnected.setText("Traders Connected: " + client.ShowAllTraders());
                System.out.println(client.ShowAllTraders());


                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //DoAllUpdates(client, tradersConnected);
                        //tradersConnected.setText("" + client.ShowAllTraders());
                        jtf.setText("");

                        updateButton.setVisible(true);
                        jtf.setVisible(false);
                        submitButton.setVisible(false);
                        doUpdate = true;
                        try {
                            tradersConnected.setText(client.ShowAllTraders());
                        } catch (Exception ee){

                        }
                        //System.out.println(client.ShowAllTraders());
                    }
                });
                //submitButton.addActionListener(new Act(client));

                updateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        submitButton.setVisible(true);
                        jtf.setVisible(true);
                        updateButton.setVisible(false);
                    }
                });

//                while (true){
//                    if (doUpdate){
//                        DoAllUpdates(client, tradersConnected);
//                        doUpdate = false;
//                    }
//                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }


        } catch (Exception e){

        }


        setSize(800, 300);
        setVisible(true);
    }

    class Act implements ActionListener {
        Customer client;
        public Act (Customer client){
            this.client = client;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            client.ShowAllTraders();
        }
    }

    public static Customer connectToServer(){
        try (Customer client = new Customer()) {
            System.out.println("Logged in successfully.");
            System.out.println("Traders Connected " + client.ShowAllTraders());
            client.ShowNewTraders();
            return client;
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String ShowAllTraders(Customer client){
        String s = client.ShowAllTraders();
        System.out.println("Traders Connected " + s);
        return s;
    }

    public static void DoAllUpdates(Customer client, JLabel showAllTradersLabel){
        showAllTradersLabel.setText(ShowAllTraders(client));
    }

    public static void main(String[] args) {

        new CustomerProgramWithGUI();
        /*
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

         */
    }
}
