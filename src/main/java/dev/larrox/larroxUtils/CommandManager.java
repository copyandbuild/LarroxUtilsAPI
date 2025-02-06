package dev.larrox.larroxUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandManager {
   private static final Map<String, TabExecutor> commandExecutors = new HashMap();

   public static void registerCommand(Object commandClassInstance, JavaPlugin plugin) {
      Method[] var2 = commandClassInstance.getClass().getDeclaredMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         if (method.isAnnotationPresent(CommandManager.Command.class)) {
            CommandManager.Command annotation = (CommandManager.Command)method.getAnnotation(CommandManager.Command.class);
            String name = annotation.name();
            PluginCommand pluginCommand = plugin.getCommand(name);
            if (pluginCommand == null) {
               plugin.getLogger().warning("Command " + name + " is not defined in plugin.yml!");
            } else {
               TabExecutor tabExecutor = getTabExecutor(commandClassInstance, method);
               pluginCommand.setExecutor(tabExecutor);
               pluginCommand.setTabCompleter(tabExecutor);
               commandExecutors.put(name, tabExecutor);
            }
         }
      }

   }

   @NotNull
   private static TabExecutor getTabExecutor(Object commandClass, Method method) {
      CommandExecutor commandExecutor = (sender, command, label, args) -> {
         try {
            method.setAccessible(true);
            method.invoke(commandClass, sender, args);
         } catch (Exception var7) {
            sender.sendMessage("§8[§5IsaUtils§8] §7Something went §cwrong§7.");
         }

         return true;
      };
      return new CommandManager.CommandTabExecutor(commandExecutor, commandClass);
   }

   public static Map<String, TabExecutor> getCommandExecutors() {
      return commandExecutors;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface Command {
      String name();

      boolean playerOnly() default false;
   }

   private static record CommandTabExecutor(CommandExecutor executor, Object instance) implements TabExecutor {
      private CommandTabExecutor(CommandExecutor executor, Object instance) {
         this.executor = executor;
         this.instance = instance;
      }

      public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
         return this.executor.onCommand(sender, command, label, args);
      }

      @Nullable
      public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
         Method[] var5 = this.instance.getClass().getDeclaredMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            if (method.isAnnotationPresent(CommandManager.Completer.class) && ((CommandManager.Completer)method.getAnnotation(CommandManager.Completer.class)).name().equalsIgnoreCase(command.getName())) {
               try {
                  method.setAccessible(true);
                  return (List)method.invoke(this.instance, sender, args);
               } catch (Exception var10) {
                  var10.printStackTrace();
               }
            }
         }

         return null;
      }

      public CommandExecutor executor() {
         return this.executor;
      }

      public Object instance() {
         return this.instance;
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD})
   public @interface Completer {
      String name();
   }
}
