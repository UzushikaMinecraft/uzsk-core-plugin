package net.iamtakagi.uzsk.core.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class Utils {
    public static String getDirectionByFace(BlockFace face) {
      switch (face) {
        case NORTH:
          return "北";
        case EAST:
          return "東";
        case SOUTH:
          return "南";
        case WEST:
          return "西";
        case UP:
          return "上";
        case DOWN:
          return "下";
        case NORTH_EAST:
          return "北東";
        case NORTH_WEST:
          return "北西";
        case SOUTH_EAST:
          return "南東";
        case SOUTH_WEST:
          return "南西";
        case WEST_NORTH_WEST:
          return "西北西";
        case NORTH_NORTH_WEST:
          return "北北西";
        case NORTH_NORTH_EAST:
          return "北北東";
        case EAST_NORTH_EAST:
          return "東北東";
        case EAST_SOUTH_EAST:
          return "東南東";
        case SOUTH_SOUTH_EAST:
          return "南南東";
        case SOUTH_SOUTH_WEST:
          return "南南西";
        case WEST_SOUTH_WEST:
          return "西南西";
        case SELF:
          return "自分";
        default:
          return "不明";
      }
    }
  
    public static double[] getRecentTps() {
      double[] recentTps = null;
      try {
        Object server = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());
        recentTps = ((double[]) server.getClass().getField("recentTps").get(server));
      } catch (ReflectiveOperationException e) {
        e.printStackTrace();
      }
      return recentTps;
    }
  
    public static String getWorldWeather(World world) {
      if (world.isClearWeather()) {
        return "晴れ";
      } else if (world.isThundering() && !world.isClearWeather()) {
        return "雷雨";
      } else if (!world.isClearWeather() && !world.isThundering()) {
        return "雨";
      }
      return "不明";
    }
  }
