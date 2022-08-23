using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

namespace MarketServer
{
    class Program
    {
        private readonly int port = 8888;

        private static readonly Market market = new Market();
        private static int nextCustomerID = 0;
        private static Dictionary<int, StreamWriter> clientMap = new Dictionary<int, StreamWriter>();

        static void Main(string[] args)
        {

            RunServer();
        }

        private static void RunServer()
        {
            TcpListener listener = new TcpListener(IPAddress.Loopback, port);
            listener.Start();
            Console.WriteLine("Waiting for incoming connections...");
            while (true)
            {
                TcpClient tcpClient = listener.AcceptTcpClient();
                new Thread(HandleIncomingConnection).Start(tcpClient);
            }
        }

        private static void HandleIncomingConnection(object param)
        {
            TcpClient tcpClient = (TcpClient) param;
            using (Stream stream = tcpClient.GetStream())
            {
                StreamWriter writer = new StreamWriter(stream);
                StreamReader reader = new StreamReader(stream);
                int customerId = nextCustomerID;
                try
                {
                    clientMap.Add(customerId, writer);
                    Console.WriteLine($"New connection; customer ID {customerId}");
                    market.AddToConnectedTraders(customerId);
                    if (market.GetStockHolderID() == -1){
                        market.GiveStockTo(customerId);
                    }
                    
                    writer.WriteLine(customerId);
                    writer.Flush();
                    writer.WriteLine("SUCCESS");
                    writer.Flush();

                    
                    Interlocked.Increment(ref nextCustomerID);

                    while (true)
                    {
                        string line = reader.ReadLine();
                        string[] substrings = line.Split(' ');
                        switch (substrings[0].ToLower())
                        {

                            case "show_all_traders":
                                string tradersConnected = "";
                                for (int i = 0; i < market.connectedTraders.Count; i++){
                                    tradersConnected += market.connectedTraders[i] + " ";
                                }
                                Console.WriteLine("[show_all_traders] Output Traders Connected: " + tradersConnected);
                                writer.WriteLine(tradersConnected);
                                writer.Flush();
                                break;

                            case "who_has_the_stock":
                                writer.WriteLine(market.GetStockHolderID());
                                writer.Flush();
                                Console.WriteLine("[who_has_the_stock] Trader " + market.GetStockHolderID() + " has the stock");
                                break;

                            case "give_stock_to":
                                if (market.GiveStockTo(int.Parse(substrings[1]))){
                                    writer.WriteLine("Gave Stock To " + substrings[1]);
                                    writer.Flush();
                                    Console.WriteLine("[give_stock_to] Gave Stock To " + substrings[1]);
                                } else {
                                    writer.WriteLine("Could Not Give Stock To " + substrings[1] + " - They are not connected");
                                    writer.Flush();
                                    Console.WriteLine("[give_stock_to] Could Not Give Stock To " + substrings[1] + " - They are not connected");
                                    
                                }
                                break;

                            case "id":
                                clientMap[customerId].WriteLine(customerId);
                                clientMap[customerId].Flush();
                                Console.WriteLine("[id] Trader " + customerId + " requested their ID");
                                break;


                            default:
                                throw new Exception($"Unknown command: {substrings[0]}.");
                        }
                    }
                }
                catch (Exception e)
                {
                    try
                    {
                        writer.WriteLine("ERROR on Server Side " + e.Message);
                        writer.Flush();
                        market.RemoveFromConnectedTraders(customerId);
                        clientMap.Remove(customerId);
                        tcpClient.Close();
                    }
                    catch
                    {
                        Console.WriteLine("Failed to send error message.");
                    }
                }
                finally
                {
                    Console.WriteLine($"Customer {customerId} disconnected.");
                }
            }
        }
    }
}
