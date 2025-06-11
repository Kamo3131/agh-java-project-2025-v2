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

        DatabaseORM db = new DatabaseORM();

        ServerInstance serverInstance = new ServerInstance(communicator);

        // test_user = ca1d47fd-b553-4dab-bc31-3cd89cd42fa5

        serverInstance.run();
    }
}
