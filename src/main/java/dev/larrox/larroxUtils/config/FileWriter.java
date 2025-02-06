package dev.larrox.larroxUtils.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class FileWriter {
   private final File f;
   private final YamlConfiguration c;

   public FileWriter(String FilePath, String FileName) {
      this.f = new File(FilePath, FileName);
      this.c = YamlConfiguration.loadConfiguration(this.f);
   }

   public YamlConfiguration getYaml() {
      return this.c;
   }

   public boolean exist() {
      return this.f.exists();
   }

   public FileWriter setValue(String ValuePath, Object Value) {
      this.c.set(ValuePath, Value);
      return this;
   }

   public FileWriter setDefaultValue(String ValuePath, Object Value) {
      if (!this.valueExist(ValuePath)) {
         this.c.set(ValuePath, Value);
         this.save();
      }

      return this;
   }

   public boolean valueExist(String value) {
      return this.getObject(value) != null;
   }

   public void save() {
      try {
         this.c.save(this.f);
      } catch (IOException var2) {
         var2.fillInStackTrace();
      }

   }

   public Object getObject(String ValuePath) {
      return this.c.get(ValuePath);
   }

   public boolean getBoolean(String ValuePath) {
      return this.c.getBoolean(ValuePath);
   }

   public String getString(String ValuePath) {
      return this.c.getString(ValuePath);
   }

   public Integer getInt(String ValuePath) {
      return this.c.getInt(ValuePath);
   }

   public Short getShort(String ValuePath) {
      return (short)this.c.getInt(ValuePath);
   }

   public List<String> getStringList(String ValuePath) {
      return this.c.getStringList(ValuePath);
   }

   public List<Boolean> getBooleanList(String ValuePath) {
      return this.c.getBooleanList(ValuePath);
   }

   public List<Float> getFloatList(String ValuePath) {
      return this.c.getFloatList(ValuePath);
   }

   public List<Byte> getByteList(String ValuePath) {
      return this.c.getByteList(ValuePath);
   }

   public List<Double> getDoubleList(String ValuePath) {
      return this.c.getDoubleList(ValuePath);
   }

   public List<Character> getCharacterList(String ValuePath) {
      return this.c.getCharacterList(ValuePath);
   }

   public List<Short> getShortList(String ValuePath) {
      return this.c.getShortList(ValuePath);
   }

   public List<Map<?, ?>> getMapList(String ValuePath) {
      return this.c.getMapList(ValuePath);
   }

   public List<?> getList(String ValuePath) {
      return this.c.getList(ValuePath);
   }

   public List<Integer> getIntList(String ValuePath) {
      return this.c.getIntegerList(ValuePath);
   }

   public Long getLong(String ValuePath) {
      return this.c.getLong(ValuePath);
   }

   public Float getFloat(String ValuePath) {
      return (float)this.c.getLong(ValuePath);
   }

   public Double getDouble(String ValuePath) {
      return this.c.getDouble(ValuePath);
   }

   public ItemStack getItemStack(String ValuePath) {
      return (ItemStack)this.c.get(ValuePath);
   }
}
