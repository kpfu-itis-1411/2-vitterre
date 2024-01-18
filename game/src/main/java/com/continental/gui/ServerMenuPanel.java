package com.continental.gui;

import com.continental.net.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerMenuPanel extends JPanel {

    private final JTextField portNumber;

    public ServerMenuPanel() {
        portNumber = new JTextField(String.valueOf(Server.PORT_NUMBER));
        add(portNumber);

        add(new JButton("Start server") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ((ServerMainFrame) getTopLevelAncestor())
                                .startServer(Integer.parseInt(portNumber.getText()));
                    }
                });
            }
        });
    }
}
