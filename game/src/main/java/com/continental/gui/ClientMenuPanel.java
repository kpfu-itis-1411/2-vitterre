package com.continental.gui;

import com.continental.net.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientMenuPanel extends JPanel {

    private final JTextField ipAddress;
    private final JTextField portNumber;

    public ClientMenuPanel() {
        ipAddress = new JTextField("localhost");
        add(ipAddress);

        portNumber = new JTextField("" + Server.PORT_NUMBER);
        add(portNumber);

        add(new JButton("Join Game") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ((ClientMainFrame) getTopLevelAncestor()).startGame(ipAddress.getText(),
                                                                            Integer.parseInt(portNumber.getText())
                        );
                    }
                });
            }
        });
    }
}
