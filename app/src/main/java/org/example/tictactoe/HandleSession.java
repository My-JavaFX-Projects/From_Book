package org.example.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandleSession implements Runnable, TicTacToeConstants {
    private Socket player1;
    private Socket player2;

    private char[][] cell = new char[3][3];

    private DataInputStream fromPlayer1;
    private DataOutputStream toPlayer1;;
    private DataInputStream fromPlayer2;
    private DataOutputStream toPlayer2;

    private boolean continueToPlay = true;

    public HandleSession(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cell[i][j] = ' ';
            }
        }
    }

    @Override
    public void run() {

        try {
            DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
            DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
            DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
            DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());
            toPlayer1.writeInt(1);

            while (true) {
                int row = fromPlayer1.readInt();
                int column = fromPlayer1.readInt();
                cell[row][column] = 'X';
                if (isWon('X')) {
                    toPlayer1.writeInt(PLAYER1_WON);
                    toPlayer2.writeInt(PLAYER1_WON);
                    sendMove(toPlayer2, row, column);
                    break;
                } else if (isFull()) {
                    toPlayer1.writeInt(DRAW);
                    toPlayer2.writeInt(DRAW);
                    sendMove(toPlayer2, row, column);
                } else {
                    toPlayer2.writeInt(CONTINUE);
                    sendMove(toPlayer2, row, column);
                }

                row = fromPlayer2.readInt();
                column = fromPlayer2.readInt();
                cell[row][column] = 'O';
                if (isWon('O')) {
                    toPlayer1.writeInt(PLAYER2_WON);
                    toPlayer2.writeInt(PLAYER2_WON);
                    sendMove(toPlayer1, row, column);
                } else {
                    toPlayer1.writeInt(CONTINUE);
                    sendMove(toPlayer1, row, column);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    private void sendMove(DataOutputStream out, int row, int column) throws IOException {
        out.writeInt(row);
        out.writeInt(column);
    }

    private boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j] == ' ') return false;
            }
        }
        return true;
    }

    private boolean isWon(char token) {
        for (int i = 0; i < 3; i++) {
            if ( cell[i][0] == token && cell[i][1]== token && cell[i][2] ==token) return true;
        }
        for (int i = 0; i < 3; i++) {
            if ( cell[0][i] == token && cell[1][i]== token && cell[2][i] ==token) return true;
        }
        if ( cell[0][0] == token && cell[1][1]== token && cell[2][2] ==token) return true;
        if ( cell[0][2] == token && cell[1][1]== token && cell[2][0] ==token) return true;

        return false;
    }

}
