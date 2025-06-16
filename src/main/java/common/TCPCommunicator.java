package common;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPCommunicator {
    private ServerSocket ss;
    private final boolean is_server;
    private final int port;

    public enum MessageType {
        LOGIN,
        REGISTER,
        FILE_UPLOAD,
        FILE_DOWNLOAD,
        GET_FILE_LIST,
        FILE_UPDATE
    }

    private TCPCommunicator(int port, boolean is_server) throws IOException {
        if (is_server) {
            this.ss = new ServerSocket(port);
        }
        this.is_server = is_server;
        this.port = port;
    }

    public static TCPCommunicator startServer(int port) throws IOException {
        return new TCPCommunicator(port, true);
    }

    public static TCPCommunicator startClient(int port) throws IOException {
        return new TCPCommunicator(port, false);
    }

    private void sendMessageImpl(Socket socket, Object message) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
    }

    private Object receiveMessageImpl(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        return ois.readObject();
    }

    private void sendFileImpl(Socket socket, File file) throws IOException {
        byte[] buffer = new byte[8 * 1024];

        try (InputStream in = new FileInputStream(file)) {
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        }
    }

    private void receiveAndSaveFileImpl(Socket socket, String filename) throws IOException {
        byte[] buffer = new byte[8 * 1024];

        try (OutputStream out = new FileOutputStream(filename)) {
            InputStream in = socket.getInputStream();

            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        }
    }

    public void sendMessage(Object message) throws IOException {
        if (this.is_server) {
            try (Socket socket = this.ss.accept()) {
                this.sendMessageImpl(socket, message);
            }
        }
        else {
            try (Socket socket = new Socket("localhost", this.port)) {
                this.sendMessageImpl(socket, message);
            }
        }
    }

    public Object receiveMessage() throws IOException, ClassNotFoundException {
        if (this.is_server) {
            try (Socket socket = this.ss.accept()) {
                return this.receiveMessageImpl(socket);
            }
        }
        else {
            try (Socket socket = new Socket("localhost", this.port)) {
                return this.receiveMessageImpl(socket);
            }
        }
    }

    public Object sendAndReceiveMessage(Object message) throws IOException, ClassNotFoundException {
        this.sendMessage(message);
        return this.receiveMessage();
    }

    public void sendFile(File file) throws IOException {
        if (this.is_server) {
            try (Socket socket = this.ss.accept()) {
                this.sendFileImpl(socket, file);
            }
        }
        else {
            try (Socket socket = new Socket("localhost", this.port)) {
                this.sendFileImpl(socket, file);
            }
        }
    }

    public void receiveAndSaveFile(String filename) throws IOException {
        if (this.is_server) {
            try (Socket socket = this.ss.accept()) {
                this.receiveAndSaveFileImpl(socket, filename);
            }
        }
        else {
            try (Socket socket = new Socket("localhost", this.port)) {
                this.receiveAndSaveFileImpl(socket, filename);
            }
        }
    }
}

