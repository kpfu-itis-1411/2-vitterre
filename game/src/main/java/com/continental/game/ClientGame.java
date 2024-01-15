package com.continental.game;

import com.continental.game.entity.Entity;
import com.continental.net.Client;
import lombok.Data;

import java.util.TreeMap;
import java.util.UUID;

@Data
public final class ClientGame {

    private final UUID playerUuid;
    private final Client client;

    private final ActionSet actionSet;

    private TreeMap<UUID, Entity> entities;

    public ClientGame(Client client, UUID clientUuid) {
        this.client = client;
        this.playerUuid = clientUuid;
        actionSet = new ActionSet();
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getUuid(), entity);
    }

    public void processEntityList(TreeMap<UUID, Entity> incomingEntityList) {
        entities = incomingEntityList;
    }

    public void tick() {
        // TODO: make it only after creating a player entity
        if (!entities.containsKey(playerUuid)) {
            client.disconnect();
        }
    }
}
