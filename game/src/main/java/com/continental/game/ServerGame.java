package com.continental.game;

import com.continental.game.entity.*;
import lombok.Data;
import lombok.val;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Data
public class ServerGame {

    private final List<PlayerEntity> players = new ArrayList<>();

    private final TreeMap<UUID, Entity> entities;

    public ServerGame() {
        entities = new TreeMap<>();
        init();
    }

    private void init() {
        try (val platformScanner =
                     new Scanner(new File("game/src/main/resources/platform-map.csv"))
        ) {
            while (platformScanner.hasNextLine()) {
                val dimensionsString = platformScanner.nextLine().split(",");
                val platformEntity = new PlatformEntity(this,
                                                        Double.parseDouble(dimensionsString[0]),
                                                        Double.parseDouble(dimensionsString[1]),
                                                        Double.parseDouble(dimensionsString[2]),
                                                        Double.parseDouble(dimensionsString[3])
                );
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateActionSet(UUID uuid, ActionSet actionSet) {
        val entity = entities.get(uuid);
        if (Objects.requireNonNull(entity) instanceof PlayerEntity p) {
            System.out.println(p + " " + actionSet.getInstantActions() + " " + actionSet.getLongTermActions());
        }

        entities.get(uuid).setActionSet(actionSet);
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getUuid(), entity);
    }

    public void removeEntity(UUID uuid) {
        if (Objects.nonNull(getEntities().get(uuid))) {
            if (getEntities().get(uuid) instanceof PlayerEntity p) {
                if (p.getTeam().equals(PlayerTeam.RED)) {
                    players.removeIf(r -> r.getTeam().equals(PlayerTeam.RED));
                } else {
                    players.removeIf(b -> b.getTeam().equals(PlayerTeam.BLUE));
                }
            }

            entities.remove(uuid);
        }
    }

    public void tick() {
        val entitiesCopy = new TreeMap<>(entities);

        for (val entity : entitiesCopy.values()) {
            entity.tick();
        }

        for (val entity : entitiesCopy.values()) {
            for (val otherEntity : entitiesCopy.values()) {
                if (entity.isColliding(otherEntity)) {
                    entity.handleCollision(otherEntity);
                }
            }
        }
    }

    public UUID spawnPlayerEntity() {
        if (players.size() >= 2) {
            throw new RuntimeException("There can be only 2 players.");
        }

        for (val player : players) {
            if (player.getTeam().equals(PlayerTeam.RED)) {
                val bluePlayer = new PlayerEntity(this, PlayerTeam.BLUE, 1, 3, Direction.RIGHT);
                players.add(bluePlayer);
                return bluePlayer.getUuid();
            } else if (player.getTeam().equals(PlayerTeam.BLUE)) {
                val redPlayer = new PlayerEntity(this, PlayerTeam.RED, 8, 3, Direction.LEFT);
                players.add(redPlayer);
                return redPlayer.getUuid();
            }
        }

        val bluePlayer = new PlayerEntity(this, PlayerTeam.BLUE, 1, 3, Direction.LEFT);
        players.add(bluePlayer);
        return bluePlayer.getUuid();
    }
}
