package net.iamtakagi.uzsk.core.model;

import java.util.List;
import java.util.UUID;

import net.iamtakagi.uzsk.core.model.entity.Profile;

public interface ProfileDao {
    void insert(Profile profile);
	void update(Profile profile);
	void upsert(Profile profile);
	void deleteById(Integer id);
	Profile findById(Integer id);
	Profile findByUUID(UUID uuid);
    List<Profile> findAll();
}