package net.iamtakagi.uzsk.core.menu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.iamtakagi.iroha.ItemBuilder;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.iroha.TimeUtil;
import net.iamtakagi.medaka.Button;
import net.iamtakagi.medaka.Menu;
import net.iamtakagi.uzsk.core.model.entity.Profile;

public class ProfileMenu extends Menu {

    private final Profile profile;

    public ProfileMenu(Plugin instance, Profile profile) {
        super(instance);
        this.profile = profile;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button(instance) {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAPER).name(Style.PINK + "統計").lore(
                    Style.WHITE + "ID: " + profile.getName(),
                    Style.WHITE + "UUID: " + profile.getUuid(),
                    Style.SCOREBAORD_SEPARATOR,
                    Style.WHITE + "初回ログイン日時: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(profile.getInitialLoginDate())),
                    Style.WHITE + "最終ログイン日時: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(profile.getLastLoginDate())),
                    Style.WHITE + "総プレイ時間: " + TimeUtil.millisToRoundedTime(profile.getTotalPlayTime()),
                    Style.SCOREBAORD_SEPARATOR,
                    Style.WHITE + "レベル: " + profile.getExperience().getCurrentLevel(),
                    Style.WHITE + "総経験値: " + profile.getExperience().size(),
                    profile.getExperience().getProgressBar(100) + " " + profile.getExperience().getPercentageToNextLevel() + "%",
                    Style.SCOREBAORD_SEPARATOR,
                    Style.WHITE + "通貨: " + profile.getCurrency(),
                    Style.WHITE + "ブロック破壊数: " + profile.getTotalDestroyBlocks(),
                    Style.WHITE + "ブロック設置数: " + profile.getTotalBuildBlocks(),
                    Style.WHITE + "倒したモブの数: " + profile.getTotalMobKills(),
                    Style.SCOREBAORD_SEPARATOR
                ).build();
            }
        });
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "プロフィール";
    }    
}
