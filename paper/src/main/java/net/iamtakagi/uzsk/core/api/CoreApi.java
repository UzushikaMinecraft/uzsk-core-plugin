package net.iamtakagi.uzsk.core.api;

import java.util.UUID;

import net.iamtakagi.uzsk.core.Core;
import net.iamtakagi.uzsk.core.model.entity.Profile;

public class CoreApi {
    public static Profile getProfileByUUID (UUID uuid) {
        return Core.getInstance().getProfileDao().findByUUID(uuid);
    }

    public static void saveProfile (Profile profile) {
        Core.getInstance().getProfileDao().update(profile);
    }

    public static void createProfile (Profile profile) {
        Core.getInstance().getProfileDao().insert(profile);
    }

    public static void deleteProfile (Profile profile) {
        Core.getInstance().getProfileDao().deleteById(profile.getId());
    }
}
