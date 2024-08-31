package net.iamtakagi.uzsk.core;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
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
                profile.getExperiences().set(profile.getExperiences().size() + (profile.getExperiences().size()
                        * config.getExperienceSettings().getOnLoginNetworkIncreasePercentage()));
            }
            profile.setLastLoginDate(System.currentTimeMillis());
            profileDao.update(profile);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CorePlayer corePlayer = new CorePlayer(event.getPlayer().getUniqueId());
        corePlayer.init();
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(CorePlayer.getCorePlayers().containsKey(event.getPlayer().getUniqueId())) {
            CorePlayer.getCorePlayers().remove(event.getPlayer().getUniqueId());
        }
    }
}

class ExperienceListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        int prevLevel = profile.getExperiences().getLevel();
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnBreakBlockIncreasePercentage()));
        int newLevel = profile.getExperiences().getLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        int prevLevel = profile.getExperiences().getLevel();
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnPlaceBlockIncreasePercentage()));
        int newLevel = profile.getExperiences().getLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        if (event.getEntityType() == EntityType.PLAYER ||
                event.getEntityType() == EntityType.ARMOR_STAND ||
                event.getEntityType() == EntityType.ITEM_FRAME ||
                event.getEntityType() == EntityType.LEASH_KNOT ||
                event.getEntityType() == EntityType.PAINTING ||
                event.getEntityType() == EntityType.VILLAGER) {
            return;
        }
        Profile profile = profileDao.findByUUID(event.getEntity().getKiller().getUniqueId());
        int prevLevel = profile.getExperiences().getLevel();
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnKillMobIncreasePercentage()));
        int newLevel = profile.getExperiences().getLevel();
        if (newLevel > prevLevel) {
            event.getEntity().getKiller().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getEntity().getKiller().playSound(event.getEntity().getKiller().getLocation(), "entity.player.levelup", 1, 1);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        int prevLevel = profile.getExperiences().getLevel();
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnChatIncreasePercentage()));
        int newLevel = profile.getExperiences().getLevel();
        if (newLevel > prevLevel) {
            event.getPlayer().sendMessage("レベルが上がりました！ 現在のレベル: " + newLevel);
            event.getPlayer().playSound(event.getPlayer().getLocation(), "entity.player.levelup", 1, 1);
        }
    }
}
