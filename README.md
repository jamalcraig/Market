# Market

A socket-based client-server system to trade a single stock in a simple stock market.  
Each client application is a trader.  
Once a trader obtains the stock, it prompts the user whom to give it to.  
The clients can connect and disconnect from the server at any time.
The client and server applications been implemented in Java and C#. The applications work seamlessly across both language.

## Config
The projects for both the Java and C# implementations have the same name. They have been separated by being in folders that group the language's implementations  

### Java in intellij  
To run the server, in the MarketServer project, run the MarketServerProgram class  
To run the client with console input, in the MarketClient project of the Java folder, run the CustomerProgram class  
To run the client with a GUI, in the MarketClient project of the Java folder, run the CustomerProgramWithGui class  

### C# in VSCode  
To run the server, in the MarketServer project of the C# folder, run Program.cs  
To run the client, in the MarketClient project of the C# folder, run Program.cs  

## Protocol
The client connects to the server through a socket. Once the client has connected, the server creates a reader and writer stream that will be used to communicate with the client. The client is then assigned a unique ID. On the server, the client is added to a map of clients with its corresponding writer stream as the value, and the client is added to a list of connected clients in the market. If there isn’t a client already connected, the client is given the stock. The server parses back the unique ID to the client. The unique ID is an atomic integer, so it is incremented once the ID has been assigned to a client. The server then enters an infinite loop, where it waits to receive a command from the client, and then it processes the command.  
  
The client constructor creates a reader and writer stream that is used to communicate with the server. Using the reader stream, the client retrieves from the server the unique ID that it has been assigned.  

Upon connection to the server, the client displays all the traders connected to the server.  
The client does this by sending the “show_all_traders” command to the server, then the server outputs a string concatenation of the all the IDs of the clients/traders connected to the server. After this exchange of data, the client’s local list of known connected traders, new connected trader, and traders that left are updated.  
  
The client enters an infinite loop where it will show their ID, the current traders connected, the new connected traders, and the traders that left. The client user can then press any key to check if there are any updates in the data.  
If the client has the stock, then the user will be asked to enter the ID of the trader they want to pass the stock to. The ID they enter will be sent to the server, then the server will assign the stock to the entered ID, if they are connected to the server. The server then sends back to the client whether the command was successful or not.  

### Client Threads  
The client only has one thread running. It is the whole interface for the process. It displays all the necessary information and asks the user to give inputs to update the interface or to pass the stock to another trader. The thread is created when the main is ran. The thread is terminated when the client disconnects from the server, or when an unhandled exception has been thrown.  

### Server Threads  
The server has a main thread, and a thread for each client that connects to the server. The main thread is what is used to run the server. Each client that connects to the server has its own thread. In these client threads, the communication from the client is handled. The thread is terminated when the client disconnects from the server, which can also be caused by an unhandled exception being thrown, like an incorrect command being called. If the main thread is terminated, then all the threads for the clients connected will be terminated too.  
