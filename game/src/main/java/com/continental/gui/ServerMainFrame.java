package com.continental.gui;

import com.continental.game.ServerGame;
import com.continental.net.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class ServerMainFrame extends JFrame {

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE_FACTOR = 3;
    public static final String NAME = "Server manager";

    private Server server;

    public ServerMainFrame() {

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

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new ServerMenuPanel());

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Objects.nonNull(server)) {
                    server.close();
                }
                System.exit(0);
            }
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startServer(int portNumber) {
        getContentPane().removeAll();

        System.out.println("[SERVER_GUI] Starting server on port " + portNumber);

        server = new Server(new ServerGame(), portNumber);

        add(new ServerMonitoringPanel(server));

        pack();
        revalidate();
        repaint();

        server.run();
    }

    public static void main(String[] args) {
        new ServerMainFrame();
    }
}
