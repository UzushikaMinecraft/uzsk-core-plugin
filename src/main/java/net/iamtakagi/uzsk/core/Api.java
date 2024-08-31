package net.iamtakagi.uzsk.core;

import java.util.UUID;

import net.iamtakagi.uzsk.core.model.entity.Profile;

public class Api {
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
