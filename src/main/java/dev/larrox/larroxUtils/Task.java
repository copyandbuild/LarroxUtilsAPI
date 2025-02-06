package dev.larrox.larroxUtils;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Task {
   private final JavaPlugin plugin = LarroxUtils.getInstance().getPlugin();
   private BukkitTask task;

   public void runTaskLater(long delay, Runnable task) {
      BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(this.plugin, task, delay);
      UUID id = UUID.randomUUID();
      this.task = bukkitTask;
   }

   public void runTaskTimer(long initialDelay, long interval, Runnable task) {
      BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(this.plugin, task, initialDelay, interval);
      UUID id = UUID.randomUUID();
      this.task = bukkitTask;
   }

   public void runTaskTimerAsync(long initialDelay, long interval, Runnable task) {
      BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, initialDelay, interval);
      UUID id = UUID.randomUUID();
      this.task = bukkitTask;
   }

   public void cancelTask() {
      if (this.task != null) {
         this.task.cancel();
      }

   }

   public boolean isTaskRunning() {
      return this.task != null;
   }
}
