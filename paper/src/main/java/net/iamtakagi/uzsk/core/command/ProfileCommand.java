package net.iamtakagi.uzsk.core.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.iamtakagi.iroha.Style;
import net.iamtakagi.kodaka.annotation.CommandMeta;
import net.iamtakagi.uzsk.core.Core;
import net.iamtakagi.uzsk.core.CorePlayer;
import net.iamtakagi.uzsk.core.api.external.MojangApi;
import net.iamtakagi.uzsk.core.menu.ProfileMenu;
import net.iamtakagi.uzsk.core.model.entity.Profile;

@CommandMeta(label = { "profile" }, permission = "uzsk.command.profile.self", subcommands = true)
public class ProfileCommand {
    public void execute(Player player) {
        CorePlayer corePlayer = CorePlayer.getCorePlayer(player.getUniqueId());
        new ProfileMenu(Core.getInstance(), corePlayer.getProfile()).openMenu(player);
    }

    @CommandMeta(label = { "view" }, permission = "uzsk.command.profile.view", subcommands = true)
    public class ProfileViewCommand extends ProfileCommand {
        public void execute(Player player, String playerName) {
            UUID uuid = MojangApi.getUUIDByName(playerName);
            if (uuid == null) {
                player.sendMessage(Style.RESET + "そのプレイヤーは存在しません。");
                return;
            }
            Profile profile = Core.getInstance().getProfileDao().findByUUID(uuid);
            if (profile == null) {
                player.sendMessage(Style.RESET + "そのプレイヤーは存在しません。");
                return;
            }
            new ProfileMenu(Core.getInstance(), profile).openMenu(player);
        }
    }
}
