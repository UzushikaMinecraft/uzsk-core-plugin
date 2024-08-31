package net.iamtakagi.uzsk.core;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

public class Experiences {

    private static Map<Integer, Integer> LEVELING = new HashMap<>();

    static {
        for (int i = 0; i <= 1024; i++) {
            LEVELING.put(i, i * 256);
         }
    }

    public double size;

    public Experiences(double size) {
        this.size = size;
    }

    public int getLevel() {
        int level = 0;
        for (int i = 0; i <= 1024; i++) {
            if (size >= LEVELING.get(i)) {
                level = i;
            }
        }
        return level;
    }

    public int getNeededExp() {
        return LEVELING.get(getLevel() + 1);
    }

    public void increase(double i) {
        this.size += i;
    }

    public void decrease(double d) {
        this.size -= d;
    }

    public void set(double exp) {
        this.size = exp;
    }

    public double size() {
        return size;
    }

    public double getParcentage() {
        return size / getNeededExp();
    }

    public String getProgressBar () {
        StringBuilder sb = new StringBuilder();
        int progress = (int) (getParcentage() * 100);
        for (int i = 0; i < 100; i++) {
            if (i < progress) {
                sb.append(ChatColor.GREEN + "|");
            } else {
                sb.append(ChatColor.GRAY + "|");
            }
        }
        return sb.toString();
    }
}
