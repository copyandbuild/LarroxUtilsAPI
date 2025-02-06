package dev.larrox.larroxUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleBuilder {
   private final org.bukkit.Particle particle;
   private Location location;
   private int count = 1;
   private double offsetX = 0.0D;
   private double offsetY = 0.0D;
   private double offsetZ = 0.0D;
   private double speed = (double)LarroxUtils.getInstance().getDefaultParticleSpeed();
   private Object data = null;
   private ParticleBuilder.ParticleShape shape;
   private double radius = 1.0D;
   private double height = 1.0D;
   private int steps = 10;
   private double rotationAngleX = 0.0D;
   private double rotationAngleY = 0.0D;
   private double rotationAngleZ = 0.0D;

   public ParticleBuilder(org.bukkit.Particle particle) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.PARTICLE)) {
         this.particle = particle;
      } else {
         this.particle = null;
      }

   }

   public ParticleBuilder setRotationAngleX(double angle) {
      this.rotationAngleX = angle;
      return this;
   }

   public ParticleBuilder setRotationAngleY(double angle) {
      this.rotationAngleY = angle;
      return this;
   }

   public ParticleBuilder setRotationAngleZ(double angle) {
      this.rotationAngleZ = angle;
      return this;
   }

   private List<Location> getShapePositions() {
      List<Location> positions = new ArrayList();
      if (this.shape == null) {
         positions.add(this.location);
         return positions;
      } else {
         switch(this.shape.ordinal()) {
         case 0:
            positions.addAll(this.generateCircle());
            break;
         case 1:
            positions.addAll(this.generateBox());
            break;
         case 2:
            positions.addAll(this.generateSpiral());
         }

         List<Location> rotatedPositions = new ArrayList();
         Iterator var3 = positions.iterator();

         while(var3.hasNext()) {
            Location pos = (Location)var3.next();
            rotatedPositions.add(this.rotateLocation(pos, this.rotationAngleX, this.rotationAngleY, this.rotationAngleZ));
         }

         return rotatedPositions;
      }
   }

   private Location rotateLocation(Location location, double angleX, double angleY, double angleZ) {
      double radiansX = Math.toRadians(angleX);
      double radiansY = Math.toRadians(angleY);
      double radiansZ = Math.toRadians(angleZ);
      double cosX = Math.cos(radiansX);
      double sinX = Math.sin(radiansX);
      double cosY = Math.cos(radiansY);
      double sinY = Math.sin(radiansY);
      double cosZ = Math.cos(radiansZ);
      double sinZ = Math.sin(radiansZ);
      double x = location.getX() - this.location.getX();
      double y = location.getY() - this.location.getY();
      double z = location.getZ() - this.location.getZ();
      double newY = cosX * y - sinX * z;
      double newZ = sinX * y + cosX * z;
      double newX = cosY * x + sinY * newZ;
      newZ = -sinY * x + cosY * newZ;
      x = newX;
      newX = cosZ * newX - sinZ * newY;
      newY = sinZ * x + cosZ * newY;
      return this.location.clone().add(newX, newY, newZ);
   }

   public ParticleBuilder setLocation(Location location) {
      this.location = location;
      return this;
   }

   public ParticleBuilder setCount(int count) {
      this.count = count;
      return this;
   }

   public ParticleBuilder setOffset(double offsetX, double offsetY, double offsetZ) {
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
      return this;
   }

   public ParticleBuilder setSpeed(double speed) {
      this.speed = speed;
      return this;
   }

   public ParticleBuilder setData(Object data) {
      this.data = data;
      return this;
   }

   public ParticleBuilder setShape(ParticleBuilder.ParticleShape shape) {
      this.shape = shape;
      return this;
   }

   public ParticleBuilder setRadius(double radius) {
      this.radius = radius;
      return this;
   }

   public ParticleBuilder setHeight(double height) {
      this.height = height;
      return this;
   }

   public ParticleBuilder setSteps(int steps) {
      this.steps = steps;
      return this;
   }

   public void spawn() {
      if (this.location != null && this.particle != null) {
         List<Location> positions = this.getShapePositions();
         Iterator var2 = positions.iterator();

         while(var2.hasNext()) {
            Location pos = (Location)var2.next();
            this.location.getWorld().spawnParticle(this.particle, pos, this.count, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.data);
         }

      } else {
         throw new IllegalStateException("Particle and Location must be set before spawning!");
      }
   }

   public void spawnForPlayer(Player player) {
      if (this.location != null && this.particle != null) {
         List<Location> positions = this.getShapePositions();
         Iterator var3 = positions.iterator();

         while(var3.hasNext()) {
            Location pos = (Location)var3.next();
            player.spawnParticle(this.particle, pos, this.count, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.data);
         }

      } else {
         throw new IllegalStateException("Particle and Location must be set before spawning!");
      }
   }

   public void spawnForPlayers(Collection<Player> players) {
      Iterator var2 = players.iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         this.spawnForPlayer(player);
      }

   }

   private List<Location> generateCircle() {
      List<Location> circleLocations = new ArrayList();

      for(int i = 0; i < this.steps; ++i) {
         double angle = Math.toRadians(360.0D / (double)this.steps * (double)i);
         double xOffset = Math.cos(angle) * this.radius;
         double zOffset = Math.sin(angle) * this.radius;
         Location pos = this.location.clone().add(xOffset, 0.0D, zOffset);
         circleLocations.add(pos);
      }

      return circleLocations;
   }

   private List<Location> generateBox() {
      List<Location> boxLocations = new ArrayList();
      double halfWidth = this.radius;
      double stepSize = this.height / (double)this.steps;

      for(int i = 0; i <= this.steps; ++i) {
         double yOffset = (double)i * stepSize;
         Location pos = this.location.clone().add(0.0D, yOffset, 0.0D);
         boxLocations.add(pos.add(-halfWidth, 0.0D, -halfWidth));
         boxLocations.add(pos.add(halfWidth * 2.0D, 0.0D, 0.0D));
         boxLocations.add(pos.add(0.0D, 0.0D, halfWidth * 2.0D));
         boxLocations.add(pos.add(-halfWidth * 2.0D, 0.0D, 0.0D));
      }

      return boxLocations;
   }

   private List<Location> generateSpiral() {
      List<Location> spiralLocations = new ArrayList();
      double angleStep = 0.3141592653589793D;
      double yStep = this.height / (double)this.steps;

      for(int i = 0; i < this.steps; ++i) {
         double angle = angleStep * (double)i;
         double xOffset = Math.cos(angle) * this.radius;
         double zOffset = Math.sin(angle) * this.radius;
         double yOffset = (double)i * yStep;
         Location pos = this.location.clone().add(xOffset, yOffset, zOffset);
         spiralLocations.add(pos);
      }

      return spiralLocations;
   }

   public static enum ParticleShape {
      CIRCLE,
      BOX,
      SPIRAL;

      // $FF: synthetic method
      private static ParticleBuilder.ParticleShape[] $values() {
         return new ParticleBuilder.ParticleShape[]{CIRCLE, BOX, SPIRAL};
      }
   }
}
