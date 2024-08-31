package net.iamtakagi.uzsk.core;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

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
}

class ExperienceListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnBreakBlockIncreasePercentage()));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnPlaceBlockIncreasePercentage()));
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        if (event.getEntityType() == EntityType.PLAYER ||
                event.getEntityType() == EntityType.ARMOR_STAND ||
                event.getEntityType() == EntityType.ITEM_FRAME ||
                event.getEntityType() == EntityType.LEASH_HITCH ||
                event.getEntityType() == EntityType.PAINTING ||
                event.getEntityType() == EntityType.VILLAGER) {
            return;
        }
        Profile profile = profileDao.findByUUID(event.getEntity().getKiller().getUniqueId());
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnKillMobIncreasePercentage()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        ProfileDao profileDao = Core.getInstance().getProfileDao();
        CoreConfig config = Core.getInstance().getCoreConfig();
        Profile profile = profileDao.findByUUID(event.getPlayer().getUniqueId());
        profile.getExperiences().set(profile.getExperiences().size()
                + (profile.getExperiences().size() * config.getExperienceSettings().getOnChatIncreasePercentage()));
    }
}
