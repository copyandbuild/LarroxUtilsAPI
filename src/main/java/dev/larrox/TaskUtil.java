package dev.larrox;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TaskUtil {

    private final JavaPlugin plugin;

    public TaskUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Wandelt die Zeitangaben in Ticks um.
     */
    private long toTicks(int days, int hours, int minutes, int seconds, int ticks) {
        long result = 0;
        result += days * 24L * 60L * 60L * 20L;   // Tage -> Ticks
        result += hours * 60L * 60L * 20L;        // Stunden -> Ticks
        result += minutes * 60L * 20L;            // Minuten -> Ticks
        result += seconds * 20L;                  // Sekunden -> Ticks
        result += ticks;                          // Direkt Ticks
        return result;
    }

    /**
     * Führt eine Aufgabe einmalig verzögert aus.
     */
    public void runLater(Runnable task, int days, int hours, int minutes, int seconds, int ticks) {
        long delay = toTicks(days, hours, minutes, seconds, ticks);
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    /**
     * Führt eine Aufgabe wiederholt aus.
     */
    public void runTimer(Runnable task,
                         int delayDays, int delayHours, int delayMinutes, int delaySeconds, int delayTicks,
                         int periodDays, int periodHours, int periodMinutes, int periodSeconds, int periodTicks) {

        long delay = toTicks(delayDays, delayHours, delayMinutes, delaySeconds, delayTicks);
        long period = toTicks(periodDays, periodHours, periodMinutes, periodSeconds, periodTicks);

        Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    /**
     * Führt eine Aufgabe sofort auf dem Bukkit-Thread aus.
     */
    public void run(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}
