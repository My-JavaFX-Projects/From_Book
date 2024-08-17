package org.example.tictactoe.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.tictactoe.HandleSession;
import org.example.tictactoe.TicTacToeConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class TicTacToeServer extends Application implements TicTacToeConstants {
    private int sessionNo = 1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea taLog = new TextArea();

        Scene scene = new Scene(new javafx.scene.control.ScrollPane(taLog), 450, 200);
        primaryStage.setTitle("TicTacToeServer");
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(()->{
            try {

                ServerSocket serverSocket = new ServerSocket(8073);
                Platform.runLater(() -> {
                    taLog.appendText(new Date() + " Started Server socket 8073\n");
                });
                while (true) {
                    Platform.runLater(()->{
                        taLog.appendText(new Date() + ": Wait for players to join session " + sessionNo + '\n');
                    });
                    Socket player1 = serverSocket.accept();
                    Platform.runLater(()->{
                        taLog.appendText(new Date() + "Player 1 joined the session " + sessionNo + '\n');
                        taLog.appendText("Player 1's IP is" + player1.getInetAddress().getHostAddress() + '\n');
                    });
                    new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1);
                    Socket player2 = serverSocket.accept();
                    Platform.runLater(()->{
                        taLog.appendText(new Date() + "Player 2 joined the session " + sessionNo + '\n');
                        taLog.appendText("Player 2's IP is " + player2.getInetAddress().getHostAddress() + '\n');
                    });
                    new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2);
                    Platform.runLater(()->{
                        taLog.appendText(new Date() + ": Start a thread for session " + sessionNo++ + '\n');
                    });
                    new Thread(new HandleSession(player1, player2)).start();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }
}
