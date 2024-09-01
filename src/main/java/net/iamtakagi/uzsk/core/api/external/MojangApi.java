package net.iamtakagi.uzsk.core.api.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MojangApi {

    private static final String API_BASEURL = "https://sessionserver.mojang.com";

    /**
     * UUID から Username を返却します
     * 
     * @param uuid
     * @return
     */
    public static String getUsernameByUUID(UUID uuid) {
        String endpoint = API_BASEURL + "/session/minecraft/profile/" + uuid.toString().replace("-", "");
        try {
            String res = IOUtils.toString(new URL(endpoint), StandardCharsets.UTF_8);
            JsonObject obj = (JsonObject) JsonParser.parseString(res);
            return obj.get("name").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UUID getUUIDByName(String name) {
        String uuid = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = (((JsonObject) new JsonParser().parse(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            System.out.println("Unable to get UUID of: " + name + "!");
            uuid = "er";
        }
        return UUID.fromString(uuid);
    }
}
