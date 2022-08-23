using System;
using System.Collections.Generic;

namespace MarketServer
{
    public class Market
    {
        private int stockHolderID = -1;
        public List<int> connectedTraders = new List<int>();

        public bool GiveStockTo(int traderID){
            if (connectedTraders.Contains(traderID)){
                stockHolderID = traderID;
                Console.WriteLine("Share has been given to " + stockHolderID);
                return true;
            } else {
                Console.WriteLine("Cannot give stock to " + traderID);
                return false;
            }
        }

        public int GetStockHolderID() {
            return stockHolderID;
        }

        public void NoTradersLeft(){
            stockHolderID = -1;
            Console.WriteLine("No traders left - waiting to reassign the stock to the next customer that connects");
        }

        public void AddToConnectedTraders(int traderID){
            connectedTraders.Add(traderID);
        }

        public void RemoveFromConnectedTraders(int traderID){

            connectedTraders.Remove(traderID);
            Console.WriteLine("Trader " + traderID + " was removed from the connected traders list");
            if (stockHolderID == traderID){
                if (connectedTraders.Count > 1){
                    GiveStockTo(connectedTraders[connectedTraders.Count-1]);
                    Console.WriteLine("Stock was automatically given to " + connectedTraders[connectedTraders.Count-1]);
                } else {
                    NoTradersLeft();
                }
            }
        }
    }
}