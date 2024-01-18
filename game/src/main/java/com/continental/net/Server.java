package com.continental.net;

import com.continental.game.ActionPacket;
import com.continental.game.ServerGame;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Server implements Runnable {

    public final static int TICK_RATE = 32;
    public final static int MS_PER_TICK = 1000000000 / TICK_RATE;
    public final static int PORT_NUMBER = 8080;

    public final List<ClientHandler> clientHandlers = new ArrayList<>();
    private final ServerGame game;
    private final ServerSocket serverSocket;

    public Server(ServerGame game, int port) {
        try {
            serverSocket = new ServerSocket(port);
            this.game = game;
        } catch (IOException e) {
            System.err.println("[SERVER] Error during launching the server");
            throw new RuntimeException(e);
        }
    }

    public Server(ServerGame game) {
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            this.game = game;
        } catch (IOException e) {
            System.err.println("[SERVER] Error during launching the server");
            throw new RuntimeException(e);
        }
    }

    private void startAcceptingClientsLoop() {
        System.out.println("[SERVER] Accepting clients...");

        while (true) {
            System.out.println("[SERVER] Waiting for clients to connect...");
            try {
                val socket = serverSocket.accept();

                System.out.println("[SERVER] A new client has connected.");

                // TODO: game.spawnPlayerEntity() -> UUID
                val clientHandler = new ClientHandler(socket, this, game.spawnPlayerEntity());
                clientHandlers.add(clientHandler);

                new Thread(clientHandler).start();
            } catch (IOException e) {
                System.err.println("[SERVER] Error during accepting clients.");
                throw new RuntimeException(e);
            }
        }
    }

    public void broadcastMessage(ClientPacket clientPacket) {
        for (val clientHandler : clientHandlers) {
            sendMessage(clientHandler, clientPacket);
        }
    }

    public void sendMessage(ClientHandler clientHandler, ClientPacket clientPacket) {
        clientHandler.sendMessage(clientPacket);
    }

    public void broadcastMessage() {
        for (val clientHandler : clientHandlers) {
            sendMessage(clientHandler);
        }
    }

    public void sendMessage(ClientHandler clientHandler) {
        clientHandler.sendMessage(new ClientPacket());
    }

    @Override
    public void run() {
        new Thread(this::startAcceptingClientsLoop).start();
        new Thread(this::startGameLoop).start();
    }

    private void startGameLoop() {
        var lastTick = System.nanoTime();

        while (true) {
            val nextTickTime = lastTick + MS_PER_TICK;

            if (System.nanoTime() < nextTickTime) {
                continue;
            }

            game.tick();

            sendUpdatesToAll();

            lastTick = System.nanoTime();
        }
    }

    public void sendUpdatesToAll() {
        for (val clientHandler : clientHandlers) {
            sendUpdates(clientHandler);
        }
    }

    public void sendUpdates(ClientHandler clientHandler) {
        clientHandler.sendUpdate(game.getEntities());
    }

    public static void main(String[] args) {
        val server = new Server(new ServerGame(), 8080);
        server.run();
    }

    public void processPacket(ClientHandler clientHandler, ClientPacket packet) {
        switch (packet) {
            case ActionPacket a -> game.updateActionSet(clientHandler.getClientUuid(), a.getActionSet());
            case ClientPacket c -> System.out.println("[SERVER] " + clientHandler.getClientUuid());
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("[SERVER] Error during closing the server.");
            throw new RuntimeException(e);
        }
    }
}
