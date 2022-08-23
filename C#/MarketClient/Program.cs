using System;

namespace MarketClient
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {

                using (Client client = new Client())
                {
                    Console.WriteLine("Successfully connected to the server.");
                    Console.WriteLine("Traders Connected: " + client.ShowAllTraders());

                    while (true)
                    {

                        if (client.customerID != int.Parse(client.CheckWhoHasTheStock())){
                            Console.WriteLine();
                            Console.WriteLine("Your ID: " + client.getID());
                            Console.WriteLine("Traders Connected: " + client.ShowAllTraders());
                            client.ShowTradersWhoLeft();
                            client.ShowNewTraders();
                            Console.WriteLine("[Enter Any Key] To Update");
                            string input = Console.ReadLine();
                        } else {
                            Console.WriteLine();
                            Console.WriteLine("You have the stock");
                            Console.WriteLine("Your ID: " + client.getID());
                            Console.WriteLine("Traders Connected " + client.ShowAllTraders());
                            client.ShowTradersWhoLeft();
                            client.ShowNewTraders();
                            Console.WriteLine("Give stock to: ");
                            try {
                                int input = int.Parse(Console.ReadLine());
                                Console.WriteLine(client.GiveStockTo(input));
                            } catch (Exception e){
                                Console.WriteLine("Invalid input - You did not type an integer");
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
