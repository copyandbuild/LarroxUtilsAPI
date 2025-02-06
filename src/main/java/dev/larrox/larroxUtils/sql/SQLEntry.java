package dev.larrox.larroxUtils.sql;

public class SQLEntry {
   private final String key;
   private final SQLEntry.Type type;
   private final boolean isPrimary;

   public SQLEntry(String key, SQLEntry.Type type, boolean isPrimary) {
      this.key = key;
      this.type = type;
      this.isPrimary = isPrimary;
   }

   public SQLEntry(String key, SQLEntry.Type type) {
      this.key = key;
      this.type = type;
      this.isPrimary = false;
   }

   public String getKey() {
      return this.key;
   }

   public SQLEntry.Type getType() {
      return this.type;
   }

   public boolean isPrimary() {
      return this.isPrimary;
   }

   public static enum Type {
      TINYINT,
      SMALLINT,
      MEDIUMINT,
      INT,
      BIGINT,
      FLOAT,
      DOUBLE,
      DECIMAL,
      DATE,
      DATETIME,
      TIMESTAMP,
      TIME,
      YEAR,
      CHAR,
      VARCHAR,
      TINYTEXT,
      TEXT,
      MEDIUMTEXT,
      LONGTEXT,
      BINARY,
      VARBINARY,
      TINYBLOB,
      BLOB,
      MEDIUMBLOB,
      LONGBLOB,
      GEOMETRY,
      POINT,
      LINESTRING,
      POLYGON,
      MULTIPOINT,
      MULTILINESTRING,
      MULTIPOLYGON,
      GEOMETRICCOLLECTION,
      JSON;

      // $FF: synthetic method
      private static SQLEntry.Type[] $values() {
         return new SQLEntry.Type[]{TINYINT, SMALLINT, MEDIUMINT, INT, BIGINT, FLOAT, DOUBLE, DECIMAL, DATE, DATETIME, TIMESTAMP, TIME, YEAR, CHAR, VARCHAR, TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, BINARY, VARBINARY, TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB, GEOMETRY, POINT, LINESTRING, POLYGON, MULTIPOINT, MULTILINESTRING, MULTIPOLYGON, GEOMETRICCOLLECTION, JSON};
      }
   }
}
