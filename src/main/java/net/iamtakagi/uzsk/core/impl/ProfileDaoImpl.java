package net.iamtakagi.uzsk.core.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String sql = "INSERT INTO profile (uuid, initial_login_date, last_login_date, total_play_time, experience, currency, total_build_blocks, total_destroy_blocks, total_mob_kills) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setString(1, profile.getUuid().toString());
            preparedStmt.setTimestamp(2, new java.sql.Timestamp(profile.getInitialLoginDate()));
            preparedStmt.setTimestamp(3, new java.sql.Timestamp(profile.getLastLoginDate()));
            preparedStmt.setLong(4, profile.getTotalPlayTime());
            preparedStmt.setDouble(5, profile.getExperience().size());
            preparedStmt.setInt(6, profile.getCurrency());
            preparedStmt.setInt(7, profile.getTotalBuildBlocks());
            preparedStmt.setInt(8, profile.getTotalDestroyBlocks());
            preparedStmt.setInt(9, profile.getTotalMobKills());
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Profile profile) {
        String sql = "UPDATE profile SET initial_login_date = ?, last_login_date = ?, total_play_time = ?, experience = ?, currency = ?, total_build_blocks = ?, total_destroy_blocks = ?, total_mob_kills = ? WHERE uuid = ?";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setTimestamp(1, new java.sql.Timestamp(profile.getInitialLoginDate()));
            preparedStmt.setTimestamp(2, new java.sql.Timestamp(profile.getLastLoginDate()));
            preparedStmt.setLong(3, profile.getTotalPlayTime());
            preparedStmt.setFloat(4, profile.getExperience().size());
            preparedStmt.setInt(5, profile.getCurrency());
            preparedStmt.setInt(6, profile.getTotalBuildBlocks());
            preparedStmt.setInt(7, profile.getTotalDestroyBlocks());
            preparedStmt.setInt(8, profile.getTotalMobKills());
            preparedStmt.setString(9, profile.getUuid().toString());
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM profile WHERE id = ?";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Profile findById(Integer id) {
        String sql = "SELECT * FROM profile WHERE id = ?";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            ResultSet resultSet = preparedStmt.executeQuery();
            return new Profile(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getTimestamp("initial_login_date").getTime(),
                resultSet.getTimestamp("last_login_date").getTime(),
                resultSet.getInt("total_play_time"),
                resultSet.getFloat("experience"),
                resultSet.getInt("currency"),
                resultSet.getInt("total_build_blocks"),
                resultSet.getInt("total_destroy_blocks"),
                resultSet.getInt("total_mob_kills")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Profile findByUUID(UUID uuid) {
        String sql = "SELECT * FROM profile WHERE uuid = ?";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            preparedStmt.setString(1, uuid.toString());
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()) {
                return new Profile(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getTimestamp("initial_login_date").getTime(),
                    resultSet.getTimestamp("last_login_date").getTime(),
                    resultSet.getInt("total_play_time"),
                    resultSet.getFloat("experience"),
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
        String sql = "SELECT * FROM profile";
        try (PreparedStatement preparedStmt = database.getConnection().prepareStatement(sql)) {
            ResultSet resultSet = preparedStmt.executeQuery();
            List<Profile> profiles = new ArrayList<>();
            while (resultSet.next()) {
                profiles.add(new Profile(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getTimestamp("initial_login_date").getTime(),
                    resultSet.getTimestamp("last_login_date").getTime(),
                    resultSet.getInt("total_play_time"),
                    resultSet.getFloat("experience"),
                    resultSet.getInt("currency"),
                    resultSet.getInt("total_build_blocks"),
                    resultSet.getInt("total_destroy_blocks"),
                    resultSet.getInt("total_mob_kills")
                ));
            }
            return profiles;
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
