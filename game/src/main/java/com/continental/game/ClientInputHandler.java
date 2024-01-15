package com.continental.game;

import lombok.val;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class ClientInputHandler implements KeyListener {

    private final boolean[] previouslyPressed;
    private final ClientGame game;

    public ClientInputHandler(ClientGame game) {
        this.game = game;
        previouslyPressed = new boolean[99999999];
        Arrays.fill(previouslyPressed, false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        val keyCode = e.getKeyCode();

        if (previouslyPressed[keyCode]) {
            return;
        }

        val longTermActions = game.getActionSet().getLongTermActions();
        val instantActions = game.getActionSet().getInstantActions();

        switch (keyCode) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> longTermActions.add(Action.MOVE_LEFT);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> longTermActions.add(Action.MOVE_RIGHT);
            case KeyEvent.VK_SPACE -> instantActions.add(Action.JUMP);
            case KeyEvent.VK_ENTER -> instantActions.add(Action.SHOOT);
        }

        previouslyPressed[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        val keyCode = e.getKeyCode();

        val longTermActions = game.getActionSet().getLongTermActions();

        switch (keyCode) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> longTermActions.remove(Action.MOVE_LEFT);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> longTermActions.remove(Action.MOVE_RIGHT);
        }

        previouslyPressed[keyCode] = false;
    }
}
