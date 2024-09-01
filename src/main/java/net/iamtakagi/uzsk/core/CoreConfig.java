package net.iamtakagi.uzsk.core;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;

public class CoreConfig {
    private DatabaseSettings databaseSettings;
    private ExperienceSettings experienceSettings;
    private SidebarSettings sidebarSettings;
    private WebApiSettings webApiSettings;

    public CoreConfig(YamlConfiguration yaml) {
        this.databaseSettings = new DatabaseSettings(yaml);
        this.experienceSettings = new ExperienceSettings(yaml);
        this.sidebarSettings = new SidebarSettings(yaml);
        this.webApiSettings = new WebApiSettings(yaml);
    }

    public DatabaseSettings getDatabaseSettings() {
        return databaseSettings;
    }

    public ExperienceSettings getExperienceSettings() {
        return experienceSettings;
    }

    public SidebarSettings getSidebarSettings() {
        return sidebarSettings;
    }

    public WebApiSettings getWebApiSettings() {
        return webApiSettings;
    }

    public class DatabaseSettings {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;

        public DatabaseSettings(YamlConfiguration yaml) {
            this.host = yaml.getString("mysql.host");
            this.port = yaml.getInt("mysql.port");
            this.database = yaml.getString("mysql.database");
            this.username = yaml.getString("mysql.username");
            this.password = yaml.getString("mysql.password");
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

    public class ExperienceSettings {
        private float onLoginNetworkIncrements;
        private float onBreakBlockIncrements;
        private float onPlaceBlockIncrements;
        private float onKillMobIncrements;
        private float onChatIncrements;

        public ExperienceSettings(YamlConfiguration yaml) {
            this.onLoginNetworkIncrements = (float) yaml.getDouble("experience.on_login_network.increments");
            this.onBreakBlockIncrements = (float) yaml.getDouble("experience.on_break_block.increments");
            this.onPlaceBlockIncrements = (float) yaml.getDouble("experience.on_place_block.increments");
            this.onKillMobIncrements = (float) yaml.getDouble("experience.on_kill_mob.increments");
            this.onChatIncrements = (float) yaml.getDouble("experience.on_chat.increments");
        }

        public float getOnLoginNetworkIncrements() {
            return this.onLoginNetworkIncrements;
        }

        public float getOnBreakBlockIncrements() {
            return this.onBreakBlockIncrements;
        }

        public float getOnPlaceBlockIncrements() {
            return this.onPlaceBlockIncrements;
        }

        public float getOnKillMobIncrements() {
            return this.onKillMobIncrements;
        }

        public float getOnChatIncrements() {
            return this.onChatIncrements;
        }
    }

    public class SidebarSettings {
        private boolean enabled;
        private String title;
        private List<String> lines;
        private int expProgressBarSize;
        private PatternSettings patternSettings;

        public SidebarSettings(YamlConfiguration yaml) {
            this.enabled = yaml.getBoolean("sidebar.enabled");
            this.title = yaml.getString("sidebar.title");
            this.lines = yaml.getStringList("sidebar.lines");
            this.expProgressBarSize = yaml.getInt("sidebar.exp_progress_bar_size");
            this.patternSettings = new PatternSettings(yaml);
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public String getTitle() {
            return this.title;
        }

        public List<String> getLines() {
            return this.lines;
        }

        public int getExpProgressBarSize() {
            return this.expProgressBarSize;
        }

        public PatternSettings getPatternSettings() {
            return this.patternSettings;
        }

        public class PatternSettings {
            private String datePattern;
            private String timePattern;
            private String worldDatePattern;
            private String worldTimePattern;

            public PatternSettings(YamlConfiguration yaml) {
                this.datePattern = yaml.getString("sidebar.pattern.date");
                this.timePattern = yaml.getString("sidebar.pattern.time");
                this.worldDatePattern = yaml.getString("sidebar.pattern.world_date");
                this.worldTimePattern = yaml.getString("sidebar.pattern.world_time");
            }

            public String getDatePattern() {
                return this.datePattern;
            }

            public String getTimePattern() {
                return this.timePattern;
            }

            public String getWorldDatePattern() {
                return this.worldDatePattern;
            }

            public String getWorldTimePattern() {
                return this.worldTimePattern;
            }
        }
    }

    public class WebApiSettings {
        private String endpointUrl;

        public WebApiSettings(YamlConfiguration yaml) {
            this.endpointUrl = yaml.getString("webapi.endpoint_url");
        }

        public String getEndpointUrl() {
            return this.endpointUrl;
        }
    
    }
}
