package dev.larrox.larroxUtils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;

public class Cosmetic {
   public Cosmetic(Cosmetic.CosmeticType type, User user) {
      Location pLocation;
      float yaw;
      double radians;
      double[][] var8;
      int var9;
      int var10;
      double[] offset;
      double relativeX;
      double relativeZ;
      double rotatedX;
      double rotatedZ;
      double mirroredX;
      double rotatedMirroredX;
      double rotatedMirroredZ;
      double[][] hornOffsets;
      switch(type.ordinal()) {
      case 0:
         hornOffsets = new double[][]{{0.5D, 1.0D, -0.5D}, {0.7D, 1.2D, -0.6D}, {0.9D, 1.4D, -0.7D}, {1.0D, 1.6D, -0.8D}, {0.9D, 1.4D, -0.9D}, {0.7D, 1.2D, -1.0D}, {0.5D, 1.0D, -1.1D}};
         pLocation = user.getLocation();
         yaw = pLocation.getYaw();
         yaw = yaw < 0.0F ? yaw + 360.0F : yaw;
         radians = Math.toRadians((double)yaw);
         var8 = hornOffsets;
         var9 = hornOffsets.length;

         for(var10 = 0; var10 < var9; ++var10) {
            offset = var8[var10];
            relativeX = offset[0];
            relativeZ = offset[2];
            rotatedX = relativeX * Math.cos(radians) - relativeZ * Math.sin(radians);
            rotatedZ = relativeX * Math.sin(radians) + relativeZ * Math.cos(radians);
            (new ParticleBuilder(org.bukkit.Particle.SNOWFLAKE)).setLocation(pLocation.clone().add(rotatedX, offset[1], rotatedZ)).setCount(1).setOffset(0.0D, 0.0D, 0.0D).setSpeed(0.0D).spawn();
            mirroredX = -relativeX;
            rotatedMirroredX = mirroredX * Math.cos(radians) - relativeZ * Math.sin(radians);
            rotatedMirroredZ = mirroredX * Math.sin(radians) + relativeZ * Math.cos(radians);
            (new ParticleBuilder(org.bukkit.Particle.SNOWFLAKE)).setLocation(pLocation.clone().add(rotatedMirroredX, offset[1], rotatedMirroredZ)).setCount(1).setOffset(0.0D, 0.0D, 0.0D).setSpeed(0.0D).spawn();
            (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(pLocation.clone().add(0.0D, 2.2D, 0.0D)).setShape(ParticleBuilder.ParticleShape.CIRCLE).setRadius(0.5D).setSpeed(0.0D).setSteps(20).setData(new DustOptions(Color.WHITE, 0.5F)).spawn();
            (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(pLocation.clone().add(0.0D, 1.0D, 0.0D)).setOffset(2.0D, 2.0D, 2.0D).setSpeed(0.175D).setSteps(100).setData(new DustOptions(Color.WHITE, 0.5F)).spawn();
         }

         return;
      case 1:
         hornOffsets = new double[][]{{0.3D, 2.1D, -0.2D}, {0.35D, 2.2D, -0.25D}, {0.4D, 2.3D, -0.3D}, {0.45D, 2.4D, -0.35D}, {0.5D, 2.5D, -0.4D}, {0.45D, 2.6D, -0.35D}, {0.4D, 2.7D, -0.3D}};
         pLocation = user.getLocation().clone().add(0.0D, -0.1D, 0.0D);
         yaw = pLocation.getYaw();
         yaw = yaw < 0.0F ? yaw + 360.0F : yaw;
         radians = Math.toRadians((double)yaw);
         var8 = hornOffsets;
         var9 = hornOffsets.length;

         for(var10 = 0; var10 < var9; ++var10) {
            offset = var8[var10];
            relativeX = offset[0];
            relativeZ = offset[2];
            rotatedX = relativeX * Math.cos(radians) - relativeZ * Math.sin(radians);
            rotatedZ = relativeX * Math.sin(radians) + relativeZ * Math.cos(radians);
            (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(pLocation.clone().add(rotatedX, offset[1], rotatedZ)).setCount(1).setOffset(0.0D, 0.0D, 0.0D).setSpeed(0.0D).setData(new DustOptions(Color.RED, 1.0F)).spawn();
            mirroredX = -relativeX;
            rotatedMirroredX = mirroredX * Math.cos(radians) - relativeZ * Math.sin(radians);
            rotatedMirroredZ = mirroredX * Math.sin(radians) + relativeZ * Math.cos(radians);
            (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(pLocation.clone().add(rotatedMirroredX, offset[1], rotatedMirroredZ)).setCount(1).setOffset(0.0D, 0.0D, 0.0D).setSpeed(0.0D).setData(new DustOptions(Color.RED, 1.0F)).spawn();
         }

         return;
      case 2:
         Location playerLocation = user.getLocation();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.3D, 1.8D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.3D, 1.8D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(-20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.35D, 2.15D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.7D, 1.8D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(1.1D, 1.9D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(-12.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.9D, 2.0D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(0.8D, 2.3D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(-20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(1.5D, 1.9D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(40.0D).setRadius(0.0D).setHeight(0.5D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-0.5D, 1.8D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-0.5D, 1.8D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-0.55D, 2.15D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(-20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-0.9D, 1.9D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(-20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-1.3D, 1.9D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(12.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-1.1D, 2.0D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(-20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-1.0D, 2.3D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(0.0D).setRotationAngleY(0.0D).setRotationAngleZ(20.0D).setRadius(0.0D).setHeight(0.3D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
         (new ParticleBuilder(org.bukkit.Particle.DUST)).setLocation(playerLocation.clone().add(-1.8D, 2.2D, 0.0D)).setShape(ParticleBuilder.ParticleShape.BOX).setRotationAngleX(90.0D).setRotationAngleY(90.0D).setRotationAngleZ(-40.0D).setRadius(0.0D).setHeight(0.5D).setSpeed(0.0D).setData(new DustOptions(Color.BLACK, 0.5F)).spawn();
      }

   }

   public static enum CosmeticType {
      ANGEL,
      DEVIL,
      ALASTOR;

      // $FF: synthetic method
      private static Cosmetic.CosmeticType[] $values() {
         return new Cosmetic.CosmeticType[]{ANGEL, DEVIL, ALASTOR};
      }
   }
}
