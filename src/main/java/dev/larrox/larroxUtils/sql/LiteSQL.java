package dev.larrox.larroxUtils.sql;

import dev.larrox.larroxUtils.LarroxUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.larrox.larroxUtils.sql.SQLEntry;
import org.bukkit.Bukkit;

public class LiteSQL {
   private final String databasePath;
   private Connection con;

   public LiteSQL(String databasePath) {
      String var10001 = LarroxUtils.getInstance().getPlugin().getDataFolder().getPath();
      this.databasePath = var10001 + "/" + databasePath;
   }

   public void createTable(String name, SQLEntry... entries) {
      StringBuilder tableDef = new StringBuilder();

      for(int i = 0; i < entries.length; ++i) {
         SQLEntry entry = entries[i];
         tableDef.append("`").append(entry.getKey()).append("` ").append(entry.getType() == SQLEntry.Type.VARCHAR ? "TEXT" : entry.getType().name()).append(entry.isPrimary() ? " PRIMARY KEY" : "");
         if (i < entries.length - 1) {
            tableDef.append(", ");
         }
      }

      String sql = String.format("CREATE TABLE IF NOT EXISTS `%s` (%s)", name, tableDef);
      this.executeUpdate(sql, "create table " + name);
   }

   public void dropTable(String name) {
      String sql = String.format("DROP TABLE IF EXISTS `%s`", name);
      this.executeUpdate(sql, "drop table " + name);
   }

   public void setEntry(String table, String column, String key, String value) {
      String sql = "INSERT INTO `" + table + "` (`uuid`, `" + column + "`) VALUES (?, ?) ON CONFLICT(`uuid`) DO UPDATE SET `" + column + "` = `" + column + "` || ',' || ?";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);

         try {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, value);
            stmt.executeUpdate();
         } catch (Throwable var16) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }
            }

            throw var16;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (SQLException var17) {
         this.logError("set entry in table " + table, var17);
      } finally {
         this.closeConnection(connection);
      }

   }

   public void removeEntry(String table, String column, String key, String value) {
      String sql = "UPDATE `" + table + "` SET `" + column + "` = REPLACE(`" + column + "`, ?, '') WHERE `uuid` = ?";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);

         try {
            stmt.setString(1, value);
            stmt.setString(2, key);
            stmt.executeUpdate();
         } catch (Throwable var16) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var15) {
                  var16.addSuppressed(var15);
               }
            }

            throw var16;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (SQLException var17) {
         this.logError("remove entry in table " + table, var17);
      } finally {
         this.closeConnection(connection);
      }

   }

   public String getEntry(String table, String column, String key) {
      String sql = "SELECT `" + column + "` FROM `" + table + "` WHERE `uuid` = ?";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt;
         label169: {
            stmt = connection.prepareStatement(sql);

            String var8;
            try {
               label163: {
                  stmt.setString(1, key);
                  ResultSet rs = stmt.executeQuery();

                  label149: {
                     try {
                        if (!rs.next()) {
                           break label149;
                        }

                        var8 = rs.getString(column);
                     } catch (Throwable var19) {
                        if (rs != null) {
                           try {
                              rs.close();
                           } catch (Throwable var18) {
                              var19.addSuppressed(var18);
                           }
                        }

                        throw var19;
                     }

                     if (rs != null) {
                        rs.close();
                     }
                     break label163;
                  }

                  if (rs != null) {
                     rs.close();
                  }
                  break label169;
               }
            } catch (Throwable var20) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var17) {
                     var20.addSuppressed(var17);
                  }
               }

               throw var20;
            }

            if (stmt != null) {
               stmt.close();
            }

            return var8;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (SQLException var21) {
         this.logError("get entry from table " + table, var21);
      } finally {
         this.closeConnection(connection);
      }

      return null;
   }

   public boolean tableContains(String table, String column, String key) {
      String sql = "SELECT 1 FROM `" + table + "` WHERE `" + column + "` = ? LIMIT 1";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);

         boolean var8;
         try {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();

            try {
               var8 = rs.next();
            } catch (Throwable var19) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var18) {
                     var19.addSuppressed(var18);
                  }
               }

               throw var19;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var20) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var17) {
                  var20.addSuppressed(var17);
               }
            }

            throw var20;
         }

         if (stmt != null) {
            stmt.close();
         }

         return var8;
      } catch (SQLException var21) {
         this.logError("check if table contains key in table " + table, var21);
      } finally {
         this.closeConnection(connection);
      }

      return false;
   }

   private void logError(String action, SQLException e) {
      Bukkit.getLogger().severe("[SQLite] Failed to " + action + ": " + e.getMessage());
   }

   private void executeUpdate(String sql, String action) {
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);

         try {
            stmt.executeUpdate();
         } catch (Throwable var13) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }
            }

            throw var13;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (SQLException var14) {
         this.logError(action, var14);
      } finally {
         this.closeConnection(connection);
      }

   }

   private Connection openConnection() {
      try {
         return DriverManager.getConnection("jdbc:sqlite:" + this.databasePath);
      } catch (SQLException var2) {
         Bukkit.getLogger().severe("[SQLite] Failed to connect to the database: " + var2.getMessage());
         return null;
      }
   }

   private void closeConnection(Connection connection) {
      try {
         if (this.isConnectionValid(connection)) {
            connection.close();
         }
      } catch (SQLException var3) {
         Bukkit.getLogger().severe("Failed to close connection.");
      }

   }

   private boolean isConnectionValid(Connection connection) {
      try {
         if (connection != null && !connection.isClosed()) {
            return true;
         }
      } catch (SQLException var3) {
         Bukkit.getLogger().severe("Connection wasn't valid.");
      }

      return false;
   }
}
