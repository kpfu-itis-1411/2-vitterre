package com.continental.net;

import com.continental.game.Action;
import com.continental.game.ActionPacket;
import com.continental.game.ClientGame;
import com.continental.game.entity.Entity;
import com.continental.gui.ClientMainFrame;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.UUID;

@Getter
public final class Client implements Runnable {

    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private boolean isRunning;
    private ClientGame game;
    private final ClientMainFrame mainFrame;

    public Client(ClientMainFrame mainFrame, String ipAddress, int port) {
        try {
            this.mainFrame = mainFrame;
            this.socket = new Socket(ipAddress, port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.isRunning = false;

            initialServerCommunication();
        } catch (IOException e) {
            System.out.println("[CLIENT] Error during initializing a client.");
            throw new RuntimeException(e);
        }
    }

    public Client(ClientMainFrame mainFrame, int port) {
        try {
            this.mainFrame = mainFrame;
            this.socket = new Socket(InetAddress.getLocalHost(), port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());

            initialServerCommunication();
        } catch (IOException e) {
            System.out.println("[CLIENT] Error during initializing a client.");
            throw new RuntimeException(e);
        }
    }

    private void initialServerCommunication() {
        try {
            val clientUuid = (UUID) inputStream.readObject();
            game = new ClientGame(this, clientUuid);

            game.processEntityList(((TreeMap<UUID, Entity>) inputStream.readObject()));

            System.out.println("[CLIENT] Finished initial server communication");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[CLIENT] Error during initial server communication");
            throw new RuntimeException(e);
        }
    }

    private void startReadAndWriteLoop() {
        System.out.println("[CLIENT] Start reading and writing loop.");
        while (isRunning) {
            try {
                // FIXME: error is here (socket is closed)
                game.processEntityList((TreeMap<UUID, Entity>) inputStream.readObject());

                sendMessage(new ActionPacket(game));
                game.getActionSet().getInstantActions().clear();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[CLIENT] Error during starting reading and writing loop");
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(ClientPacket clientPacket) {
        try {
            outputStream.writeObject(clientPacket);
            outputStream.reset();
//            outputStream.flush();
        } catch (IOException e) {
            System.out.println("[CLIENT] Error during sending a message.");
            throw new RuntimeException(e);
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    val packetFromServer = (ClientPacket) inputStream.readObject();
//                    System.out.println(packetFromServer.getPayload());
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("[CLIENT] Error during listening to message.");
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void disconnect() {
        System.out.println("[CLIENT] Disconnecting from the server.");

        isRunning = false;

        sendMessage(new DisconnectPacket());

        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("[CLIENT] Error during disconnecting from the server");
            throw new RuntimeException();
        }

        mainFrame.handleDeath();
    }

    @Override
    public void run() {
        isRunning = true;
        new Thread(this::startReadAndWriteLoop).start();
        new Thread(this::startGameLoop).start();
    }

    private void startGameLoop() {
        System.out.println("[CLIENT] Start game loop.");
        while (isRunning) {
            game.tick();
            mainFrame.repaint();
        }
    }
}
