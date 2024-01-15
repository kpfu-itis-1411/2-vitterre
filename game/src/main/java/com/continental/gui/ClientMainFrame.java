package com.continental.gui;

import com.continental.net.Client;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class ClientMainFrame extends JFrame {

    public static final int WIDTH = 568;
    public static final int HEIGHT = 320;
    public static final int SCALE_FACTOR = 2;
    public static final String NAME = "Blood Code";

    private Client client;

    public ClientMainFrame() {
        setTitle(NAME);

        setMinimumSize(new Dimension(
                WIDTH * SCALE_FACTOR,
                HEIGHT * SCALE_FACTOR
        ));

        setMaximumSize(new Dimension(
                WIDTH * SCALE_FACTOR,
                HEIGHT * SCALE_FACTOR
        ));

        setPreferredSize(new Dimension(
                WIDTH * SCALE_FACTOR,
                HEIGHT * SCALE_FACTOR
        ));

        add(new ClientMenuPanel());

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (Objects.nonNull(client)) {
                    client.disconnect();
                }

                System.exit(0);
            }
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean startGame(String ipAddress, int port) {
        System.out.println("[CLIENT_GUI] Starting game...]");
        client = new Client(this, ipAddress, port);
        return startGame();
    }

    public boolean startGame(int port) {
        System.out.println("[CLIENT_GUI] Starting game...]");
        client = new Client(this, port);
        return startGame();
    }

    private boolean startGame() {
        getContentPane().removeAll();

        val gamePanel = new ClientGamePanel(client.getGame());

        add(gamePanel);


        gamePanel.requestFocusInWindow();

        pack();
        revalidate();
        repaint();

        client.run();

        return true;
    }

    public static void main(String[] args) {
        new ClientMainFrame();
    }

    public void handleDeath() {
        getContentPane().removeAll();

        add(new JLabel("You died."));

        pack();
        revalidate();
        repaint();
    }
}