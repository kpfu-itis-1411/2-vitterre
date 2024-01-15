package com.continental.gui;

import com.continental.net.Server;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerMonitoringPanel extends JPanel {

    public ServerMonitoringPanel(Server server) {
        add(new JLabel("Server is running."));

        try {
            add(new JLabel(
                    "IP Address: " + InetAddress.getLocalHost().getHostAddress()
            ));
            add(new JLabel(
                    "Port: " + server.getServerSocket().getLocalPort()
            ));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
