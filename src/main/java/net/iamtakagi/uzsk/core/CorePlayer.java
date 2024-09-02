package net.iamtakagi.uzsk.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.nametagedit.plugin.NametagEdit;

import net.iamtakagi.iroha.Style;
import net.iamtakagi.uzsk.core.CoreConfig.RoleSettings.Role;
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
    public CorePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void init () {
        if (Core.getInstance().getCoreConfig().getSidebarSettings().isEnabled()) {
            this.sidebar = new CoreSidebar(this);
            this.sidebar.setup();
        }
        this.profile = Core.getInstance().getProfileDao().findByUUID(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                updateNameTag();
            }
        }.runTaskLater(Core.getInstance(), 2);
        players.put(uuid, this);
    }

    public String getPrefix () {
        List<Role> roles = Core.getInstance().getCoreConfig().getRoleSettings().getRoles();
        for (Role role : roles) {
            if (getBukkitPlayer().hasPermission(role.getPermission())) {
                return Style.AQUA + "[Lv." + profile.getExperience().getCurrentLevel() + "] " + role.getPrefix();
            }
        };
        return Style.AQUA + "[Lv." + profile.getExperience().getCurrentLevel() + "] " + Core.getInstance().getCoreConfig().getRoleSettings().getDefaultRole().getPrefix();
    }

    public String getSuffix () {
        List<Role> roles = Core.getInstance().getCoreConfig().getRoleSettings().getRoles();
        for (Role role : roles) {
            if (getBukkitPlayer().hasPermission(role.getPermission())) {
                return role.getSuffix();
            }
        };
        return Core.getInstance().getCoreConfig().getRoleSettings().getDefaultRole().getSuffix();
    }

    public void updateNameTag () {
        String prefix = getPrefix();
        String suffix = getSuffix();
        NametagEdit.getApi().setPrefix(getBukkitPlayer(), prefix);
        NametagEdit.getApi().setSuffix(getBukkitPlayer(), suffix);
        getBukkitPlayer().setDisplayName(prefix + getBukkitPlayer().getName() + suffix);
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public CoreSidebar getSidebar() {
        return sidebar;
    }

    public Profile getProfile() {
        return profile;
    }

    public void saveProfile() {
        Core.getInstance().getProfileDao().update(profile);
    }

}
