package net.iamtakagi.uzsk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

public class CoreConfig {
    private DatabaseSettings databaseSettings;
    private ExperienceSettings experienceSettings;
    private SidebarSettings sidebarSettings;
    private NametagSettings nametagSettings;
    private RoleSettings roleSettings;
    private WebApiSettings webApiSettings;

    public CoreConfig(YamlConfiguration yaml) {
        this.databaseSettings = new DatabaseSettings(yaml);
        this.experienceSettings = new ExperienceSettings(yaml);
        this.sidebarSettings = new SidebarSettings(yaml);
        this.nametagSettings = new NametagSettings(yaml);
        this.roleSettings = new RoleSettings(yaml);
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

    public NametagSettings getNametagSettings() {
        return nametagSettings;
    }

    public RoleSettings getRoleSettings() {
        return roleSettings;
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

    public class NametagSettings {
        boolean enabled;

        public NametagSettings(YamlConfiguration yaml) {
            this.enabled = yaml.getBoolean("nametag.enabled");
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    public class RoleSettings {
        private List<Role> roles;
        private Role defaultRole;

        public RoleSettings(YamlConfiguration yaml) {
            // Initialize the lists
            this.roles = new ArrayList<>();
            
            // Load roles from YAML
            List<Map<String, Object>> roleMaps = (List<Map<String, Object>>) yaml.get("role.roles");

            if (roleMaps != null) {
                for (Map<String, Object> roleMap : roleMaps) {
                    String name = (String) roleMap.get("name");
                    int weight = (Integer) roleMap.get("weight");
                    String permission = (String) roleMap.get("permission");
                    String prefix = (String) roleMap.get("prefix");
                    String suffix = (String) roleMap.get("suffix");

                    Role role = new Role(name, weight, permission, prefix, suffix);
                    this.roles.add(role);
                }
            }

            roles.sort((r1, r2) -> r2.getWeight() - r1.getWeight());

            // Load default role
            String defaultRoleName = yaml.getString("role.default");
            if (defaultRoleName != null) {
                this.defaultRole = findRoleByName(defaultRoleName);
            }
        }

        public Role getDefaultRole() {
            return this.defaultRole;
        }

        public List<Role> getRoles() {
            return this.roles;
        }

        private Role findRoleByName(String name) {
            for (Role role : this.roles) {
                if (role.getName().equals(name)) {
                    return role;
                }
            }
            return null; // or throw an exception if you prefer
        }

        public class Role {
            String name;
            int weight;
            String permission;
            String prefix;
            String suffix;

            public Role(String name, int weight, String permission, String prefix, String suffix) {
                this.name = name;
                this.weight = weight;
                this.permission = permission;
                this.prefix = prefix;
                this.suffix = suffix;
            }

            public String getName() {
                return this.name;
            }

            public int getWeight() {
                return this.weight;
            }

            public String getPermission() {
                return this.permission;
            }

            public String getPrefix() {
                return this.prefix;
            }

            public String getSuffix() {
                return this.suffix;
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
