package net.iamtakagi.uzsk.core;

import net.iamtakagi.uzsk.core.util.Utils;
import net.iamtakagi.uzsk.core.model.entity.Profile;
import net.iamtakagi.uzsk.core.util.TicksFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public class CoreSidebar {
    private static ScoreboardLibrary scoreboard;

    public static void init() {
        try {
            scoreboard = ScoreboardLibrary.loadScoreboardLibrary(Core.getInstance());
        } catch (NoPacketAdapterAvailableException e) {
            scoreboard = new NoopScoreboardLibrary();
        }
    }

    private Player player;
    private Sidebar sidebar;
    private ComponentSidebarLayout layout;
    private BukkitTask tickTask;

    CoreSidebar(Player player) {
      this.player = player;
    }

    public void setup() {
      this.sidebar = scoreboard.createSidebar();
      this.sidebar.addPlayer(player);
      this.tickTask = Bukkit.getScheduler().runTaskTimer(Core.getInstance(), this::tick, 0, 20);
    }

    private void tick() {
      SidebarComponent.Builder builder = SidebarComponent.builder();
      SidebarComponent title = SidebarComponent.staticLine(
          Component.text(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getCoreConfig().getSidebarSettings().getTitle())));
      for (int i = 0; i < Core.getInstance().getCoreConfig().getSidebarSettings().getLines().size(); i++) {
        String line = Core.getInstance().getCoreConfig().getSidebarSettings().getLines().get(i);
        if (line.length() > 0) {
          builder.addStaticLine(Component.text(ChatColor.translateAlternateColorCodes('&', parseLine(line))));
        } else {
          builder.addStaticLine(Component.empty());
        }
      }
      SidebarComponent component = builder.build();
      this.layout = new ComponentSidebarLayout(title, component);
      this.layout.apply(this.sidebar);
    }

    private String parseLine(String raw) {
      if (raw.contains("{ONLINE_PLAYERS}")) {
        raw = raw.replace("{ONLINE_PLAYERS}", "" + Bukkit.getOnlinePlayers().size());
      }

      if (raw.contains("{MAX_PLAYERS}")) {
        raw = raw.replace("{MAX_PLAYERS}", "" + Bukkit.getMaxPlayers());
      }

      if (raw.contains("{LEVEL}")) {
        Profile profile = Core.getInstance().getProfileDao().findByUUID(player.getUniqueId());
        raw = raw.replace("{LEVEL}", "" + profile.getExperiences().getLevel());
      }

      if (raw.contains("{EXP_PERCENTAGE}")) {
        Profile profile = Core.getInstance().getProfileDao().findByUUID(player.getUniqueId());
        raw = raw.replace("{EXP_PERCENTAGE}", "" + profile.getExperiences().getParcentage());
      }

      if (raw.contains("{PING}")) {
        raw = raw.replace("{PING}", "" + player.getPing());
      }

      if (raw.contains("{X}")) {
        raw = raw.replace("{X}", "" + player.getLocation().getBlockX());
      }

      if (raw.contains("{Y}")) {
        raw = raw.replace("{Y}", "" + player.getLocation().getBlockY());
      }

      if (raw.contains("{Z}")) {
        raw = raw.replace("{Z}", "" + player.getLocation().getBlockZ());
      }

      if (raw.contains("{DIRECTION}")) {
        raw = raw.replace("{DIRECTION}", Utils.getDirectionByFace(player.getFacing()));
      }

      if (raw.contains("{WORLD}")) {
        raw = raw.replace("{WORLD}", player.getWorld().getName());
      }

      if (raw.contains("{WORLD_DATE}")) {
        raw = raw.replace("{WORLD_DATE}", TicksFormatter.formatDate(player.getWorld().getFullTime()));
      }

      if (raw.contains("{WORLD_TIME}")) {
        raw = raw.replace("{WORLD_TIME}", TicksFormatter.formatTime(player.getWorld().getTime()));
      }

      if (raw.contains("{WEATHER}")) {
        raw = raw.replace("{WEATHER}", Utils.getWorldWeather(player.getWorld()));
      }

      if (raw.contains("{TPS}")) {
        double[] recetTps = Utils.getRecentTps();
        double avgTps = (recetTps[0] + recetTps[1] + recetTps[2]) / 3;
        raw = raw.replace("{TPS}", "" + (Math.floor(avgTps * 100)) / 100);
      }

      if (raw.contains("{DATE}")) {
        raw = raw.replace("{DATE}",
            new SimpleDateFormat(Core.getInstance().getCoreConfig().getSidebarSettings().getPatternSettings().getDatePattern())
                .format(Calendar.getInstance().getTime()));
      }

      if (raw.contains("{TIME}")) {
        raw = raw.replace("{TIME}",
            new SimpleDateFormat(Core.getInstance().getCoreConfig().getSidebarSettings().getPatternSettings().getTimePattern())
                .format(Calendar.getInstance().getTime()));
      }

      if (raw.contains("{USAGE_RAM}")) {
        raw = raw.replace("{USAGE_RAM}", String.format("%,d",
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024));
      }

      if (raw.contains("{TOTAL_RAM}")) {
        raw = raw.replace("{TOTAL_RAM}", String.format("%,d", Runtime.getRuntime().totalMemory() / 1024 / 1024));
      }

      return raw;
    }
}