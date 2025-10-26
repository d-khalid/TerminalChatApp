import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket  serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        System.out.printf("Server listening on port %d...\n", port);

        clientSocket = serverSocket.accept();
        System.out.printf("Accepted connection from %s\n", clientSocket.getRemoteSocketAddress());

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        for(;;)
        {
            String message = in.readLine();

            out.println(message);
            System.out.println("Client: " + message);

            if("\\quit".equals(message)) break;
        }


    }

    public void stop()
    {
        try
        {
            in.close();
            out.close();
            serverSocket.close();
            clientSocket.close();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }

    }
}
