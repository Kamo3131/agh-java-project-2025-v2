package server;

import common.TCPCommunicator;
import common.messages.ConnectionRequest;
import common.messages.ConnectionResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, InterruptedException {
        ArrayBlockingQueue<Integer> ports = getPorts();

        System.out.println("Server starting...");

        TCPCommunicator communicator = TCPCommunicator.startServer(8080);

        while (true) {
            if (!ports.isEmpty()) {
                ConnectionRequest req = (ConnectionRequest) communicator.receiveMessage();

                int port = ports.take();
                ConnectionResponse response = new ConnectionResponse(port);

                TCPCommunicator comm = TCPCommunicator.startServer(port);
                Thread t = new Thread(() -> {
                    try {
                        (new ServerInstance(comm)).run();
                        ports.put(port);
                    } catch (SQLException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                );
                t.start();

                communicator.sendMessage(response);
            }
        }
    }

    private static ArrayBlockingQueue<Integer> getPorts() {
        Stack<Integer> ports = new Stack<>();

        for (int i = 1024; i < 2048; i++) {
            ports.push(i);
        }

        Collections.shuffle(ports);

        return new ArrayBlockingQueue<Integer>(1024, true, ports);
    }
}
