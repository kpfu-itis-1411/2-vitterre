package com.continental.game;

import com.continental.net.ClientPacket;
import lombok.Data;

@Data
public class ActionPacket extends ClientPacket {

    private final ActionSet actionSet;

    public ActionPacket(ClientGame game) {
        this.actionSet = game.getActionSet();
    }
}
