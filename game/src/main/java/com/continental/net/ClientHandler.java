package com.continental.net;

import com.continental.game.entity.Entity;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;
import java.util.UUID;

@Getter
public final class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final UUID clientUuid;
    private boolean isRunning;

    public ClientHandler(Socket socket, Server server, UUID uuid) {
        try {
            this.socket = socket;
            this.server = server;
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.clientUuid = uuid;
            this.isRunning = true;

            initialClientCommunication();
        } catch (IOException e) {
            System.out.println("[CLIENT_HANDLER] Error during creating client handler instance.");
            throw new RuntimeException(e);
        }
    }

    private void initialClientCommunication() {
        try {
            outputStream.writeObject(clientUuid);
            server.sendUpdates(this);
        } catch (IOException e) {
            System.out.println("[CLIENT_HANDLER] Error during initial communication.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            try {
                val packet = (ClientPacket) inputStream.readObject();
                server.processPacket(this, packet);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("[CLIENT_HANDLER] Error during handling the message.");
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnect() {
        try {
            isRunning = false;
            inputStream.close();
            outputStream.close();
            socket.close();

            System.out.println("[CLIENT_HANDLER] Disconnected.");
        } catch (IOException e) {
            System.out.println("[CLIENT_HANDLER] Error during disconnecting...");
        }
    }

    public void sendMessage(ClientPacket message) {
        if (!isRunning) {
            return;
        }

        try {
            outputStream.writeObject(message);
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUpdate(TreeMap<UUID, Entity> updates) {
        if (!isRunning) {
            return;
        }

        try {
            outputStream.writeObject(updates);
            outputStream.reset();
        } catch (IOException e) {
            System.out.println("[CLIENT_HANDLER] Error during sending update");
            throw new RuntimeException(e);
        }
    }
}
