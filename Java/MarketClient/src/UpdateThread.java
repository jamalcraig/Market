public class UpdateThread implements Runnable{

    Customer client;
    public UpdateThread(Customer client){
        this.client = client;
    }
    @Override
    public void run() {
        try {
            while (true) {
                String c = client.checkForUpdates();
                //if (!c.equals("No update")) {
                    System.out.println(c);
                //}
                Thread.sleep(3000);
            }
        } catch (Exception ie){
            System.out.println("Thread died");
            ie.printStackTrace();
        }
    }
}
