package dev.larrox.larroxUtils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.larrox.larroxUtils.LarroxUtils;
import org.bukkit.Bukkit;

public class MySQL {
   private final String host;
   private final String database;
   private final String user;
   private final String password;
   private final int port;
   private final boolean useSSL;
   private Connection con;

   public MySQL(String host, String database, int port, String user, String password, boolean useSSL) {
      if (!LarroxUtils.getInstance().isSystemDisabled(LarroxUtils.SystemType.DATABASE)) {
         this.host = host;
         this.database = database;
         this.port = port;
         this.user = user;
         this.password = password;
         this.useSSL = useSSL;
      } else {
         this.host = "";
         this.database = "";
         this.port = 0;
         this.user = "";
         this.password = "";
         this.useSSL = false;
         Bukkit.getLogger().info("[IsaUtils] The Database System was disabled.");
      }

   }

   public void createTable(String name, SQLEntry... entries) {
      StringBuilder tableDef = new StringBuilder();

      for(int i = 0; i < entries.length; ++i) {
         SQLEntry entry = entries[i];
         tableDef.append("`").append(entry.getKey()).append("` ").append(entry.getType() == SQLEntry.Type.VARCHAR ? "VARCHAR(255)" : entry.getType().name()).append(entry.isPrimary() ? " PRIMARY KEY" : "");
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
      String sql = "INSERT INTO `" + table + "` (`uuid`, `" + column + "`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `" + column + "` = CONCAT(IFNULL(`" + column + "`, ''), ?)";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt = connection.prepareStatement(sql);

         try {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, "," + value);
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

   public String getEntry(String table, String column, String key, String value) {
      String sql = "SELECT `" + column + "` FROM `" + table + "` WHERE `uuid` = ?";
      Connection connection = this.openConnection();

      try {
         PreparedStatement stmt;
         label169: {
            stmt = connection.prepareStatement(sql);

            String var9;
            try {
               label163: {
                  stmt.setString(1, key);
                  ResultSet rs = stmt.executeQuery();

                  label149: {
                     try {
                        if (!rs.next()) {
                           break label149;
                        }

                        var9 = rs.getString(column);
                     } catch (Throwable var20) {
                        if (rs != null) {
                           try {
                              rs.close();
                           } catch (Throwable var19) {
                              var20.addSuppressed(var19);
                           }
                        }

                        throw var20;
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
            } catch (Throwable var21) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var18) {
                     var21.addSuppressed(var18);
                  }
               }

               throw var21;
            }

            if (stmt != null) {
               stmt.close();
            }

            return var9;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (SQLException var22) {
         this.logError("get entry from table " + table, var22);
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
      Bukkit.getLogger().severe("[MySQL] Failed to " + action + ": " + e.getMessage());
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
         return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=" + this.useSSL, this.user, this.password);
      } catch (SQLException var2) {
         Bukkit.getLogger().severe("[MySQL] Failed to connect to the database: " + var2.getMessage());
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
