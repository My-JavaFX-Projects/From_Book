package org.example.studentRegistration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StudentServer {
    private ObjectOutputStream outputToFile;
    private ObjectInputStream inputFromClient;

    public static void main(String[] args) {
        new StudentServer();
    }


    public StudentServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8064);
            System.out.println("server started");
            outputToFile = new ObjectOutputStream(new FileOutputStream("student.dat", true));
            while (true) {
                Socket socket = serverSocket.accept();
                inputFromClient = new ObjectInputStream(socket.getInputStream());
                Object object = inputFromClient.readObject();
                outputToFile.writeObject(object);
                System.out.println("A new Student object is stored");
            }
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                inputFromClient.close();
                outputToFile.close();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
