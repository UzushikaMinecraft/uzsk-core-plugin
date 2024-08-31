package net.iamtakagi.uzsk.core.model.entity;

import java.io.Serializable;
import java.util.UUID;

public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;
    
	private Integer id;
	private UUID uuid;
	private long initialLoginDate;
	private long lastLoginDate;
	private long playTime;
	private double experiences;
	private int currency;
	private int totalBuildBlocks;
	private int totalDestroyBlocks;
	private int totalMobKills;

	public Profile(UUID uuid, long initialLoginDate, long lastLoginDate, int playTime, double experiences, int currency,
			int totalBuildBlocks, int totalDestroyBlocks, int totalMobKills) {
		this.uuid = uuid;
		this.initialLoginDate = initialLoginDate;
		this.lastLoginDate = lastLoginDate;
		this.playTime = playTime;
		this.experiences = experiences;
		this.currency = currency;
		this.totalBuildBlocks = totalBuildBlocks;
		this.totalDestroyBlocks = totalDestroyBlocks;
		this.totalMobKills = totalMobKills;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public long getInitialLoginDate() {
		return initialLoginDate;
	}

	public void setInitialLoginDate(long initialLoginDate) {
		this.initialLoginDate = initialLoginDate;
	}

	public long getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(long lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	public double getExperiences() {
		return experiences;
	}

	public void setExperiences(double experiences) {
		this.experiences = experiences;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getTotalBuildBlocks() {
		return totalBuildBlocks;
	}

	public void setTotalBuildBlocks(int totalBuildBlocks) {
		this.totalBuildBlocks = totalBuildBlocks;
	}

	public int getTotalDestroyBlocks() {
		return totalDestroyBlocks;
	}

	public void setTotalDestroyBlocks(int totalDestroyBlocks) {
		this.totalDestroyBlocks = totalDestroyBlocks;
	}

	public int getTotalMobKills() {
		return totalMobKills;
	}

	public void setTotalMobKills(int totalMobKills) {
		this.totalMobKills = totalMobKills;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", uuid=" + uuid + ", initialLoginDate=" + initialLoginDate + ", lastLoginDate="
				+ lastLoginDate + ", playTime=" + playTime + ", experiences=" + experiences + ", currency=" + currency
				+ ", totalBuildBlocks=" + totalBuildBlocks + ", totalDestroyBlocks=" + totalDestroyBlocks
				+ ", totalMobKills=" + totalMobKills + "]";
	}
}
