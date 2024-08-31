package net.iamtakagi.uzsk.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public class CoreBossbar {
    private static Map<UUID, CoreBossbar> bossBars = new HashMap<>();

    public static Map<UUID, CoreBossbar> getCoreBossbars() {
        return bossBars;
    }

    private Player player;
    private String name;
    private List<BossBar> bars;
    private BukkitTask tickTask;

    public CoreBossbar(Player player) {
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public String parseContent (String content) {
        if (content.contains("{LEVEL}")) {
            content = content.replace("{LEVEL}", String.valueOf(Core.getInstance().getProfileDao().findByUUID(player.getUniqueId()).getExperiences().getLevel()));
        }
        if (content.contains("{EXP}")) {
            content = content.replace("{EXP}", String.valueOf(Core.getInstance().getProfileDao().findByUUID(player.getUniqueId()).getExperiences().size()));
        }
        if (content.contains("{EXP_PERCENTAGE}")) {
            content = content.replace("{EXP_PERCENTAGE}", String.valueOf(Core.getInstance().getProfileDao().findByUUID(player.getUniqueId()).getExperiences().getParcentage()));
        }
        if (content.contains("{EXP_PROGRESS_BAR}")) {
            content = content.replace("{EXP_PROGRESS_BAR}", Core.getInstance().getProfileDao().findByUUID(player.getUniqueId()).getExperiences().getProgressBar());
        }
        return content;
    }

    public void show() {
        Core.getInstance().getCoreConfig().getBossbarSettings().getBossbarDetailSettings().forEach(s -> {
            BossBar bossBar = BossBar.bossBar(Component.text(parseContent(s.getContent())), s.getProgress(), s.getColor(), s.getOverlay());
            bossBar.addViewer((Audience) player);
            bars.add(bossBar);
        });
    }

    public void hide() {
        bars.forEach(b -> b.removeViewer((Audience) player));
    }

    public void update() {
        hide();
        show();
    }

    public void tick() {
        update();
    }

    public void init() {
        show();
        this.tickTask = Bukkit.getScheduler().runTaskTimer(Core.getInstance(), this::tick, 0, 20);
    }

}
