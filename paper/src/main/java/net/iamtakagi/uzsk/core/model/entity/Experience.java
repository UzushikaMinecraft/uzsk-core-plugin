package net.iamtakagi.uzsk.core.model.entity;

import net.iamtakagi.iroha.Style;

public class Experience {

    public float size;

    public Experience(float size) {
        this.size = size;
    }

    public void increase(float i) {
        this.size += i;
    }

    public void decrease(float d) {
        this.size -= d;
    }

    public void set(float s) {
        this.size = s;
    }

    public float size() {
        return size;
    }

    // 現在のレベルを取得するメソッド
    public int getCurrentLevel() {
        int level = 0;
        int xp = (int) size;
        while (xp >= getExperienceForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    // 特定レベルに必要な経験値を取得するメソッド
    public int getExperienceForLevel(int level) {
        // Minecraftの経験値の計算式
        if (level < 0) return 0;
        if (level <= 16) return level * level + 2 * level;
        if (level <= 31) return (int) (0.5 * level * level + 40.5 * level - 194);
        return (int) (1.5 * level * level - 40.5 * level + 360);
    }

    // 現在のレベルでの経験値を取得するメソッド
    public float getExperienceForCurrentLevel() {
        int currentLevel = getCurrentLevel();
        int currentLevelExp = getExperienceForLevel(currentLevel);
        return size - currentLevelExp;
    }

    // レベルアップに必要な経験値を取得するメソッド
    public float getExperienceToLevelUp() {
        int currentLevel = getCurrentLevel();
        int nextLevelExp = getExperienceForLevel(currentLevel + 1);
        return nextLevelExp - size;
    }

    // レベルアップまでのパーセンテージを取得するメソッド
    public float getPercentageToNextLevel() {
        int currentLevel = getCurrentLevel();
        int currentLevelExp = getExperienceForLevel(currentLevel);
        int nextLevelExp = getExperienceForLevel(currentLevel + 1);
        float progress = (size - currentLevelExp) / (float) (nextLevelExp - currentLevelExp) * 100;
        return Math.min(progress, 100.0f); // パーセンテージが100%を超えないように
    }

    // プログレスバーを文字列として返すメソッド
    public String getProgressBar(int barLength) {
        int totalBars = barLength; // バーの長さ
        float percentage = getPercentageToNextLevel();
        int filledBars = Math.round(totalBars * (percentage / 100.0f)); // 塗りつぶされたバーの数

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                sb.append(Style.GREEN + "|");
            } else {
                sb.append(Style.GRAY + "|");
            }
        }
        return sb.toString();
    }
}
