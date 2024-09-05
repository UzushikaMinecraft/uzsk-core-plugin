package net.iamtakagi.uzsk.core;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.iamtakagi.uzsk.core.model.ProfileDao;
import net.iamtakagi.uzsk.core.model.entity.Profile;

class GeneralListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        if (profile == null) {
            profile = new Profile(
                    event.getPlayer().getUniqueId(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    0,
                    0,
                    0,
                    0,
                    0,
                    0);
            profileDao.insert(profile);
        } else {
            if (profile.getLastLoginDate() < System.currentTimeMillis() - 86400000) {
                profile.getExperience().increase(config.getExperienceSettings().getOnLoginNetworkIncrements());
                event.getPlayer().sendMessage("ログインボーナス (経験値) を受け取りました！");
            }
            profile.setLastLoginDate(System.currentTimeMillis());
        }

        profileDao.update(profile);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CorePlayer corePlayer = new CorePlayer(event.getPlayer().getUniqueId());
        corePlayer.init();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(CorePlayer.getCorePlayers().containsKey(event.getPlayer().getUniqueId())) {
            CorePlayer corePlayer = CorePlayer.getCorePlayer(event.getPlayer().getUniqueId());
            Profile profile = corePlayer.getProfile();
            profile.setTotalPlayTime(profile.getTotalPlayTime() + (System.currentTimeMillis() - profile.getLastLoginDate()));
            corePlayer.saveProfile();
            CorePlayer.getCorePlayers().remove(event.getPlayer().getUniqueId());
        }
    }
}

class ExperienceListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        CorePlayer corePlayer = CorePlayer.getCorePlayer(event.getPlayer().getUniqueId());
        Profile profile = corePlayer.getProfile();
        CoreConfig config = Core.getInstance().getCoreConfig();
        profile.setTotalDestroyBlocks(profile.getTotalDestroyBlocks() + 1);
        int prevLevel = profile.getExperience().getCurrentLevel();
        profile.getExperience().increase(config.getExperienceSettings().getOnBreakBlockIncrements());
        corePlayer.saveProfile();
        int newLevel = profile.getExperience().getCurrentLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    corePlayer.updateNameTag();
                }
            }.runTaskLater(Core.getInstance(), 2);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        CorePlayer corePlayer = CorePlayer.getCorePlayer(event.getPlayer().getUniqueId());
        Profile profile = corePlayer.getProfile();
        CoreConfig config = Core.getInstance().getCoreConfig();
        profile.setTotalBuildBlocks(profile.getTotalBuildBlocks() + 1);
        int prevLevel = profile.getExperience().getCurrentLevel();
        profile.getExperience().increase(config.getExperienceSettings().getOnPlaceBlockIncrements());
        corePlayer.saveProfile();
        int newLevel = profile.getExperience().getCurrentLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    corePlayer.updateNameTag();
                }
            }.runTaskLater(Core.getInstance(), 2);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        if (event.getEntityType() == EntityType.PLAYER ||
                event.getEntityType() == EntityType.ARMOR_STAND ||
                event.getEntityType() == EntityType.ITEM_FRAME ||
                event.getEntityType() == EntityType.LEASH_KNOT ||
                event.getEntityType() == EntityType.PAINTING ||
                event.getEntityType() == EntityType.VILLAGER) {
            return;
        }

        CorePlayer corePlayer = CorePlayer.getCorePlayer(event.getEntity().getKiller().getUniqueId());
        Profile profile = corePlayer.getProfile();
        CoreConfig config = Core.getInstance().getCoreConfig();
        profile.setTotalMobKills(profile.getTotalMobKills() + 1);
        int prevLevel = profile.getExperience().getCurrentLevel();
        profile.getExperience().increase(config.getExperienceSettings().getOnKillMobIncrements());
        corePlayer.saveProfile();
        int newLevel = profile.getExperience().getCurrentLevel();
        if (newLevel > prevLevel) {
            event.getEntity().getKiller().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getEntity().getKiller().playSound(event.getEntity().getKiller().getLocation(), "entity.player.levelup", 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    corePlayer.updateNameTag();
                }
            }.runTaskLater(Core.getInstance(), 2);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        CorePlayer corePlayer = CorePlayer.getCorePlayer(event.getPlayer().getUniqueId());
        Profile profile = corePlayer.getProfile();
        CoreConfig config = Core.getInstance().getCoreConfig();
        int prevLevel = profile.getExperience().getCurrentLevel();
        profile.getExperience().increase(config.getExperienceSettings().getOnChatIncrements());
        corePlayer.saveProfile();
        int newLevel = profile.getExperience().getCurrentLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    corePlayer.updateNameTag();
                }
            }.runTaskLater(Core.getInstance(), 2);
        }
    }
}
