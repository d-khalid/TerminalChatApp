import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void start(String ip, int port) throws IOException {
        serverSocket = new Socket(ip, port);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        Thread t = new Thread(
                () ->
                {
                    try
                    {
                        String m;
                        while ((m = in.readLine()) != null)
                        {
                            BufferedCharInput.clearLine();

                            System.out.println(m);

                            BufferedCharInput.redrawLine();
                        }
                        System.out.println("Connection closed by server!");

                    }
                    catch(IOException e)
                    {
                        System.out.println(e.getMessage());
                    }

                }
        );
        t.start();

        // Send username and color first
        String username = BufferedCharInput.readLineAtomic("Enter Username: ");
        out.println(username);

        System.out.println("\nColor Options: [RED, YELLOW, BLUE, GREEN, PURPLE, CYAN, WHITE, BLACK]");

        String color = BufferedCharInput.readLineAtomic("Enter Color: ");
        String colorCode = ConsoleColors.getColor(color);
        out.println(colorCode);

        for(;;)
        {
            String str = BufferedCharInput.readLineAtomic(ConsoleColors.apply(username + ": ", colorCode));

            out.println(str);

            if("/quit".equals(str)) break;
        }
    }

    public void stop() {
        try
        {
            in.close();
            out.close();
            serverSocket.close();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }


}
