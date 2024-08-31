package net.iamtakagi.uzsk.core;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;

public class CoreConfig {
    private DatabaseSettings databaseSettings;
    private ExperienceSettings experienceSettings;
    private SidebarSettings sidebarSettings;
    private BossbarSettings bossbarSettings;
    private WebApiSettings webApiSettings;

    public CoreConfig(YamlConfiguration yaml) {
        this.databaseSettings = new DatabaseSettings(yaml);
        this.experienceSettings = new ExperienceSettings(yaml);
        this.sidebarSettings = new SidebarSettings(yaml);
        this.bossbarSettings = new BossbarSettings(yaml);
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

    public BossbarSettings getBossbarSettings() {
        return bossbarSettings;
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
        private double onLoginNetworkIncreasePercentage;
        private double onBreakBlockIncreasePercentage;
        private double onPlaceBlockIncreasePercentage;
        private double onKillMobIncreasePercentage;
        private double onChatIncreasePercentage;

        public ExperienceSettings(YamlConfiguration yaml) {
            this.onLoginNetworkIncreasePercentage = yaml.getDouble("experience.on_login_network.increase_percentage");
            this.onBreakBlockIncreasePercentage = yaml.getDouble("experience.on_break_block.increase_percentage");
            this.onPlaceBlockIncreasePercentage = yaml.getDouble("experience.on_place_block.increase_percentage");
            this.onKillMobIncreasePercentage = yaml.getDouble("experience.on_kill_mob.increase_percentage");
            this.onChatIncreasePercentage = yaml.getDouble("experience.on_chat.increase_percentage");
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

    public class SidebarSettings {
        private boolean enabled;
        private String title;
        private List<String> lines;
        private PatternSettings patternSettings;

        public SidebarSettings(YamlConfiguration yaml) {
            this.enabled = yaml.getBoolean("sidebar.enabled");
            this.title = yaml.getString("sidebar.title");
            this.lines = yaml.getStringList("sidebar.lines");
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

    class BossbarSettings {
        private boolean enabled;
        private List<BossbarDetailSettings> bossbarDetailSettings;

        public BossbarSettings(YamlConfiguration yaml) {
            this.enabled = yaml.getBoolean("bossbar.enabled");
            this.bossbarDetailSettings = yaml.getStringList("bossbar.bars").stream()
                    .map(detail -> new BossbarDetailSettings((YamlConfiguration) yaml.get(detail)))
                    .toList();
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public List<BossbarDetailSettings> getBossbarDetailSettings() {
            return this.bossbarDetailSettings;
        }

        class BossbarDetailSettings {
            private String name;
            private String content;
            private Color color;
            private Overlay overlay;
            private float progress;

            public BossbarDetailSettings(YamlConfiguration yaml) {
                this.name = yaml.getString("bossbar.name");
                this.content = yaml.getString("bossbar.content");
                this.color = Color.valueOf(yaml.getString("bossbar.color"));
                this.overlay = Overlay.valueOf(yaml.getString("bossbar.overlay"));
                this.progress = (float) yaml.getDouble("bossbar.progress");
            }

            public String getName() {
                return this.name;
            }

            public String getContent() {
                return this.content;
            }

            public Color getColor() {
                return this.color;
            }

            public Overlay getOverlay() {
                return this.overlay;
            }

            public float getProgress() {
                return this.progress;
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
