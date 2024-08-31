package net.iamtakagi.uzsk.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.iamtakagi.uzsk.core.model.entity.Profile;

public class CorePlayer {

    private static Map<UUID, CorePlayer> players = new HashMap<>();

    public static Map<UUID, CorePlayer> getCorePlayers() {
        return players;
    }

    public static CorePlayer getCorePlayer(UUID uuid) {
        return players.get(uuid);
    }

    private UUID uuid;
    private Profile profile;
    private CoreSidebar sidebar;
    private CoreBossbar bossbar;
    public CorePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void init () {
        if (Core.getInstance().getCoreConfig().getSidebarSettings().isEnabled()) {
            this.sidebar.setup();
        }
        if (Core.getInstance().getCoreConfig().getBossbarSettings().isEnabled()) {
            this.bossbar = new CoreBossbar(getPlayer());
        }
        this.profile = Core.getInstance().getProfileDao().findByUUID(uuid);
        players.put(uuid, this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public CoreSidebar getSidebar() {
        return sidebar;
    }

    public CoreBossbar getBossbar() {
        return bossbar;
    }

    public Profile getProfile() {
        return profile;
    }

    public void saveProfile() {
        Core.getInstance().getProfileDao().update(profile);
    }

}
