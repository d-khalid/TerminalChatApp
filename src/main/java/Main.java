import java.net.*;
import java.util.*;

class AppConfig {
    public boolean IsServer;

    // These fields are only relevant for clients
    public String serverAddress;
    public int port;

}

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        AppConfig conf = ParseArgs(new ArrayList<String>(Arrays.asList(args)));

        // Server and client code
        if(conf.IsServer)
        {
            ChatServer server = new ChatServer();
            try
            {
                server.start(conf.port);
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
                return;
            }
            finally {
                server.stop();
            }
        }
        else
        {
            ChatClient client = new ChatClient();
            try
            {
                client.start(conf.serverAddress, conf.port);

                for(;;)
                {
                    System.out.print("Write a Message: ");
                    String str = scanner.nextLine();

                    client.sendMessage(str);

                    if("\\quit".equals(str)) break;
                }
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
                return;
            }
            finally {
                client.stop();
            }
        }

    }

    // ArrayList is easier to operate on
    static AppConfig ParseArgs(ArrayList<String> args) {
        try {
            AppConfig appConfig = new AppConfig();

            if (args.contains("--server")) {
                appConfig.IsServer = true;
                appConfig.port = Integer.parseInt(args.get(1));
            } else if (args.contains("--client")) {
                appConfig.IsServer = false;
                appConfig.port = Integer.parseInt(args.get(1));
                appConfig.serverAddress = args.get(2);
            }
            else throw new IllegalArgumentException();

            return appConfig;
        } catch (Exception e) {

            System.err.println("Invalid arguments!");
            System.err.println("Usage: java Main\n" +
                    "--server <Port>\t\t\t\t\tStarts an instance of the server on the specified port.\n" +
                    "--client <Port> <IPAddress> \tStarts an instance of the client that connects to the specified endpoint");
            System.exit(1);
            return null; // Should never be returned
        }


    }
}