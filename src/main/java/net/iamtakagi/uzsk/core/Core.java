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

class CoreConfig {
  private DatabaseSettings databaseSettings;
  private ExperienceSettings experienceSettings;

   public CoreConfig(YamlConfiguration config) {
    this.databaseSettings = new DatabaseSettings(
      config.getString("database.host"),
      config.getInt("database.port"),
      config.getString("database.database"),
      config.getString("database.username"),
      config.getString("database.password")
    );
    this.experienceSettings = new ExperienceSettings(
      config.getDouble("experience.on_login_network_increase_percentage"),
      config.getDouble("experience.on_break_block_increase_percentage"),
      config.getDouble("experience.on_place_block_increase_percentage"),
      config.getDouble("experience.on_kill_mob_increase_percentage"),
      config.getDouble("experience.on_chat_increase_percentage")
    );
  }

  public DatabaseSettings getDatabaseSettings() {
    return databaseSettings;
  }

  public ExperienceSettings getExperienceSettings() {
    return experienceSettings;
  }

  class DatabaseSettings {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public DatabaseSettings(String host, int port, String database, String username, String password) {
      this.host = host;
      this.port = port;
      this.database = database;
      this.username = username;
      this.password = password;
    }

    public String getHost() {
      return host;
    }

    public int getPort() {
      return port;
    }

    public String getDatabase() {
      return database;
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }
  }

  class ExperienceSettings {
    private double onLoginNetworkIncreasePercentage;
    private double onBreakBlockIncreasePercentage;
    private double onPlaceBlockIncreasePercentage;
    private double onKillMobIncreasePercentage;
    private double onChatIncreasePercentage;

    public ExperienceSettings(double onLoginNetworkIncreasePercentage, double onBreakBlockIncreasePercentage, double onPlaceBlockIncreasePercentage, double onKillMobIncreasePercentage, double onChatIncreasePercentage) {
      this.onLoginNetworkIncreasePercentage = onLoginNetworkIncreasePercentage;
      this.onBreakBlockIncreasePercentage = onBreakBlockIncreasePercentage;
      this.onPlaceBlockIncreasePercentage = onPlaceBlockIncreasePercentage;
      this.onKillMobIncreasePercentage = onKillMobIncreasePercentage;
      this.onChatIncreasePercentage = onChatIncreasePercentage;
    }

    public double getOnLoginNetworkIncreasePercentage() {
      return onLoginNetworkIncreasePercentage;
    }

    public double getOnBreakBlockIncreasePercentage() {
      return onBreakBlockIncreasePercentage;
    }

    public double getOnPlaceBlockIncreasePercentage() {
      return onPlaceBlockIncreasePercentage;
    }

    public double getOnKillMobIncreasePercentage() {
      return onKillMobIncreasePercentage;
    }

    public double getOnChatIncreasePercentage() {
      return onChatIncreasePercentage;
    }
  }
}