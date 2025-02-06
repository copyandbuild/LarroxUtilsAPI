package dev.larrox.larroxUtils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Particle {
   private final Location location;

   public Particle(Location location, org.bukkit.Particle particle, short amount) {
      this.location = location;
      location.getWorld().spawnParticle(particle, location, amount);
   }

   public Particle(Player player, org.bukkit.Particle particle, short amount) {
      this.location = player.getLocation();
      player.getWorld().spawnParticle(particle, player.getLocation(), amount);
   }

   public void playSound(Sound sound, float pitch, float volume) {
      this.location.getWorld().playSound(this.location, sound, pitch, volume);
   }
}
