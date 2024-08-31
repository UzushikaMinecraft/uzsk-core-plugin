package net.iamtakagi.uzsk.core.api;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.iamtakagi.uzsk.core.Core;
import net.iamtakagi.uzsk.core.CoreConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class ServerStatus {
    private String name;
    private String description;
    private boolean isOnline;
    private int onlinePlayers;
    private int maxPlayers;
    private String[] playersSample;

    public ServerStatus(String name, String description, boolean isOnline, int onlinePlayers, int maxPlayers, String[] playersSample) {
        this.name = name;
        this.description = description;
        this.isOnline = isOnline;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.playersSample = playersSample;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String[] getPlayersSample() {
        return playersSample;
    }
}

public class WebApi {

    public static ServerStatus getServerStatus() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Core.getInstance().getCoreConfig().getWebApiSettings().getEndpointUrl()).build();
        Response response = client.newCall(request).execute();
        String raw = response.body().string();
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(raw, JsonObject.class);
        return new ServerStatus(
            json.get("name").getAsString(),
            json.get("description").getAsString(),
            json.get("isOnline").getAsBoolean(),
            json.get("onlinePlayers").getAsInt(),
            json.get("maxPlayers").getAsInt(),
            gson.fromJson(json.get("playersSample").getAsJsonArray(), String[].class)
        );
    }
}
