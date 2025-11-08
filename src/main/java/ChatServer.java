import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


class ClientHandler {
    public Socket clientSocket;
    public BufferedReader in;
    public PrintWriter out;

    public String username;
    public String color;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }
}

public class ChatServer {
    private ServerSocket serverSocket;

    // A thread-safe list of clients
    private final Set<ClientHandler> activeClients = Collections.synchronizedSet(new HashSet<ClientHandler>());
    private final HashMap<String, ClientHandler> clientNames = new HashMap<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf("Server listening on port %d...\n", port);

        for (; ; ) {
            Socket clientSocket = serverSocket.accept();
            System.out.printf("Accepted connection from %s\n", clientSocket.getRemoteSocketAddress());


            ClientHandler handler = new ClientHandler(clientSocket);
            activeClients.add(handler);

            // Run the code to handle a new client on a new thread
            Thread t = new Thread(()->
            {
                try
                {
                    handleClient(handler);
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
            });
            t.start();


        }
    }

    // Safely terminate all connections with clients
    public void stop() throws IOException
    {
        synchronized (activeClients)
        {
            for(ClientHandler c : activeClients)
            {
                removeClient(c);
                activeClients.remove(c);
            }
        }
    }


    private void handleClient(ClientHandler h) throws IOException {
        h.in = new BufferedReader(new InputStreamReader(h.clientSocket.getInputStream()));
        h.out = new PrintWriter(h.clientSocket.getOutputStream(), true);

        // Immediately get username and color
        // Client will send these first
        h.username = h.in.readLine();

        // Add an entry for this client's name in a hashmap
        clientNames.put(h.username, h);

        h.color = h.in.readLine();


        // Joining message, send to all
        broadcastMsg(String.format("User %s has joined the chat!\n", ConsoleColors.apply(h.username, h.color)), null);

        for (; ; ) {
            String message = h.in.readLine();

            // Ignore empty messages
            if (message.trim().isEmpty()) continue;

            // Handle message types
            // a null message indicates a disconnection, handle gracefully
            if("/quit".equals(message) || message == null) // Announce if someone leaves the chat
            {
                broadcastMsg(String.format("User %s has left the chat!\n", ConsoleColors.apply(h.username, h.color)), h);
                break;
            }
            else if("/users".equals(message)) // Broadcast active user list
            {
                // Using the stream API for simpler code (it's like C#'s LINQ)
                List<String> userList = activeClients.stream()
                        .map(c -> ConsoleColors.apply(c.username, c.color))
                        .toList();

                // Broadcast to all
                broadcastMsg("ACTIVE USERS: "+ String.join(", ",  userList), null);
            }
            else // Regular message (Broadcast or Unicast)
            {
                List<String> pingedUsers = Arrays.stream(message.split(" "))
                        .filter(s -> s.charAt(0) == '@')
                        .map(p -> p.substring(1))
                        .toList();

                if(pingedUsers.isEmpty()) // Broadcast to all
                {
                    broadcastMsg(ConsoleColors.apply(h.username, h.color) + ": " + message, h);
                }
                else // Send only to pinged users
                {
                    // Add colors for usernames in message
                    List<String> tokens = Arrays.stream(message.split(" "))
                                    .map(p -> !clientNames.containsKey(p.substring(1)) ? p : "@" + ConsoleColors.apply(p.substring(1), clientNames.get(p.substring(1)).color))
                                    .toList();
                    message = String.join(" ", tokens);

                    unicastMsg(ConsoleColors.apply(h.username, h.color) + ": " + message, pingedUsers);
                }




            }

        }
        removeClient(h);
    }

    private void broadcastMsg(String msg, ClientHandler sender) throws IOException
    {
        // Print on server-side too (for debug)
        System.out.println(msg);

        synchronized (activeClients)
        {
            for(ClientHandler c : activeClients)
            {
                // Don't resend to sender
                if (c == sender) continue;

                c.out.println(msg);
            }
        }
    }

    // Send messages only to those in the passed list
    private void unicastMsg(String msg, List<String> recipients)
    {
        // Print on server-side too (for debug)
        System.out.println(msg);

        synchronized (activeClients)
        {
            for (String username : recipients)
            {
                if (!clientNames.containsKey(username)) continue;

                ClientHandler c = clientNames.get(username);
                c.out.println(msg);
            }
        }
    }

    // Called when a client disconnects
    private void removeClient(ClientHandler h) throws IOException {
        h.in.close();
        h.out.close();
        h.clientSocket.close();

        activeClients.remove(h);
        clientNames.remove(h.username);
    }
}
