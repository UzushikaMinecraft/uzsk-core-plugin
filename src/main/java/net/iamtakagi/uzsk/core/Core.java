package net.iamtakagi.uzsk.core;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
    this.database = new Database(
      this.config.getDatabaseSettings().getHost(),
      this.config.getDatabaseSettings().getPort(),
      this.config.getDatabaseSettings().getDatabase(),
      this.config.getDatabaseSettings().getUsername(),
      this.config.getDatabaseSettings().getPassword()
    );
    this.profileDao = new DaoFactory(database).createProfileDao();
    this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
    this.getServer().getPluginManager().registerEvents(new ExperienceListener(), this);
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