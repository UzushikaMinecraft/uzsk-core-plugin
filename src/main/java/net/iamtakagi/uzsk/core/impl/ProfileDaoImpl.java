package net.iamtakagi.uzsk.core.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import net.iamtakagi.uzsk.core.Database;
import net.iamtakagi.uzsk.core.model.ProfileDao;
import net.iamtakagi.uzsk.core.model.entity.Profile;

public class ProfileDaoImpl implements ProfileDao {

    private Database database;

    public ProfileDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public void insert(Profile profile) {
        String sql = "INSERT INTO profile (uuid, initial_login_date, last_login_date, play_time, experiences, currency, total_build_blocks, total_destroy_blocks, total_mob_kills) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setString(1, profile.getUuid().toString());
            preparedStmt.setTimestamp(2, new java.sql.Timestamp(profile.getInitialLoginDate()));
            preparedStmt.setTimestamp(3, new java.sql.Timestamp(profile.getLastLoginDate()));
            preparedStmt.setLong(4, profile.getPlayTime());
            preparedStmt.setDouble(5, profile.getExperiences().size());
            preparedStmt.setInt(6, profile.getCurrency());
            preparedStmt.setInt(7, profile.getTotalBuildBlocks());
            preparedStmt.setInt(8, profile.getTotalDestroyBlocks());
            preparedStmt.setInt(9, profile.getTotalMobKills());
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Profile profile) {
        String sql = "UPDATE profile SET initial_login_date = ?, last_login_date = ?, play_time = ?, experiences = ?, currency = ?, total_build_blocks = ?, total_destroy_blocks = ?, total_mob_kills = ? WHERE uuid = ?";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setTimestamp(1, new java.sql.Timestamp(profile.getInitialLoginDate()));
            preparedStmt.setTimestamp(2, new java.sql.Timestamp(profile.getLastLoginDate()));
            preparedStmt.setLong(3, profile.getPlayTime());
            preparedStmt.setFloat(4, profile.getExperiences().size());
            preparedStmt.setInt(5, profile.getCurrency());
            preparedStmt.setInt(6, profile.getTotalBuildBlocks());
            preparedStmt.setInt(7, profile.getTotalDestroyBlocks());
            preparedStmt.setInt(8, profile.getTotalMobKills());
            preparedStmt.setString(9, profile.getUuid().toString());
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        this.database.execute("DELETE FROM profile WHERE id = " + id);
    }

    @Override
    public Profile findById(Integer id) {
        ResultSet resultSet = this.database.execute("SELECT * FROM profile WHERE id = " + id);
        try {
            if (resultSet.next()) {
                return new Profile(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getTimestamp("initial_login_date").getTime(),
                    resultSet.getTimestamp("last_login_date").getTime(),
                    resultSet.getInt("play_time"),
                    resultSet.getFloat("experiences"),
                    resultSet.getInt("currency"),
                    resultSet.getInt("total_build_blocks"),
                    resultSet.getInt("total_destroy_blocks"),
                    resultSet.getInt("total_mob_kills")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Profile findByUUID(UUID uuid) {
        ResultSet resultSet = this.database.execute("SELECT * FROM  WHERE uuid = '" + uuid.toString() + "'");
        try {
            if (resultSet.next()) {
                return new Profile(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getTimestamp("initial_login_date").getTime(),
                    resultSet.getTimestamp("last_login_date").getTime(),
                    resultSet.getInt("play_time"),
                    resultSet.getFloat("experiences"),
                    resultSet.getInt("currency"),
                    resultSet.getInt("total_build_blocks"),
                    resultSet.getInt("total_destroy_blocks"),
                    resultSet.getInt("total_mob_kills")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Profile> findAll() {
        ResultSet resultSet = this.database.execute("SELECT * FROM profile");
        try {
            while (resultSet.next()) {
                return List.of(new Profile(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getTimestamp("initial_login_date").getTime(),
                    resultSet.getTimestamp("last_login_date").getTime(),
                    resultSet.getInt("play_time"),
                    resultSet.getFloat("experiences"),
                    resultSet.getInt("currency"),
                    resultSet.getInt("total_build_blocks"),
                    resultSet.getInt("total_destroy_blocks"),
                    resultSet.getInt("total_mob_kills")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void upsert(Profile profile) {
        if (findByUUID(profile.getUuid()) == null) {
            insert(profile);
        } else {
            update(profile);
        }
    }
    
}
