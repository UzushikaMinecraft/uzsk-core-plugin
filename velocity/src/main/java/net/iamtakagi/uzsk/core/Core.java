package net.iamtakagi.uzsk.core;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.slf4j.Logger;

@Plugin(id = "core", name = "Core", version = "1.0.0-SNAPSHOT", url = "https://uzsk.iamtakagi.net", description = "UZSK Core Plugin", authors = {
        "iamtakagi" }, dependencies = {
                @Dependency(id = "geyser", optional = false),
                @Dependency(id = "floodgate", optional = false)
        })
public class Core implements EventRegistrar {

    private final ProxyServer server;
    private final Logger logger;
    private Database database;

    @Inject
    public Core(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.database = new Database("uzsk_db", "3306", "root", "uzsk", "uzsk");
        GeyserApi.api().eventBus().register(this, this);
        GeyserApi.api().eventBus().subscribe(this, SessionJoinEvent.class, this::onJoin);
        this.logger.info("Core has been initialized!");
    }

    @Subscribe
    public void onJoin(SessionJoinEvent event) {
        GeyserConnection connection = event.connection();
        if (connection == null) {
            return;
        }
        UUID uuid = connection.playerUuid();
        if (uuid == null) {
            return;
        }
        if (GeyserApi.api().isBedrockPlayer(uuid)) {
            FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(uuid);
            if (floodgatePlayer == null) {
                return;
            }
            String fuid = uuid.toString();
            String checkExistsSql = "SELECT * FROM bedrock WHERE fuid = ?";
            try (PreparedStatement preparedStmt = this.database.getConnection().prepareStatement(checkExistsSql)) {
                preparedStmt.setString(1, fuid);
                ResultSet rs = preparedStmt.executeQuery();
                if (rs != null && rs.next()) {
                    preparedStmt.close();
                    return;
                }
                preparedStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String xuid = floodgatePlayer.getXuid();
            String insertSql = "INSERT INTO bedrock (fuid, xuid) VALUES (?, ?)";
            try (PreparedStatement preparedStmt = this.database.getConnection().prepareStatement(insertSql)) {
                preparedStmt.setString(1, fuid);
                preparedStmt.setString(2, xuid);
                preparedStmt.execute();
                preparedStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}