using System;
using System.IO;
using System.Net.Sockets;
using System.Collections.Generic;

namespace MarketClient
{
    public class Client : IDisposable
    {

        const int port = 8888;

        private readonly StreamReader reader;
        private readonly StreamWriter writer;

        public int customerID;
        public int whoHasTheStock = -1;
        public Client(){
            TcpClient tcpClient = new TcpClient("localhost", port);
            NetworkStream stream = tcpClient.GetStream();
            reader = new StreamReader(stream);
            writer = new StreamWriter(stream);

            // Sending customer ID
            //writer.WriteLine(customerId);
            //writer.Flush();

            string line = reader.ReadLine();
            customerID = int.Parse(line);

            // Parsing the response
            line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                throw new Exception(line);
        }

        public int getID(){
            writer.WriteLine("id");
            writer.Flush();
            return int.Parse(reader.ReadLine());
        }

        List<string> listOfKnownTraders = new List<string>();
        List<string> listOfNewTraders = new List<string>();
        List<string> listOfTradersThatLeft = new List<string>();
        public string ShowAllTraders(){
            writer.WriteLine("show_all_traders");
            writer.Flush();
            string s = reader.ReadLine();
            string[] updatedListofTraders = s.Split(" ");
            for (int i = 0; i < updatedListofTraders.Length; i++){
                if (!listOfKnownTraders.Contains(updatedListofTraders[i])){
                    listOfNewTraders.Add(updatedListofTraders[i]);
                }
            }
            for (int i = 0; i < listOfKnownTraders.Count; i++){
                if (!s.Contains(listOfKnownTraders[i])){
                    listOfTradersThatLeft.Add(listOfKnownTraders[i]);
                }
            }
            listOfKnownTraders.Clear();
            for (int i = 0; i < updatedListofTraders.Length; i++){
                listOfKnownTraders.Add(updatedListofTraders[i]);
            }
            return s;
        }

        public void ShowNewTraders(){
            if (listOfNewTraders.Count > 0){
                string s = "(";
                for (int i = 0; i < listOfNewTraders.Count; i++){
                    s += listOfNewTraders[i] + ", ";
                }
                s = s.Substring(0, s.Length - 2);
                s += ")";

                Console.WriteLine("New Traders Joined: " + s);
            }
            listOfNewTraders.Clear();
        }

        public void ShowTradersWhoLeft(){
            if (listOfTradersThatLeft.Count > 0){
                
                string s = "(";
                for (int i = 0; i < listOfTradersThatLeft.Count; i++){
                    s += listOfTradersThatLeft[i] + ", ";
                }
                s = s.Substring(0, s.Length - 2);
                s += ")";
                Console.WriteLine("Traders that left " + s);
            }
            listOfTradersThatLeft.Clear();
        }

        String whoHadTheStockLast = "";
        String nowWhoHasTheStock = "";
        public String CheckWhoHasTheStock(){
            writer.WriteLine("who_has_the_stock");
            writer.Flush();
            String s = reader.ReadLine();;
            nowWhoHasTheStock = s;
            if (whoHadTheStockLast.Length > 0  && whoHadTheStockLast != nowWhoHasTheStock){
                Console.WriteLine("Trader " + whoHadTheStockLast + " has given the stock to Trader " + nowWhoHasTheStock);
            }
            whoHadTheStockLast = s;

            return s;
        }

        public String GiveStockTo(int id){
            writer.WriteLine("give_stock_to " + id);
            writer.Flush();
            return reader.ReadLine();
        }

        public void Dispose()
        {
            reader.Close();
            writer.Close();
        }
    }
}