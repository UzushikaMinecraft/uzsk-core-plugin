package net.iamtakagi.uzsk.core;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.geyser.api.GeyserApi;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

@Plugin(id = "core", name = "UZSK Core Plugin", version = "1.0.0-SNAPSHOT", url = "https://uzsk.iamtakagi.net", description = "UZSK Core Plugin", authors = {
        "iamtakagi" })
public class Core {

    private final ProxyServer server;
    private final Logger logger;

    private GeyserApi geyserApi;
    private FloodgateApi floodgateApi;
    private Database database;

    @Inject
    @DataDirectory
    private Path dataDirectory;
    private CoreConfig config;

    @Inject
    public Core(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public void loadConfig() throws IOException {
        if (Files.notExists(dataDirectory)) {
            try {
                Files.createDirectory(dataDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final Path config = dataDirectory.resolve("config.yml");
        if (Files.notExists(config)) {
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                try {
                    Files.copy(stream, config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(config).build();
        final CommentedConfigurationNode node = loader.load();
        this.config = new CoreConfig(node);
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event)  {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.database = new Database(config.getDatabaseSettings().getHost(), config.getDatabaseSettings().getPort(),
                            config.getDatabaseSettings().getUsername(), config.getDatabaseSettings().getPassword(),
                            config.getDatabaseSettings().getDatabase());
        this.geyserApi = GeyserApi.api();
        this.floodgateApi = FloodgateApi.getInstance();
        server.getEventManager().register(this, new Listener(geyserApi, floodgateApi, database));

        this.logger.info("Core has been initialized!");
    }
}

class Listener {
    private final GeyserApi geyserApi;
    private final FloodgateApi floodgateApi;
    private final Database database;

    public Listener(GeyserApi geyserApi, FloodgateApi floodgateApi, Database database) {
        this.geyserApi = geyserApi;
        this.floodgateApi = floodgateApi;
        this.database = database;
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (geyserApi.isBedrockPlayer(event.getPlayer().getUniqueId())) {
            FloodgatePlayer floodgatePlayer = floodgateApi.getPlayer(event.getPlayer().getUniqueId());
            if (floodgatePlayer == null) {
                return;
            }
            String sql = "INSERT INTO bedrock (fuid, xuid) VALUES (?, ?)";
            try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
                preparedStmt.setString(1, event.getPlayer().getUniqueId().toString());
                preparedStmt.setString(2, floodgatePlayer.getXuid());
                preparedStmt.execute();
                preparedStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

class CoreConfig {
    private DatabaseSettings databaseSettings;

    public CoreConfig(CommentedConfigurationNode node) {
        this.databaseSettings = new DatabaseSettings(node);
    }

    public DatabaseSettings getDatabaseSettings() {
        return databaseSettings;
    }

    public class DatabaseSettings {
        private String host;
        private String port;
        private String database;
        private String username;
        private String password;

        public DatabaseSettings(CommentedConfigurationNode node) {
            this.host = node.getString("mysql.host");
            this.port = node.getString("mysql.port");
            this.database = node.getString("mysql.database");
            this.username = node.getString("mysql.username");
            this.password = node.getString("mysql.password");
        }

        public String getHost() {
            return host;
        }

        public String getPort() {
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
}