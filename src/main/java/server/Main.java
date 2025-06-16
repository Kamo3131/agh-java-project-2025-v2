package server;

import common.TCPCommunicator;
import server.db_objects.SavedFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("Server starting...");

        TCPCommunicator communicator = TCPCommunicator.startServer(8080);
        ServerInstance serverInstance = new ServerInstance(communicator);

        serverInstance.run();
    }
}
