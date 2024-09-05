package net.iamtakagi.uzsk.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.iamtakagi.kodaka.Kodaka;
import net.iamtakagi.medaka.Medaka;
import net.iamtakagi.uzsk.core.command.ProfileCommand;
import net.iamtakagi.uzsk.core.model.DaoFactory;
import net.iamtakagi.uzsk.core.model.ProfileDao;

public class Core extends JavaPlugin {

  private static Core instance;
  private CoreConfig config;
  private Database database;
  private ProfileDao profileDao;

  @Override
  public void onEnable() {
    instance = this;
    this.saveDefaultConfig();
    this.loadConfig();
    this.config = new CoreConfig((YamlConfiguration) this.getConfig());
    if (config.getNametagSettings().isEnabled() && !getServer().getPluginManager().isPluginEnabled("NametagEdit")) {
      getLogger().severe("NametagEdit is not enabled or installed, Disabling...");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    this.database = new Database(
      this.config.getDatabaseSettings().getHost(),
      this.config.getDatabaseSettings().getPort(),
      this.config.getDatabaseSettings().getUsername(),
      this.config.getDatabaseSettings().getPassword(),
      this.config.getDatabaseSettings().getDatabase()
    );
    this.profileDao = new DaoFactory(database).createProfileDao();
    this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
    this.getServer().getPluginManager().registerEvents(new ExperienceListener(), this);
    Medaka.init(this);
    Kodaka kodaka = new Kodaka(this);
    kodaka.registerCommand(new ProfileCommand());
    if (this.config.getSidebarSettings().isEnabled()) {
      CoreSidebar.init();
    }
    Bukkit.getOnlinePlayers().forEach(player -> {
      CorePlayer corePlayer = new CorePlayer(player.getUniqueId());
      corePlayer.init();
    });
  }

  @Override
  public void onDisable() {
    this.saveDefaultConfig();
  }

  public static Core getInstance() {
    return instance;
  }

  public CoreConfig getCoreConfig() {
    return config;
  }

  public ProfileDao getProfileDao() {
    return profileDao;
  }

  private void loadConfig() {
    this.config = new CoreConfig((YamlConfiguration) this.getConfig());
  }
}