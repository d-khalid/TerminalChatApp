## Intro
This is a terminal-based chat application made with Java (JDK 21.0). It supports basic chatting features. It was made for a uniersity assignment.

## Features
- The app can be run as both client and server based on the command-line arguments given  
- Clients can connect to a server by specifying the port and ip address of that server
- Clients will be prompted for a unique username and a color upon connecting with a server
- Two types of messaging are supported:
  - Broadcast: The entered message is sent to all connected clients.
  - Unicast: The entered messsage is sent to all clients which were pinged with the syntax '@<username>' within the message
- Other commands:
  - '/users': Lists the current active users, which are connected to the server by username
  - '/quit': Terminate the connection with the server and exit the application.
 
## Execution
This repository has a '.jar' file in the Releases sections, which can be used to run the app regardless of platform as long as Java Runtime Environment (JRE) 21 or later are avaliable. You might need to grant executable permissions to the file in some cases

To run the .jar file as a server:  
````java -jar TerminalChatApp_v1.0.jar --server 8080````  
To run the .jar file as a client:  
````java -jar TerminalChatApp_v1.0.jar --client 8080 127.0.0.1````
## License
The code in this repository is licensed under the GNU General Public License, version 3.

