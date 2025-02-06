package dev.larrox.larroxUtils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class Scoreboard {
   private final org.bukkit.scoreboard.Scoreboard scoreboard;
   private final Objective objective;
   private final Scoreboard.ScoreboardPosition position;
   private int scores = -1;

   public Scoreboard(Scoreboard.ScoreboardPosition position, Scoreboard.ScoreboardType type, String title) {
      if (title.length() > 32) {
         this.scoreboard = null;
         this.objective = null;
         this.position = null;
         System.err.println("[LarroxUtils] Title exceeds 32 characters.");
      } else {
         this.scoreboard = type == Scoreboard.ScoreboardType.NEW ? Bukkit.getScoreboardManager().getNewScoreboard() : Bukkit.getScoreboardManager().getMainScoreboard();
         this.objective = this.scoreboard.registerNewObjective(title, Criteria.DUMMY, LarroxUtils.getInstance().getMessage().translateIntoColor(title));
         Objective var10000 = this.objective;
         DisplaySlot var10001;
         switch(position.ordinal()) {
         case 0:
            var10001 = DisplaySlot.SIDEBAR;
            break;
         case 1:
            var10001 = DisplaySlot.BELOW_NAME;
            break;
         case 2:
            var10001 = DisplaySlot.PLAYER_LIST;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         var10000.setDisplaySlot(var10001);
         this.position = position;
      }
   }

   public Scoreboard addLine(String text) {
      ++this.scores;
      this.objective.getScore(LarroxUtils.getInstance().getMessage().getLegacyText(LarroxUtils.getInstance().getMessage().translateIntoColor(text))).setScore(this.scores);
      return this;
   }

   public void displayScoreboard(User user) {
      user.getPlayer().setScoreboard(this.scoreboard);
   }

   public void updatingScoreboard(final User user, long delay, long period) {
      (new BukkitRunnable() {
         public void run() {
            if (user != null && user.isOnline()) {
               Bukkit.getScheduler().runTask(LarroxUtils.getInstance().getPlugin(), () -> {
                  Scoreboard.this.displayScoreboard(user);
               });
            } else {
               this.cancel();
            }

         }
      }).runTaskTimerAsynchronously(LarroxUtils.getInstance().getPlugin(), delay, period);
   }

   public void unregister() {
      this.scoreboard.getObjectives().forEach(Objective::unregister);
   }

   public void hide(User user) {
      org.bukkit.scoreboard.Scoreboard var10000 = user.getScoreboard();
      DisplaySlot var10001;
      switch(this.position.ordinal()) {
      case 0:
         var10001 = DisplaySlot.SIDEBAR;
         break;
      case 1:
         var10001 = DisplaySlot.BELOW_NAME;
         break;
      case 2:
         var10001 = DisplaySlot.PLAYER_LIST;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      var10000.clearSlot(var10001);
   }

   public static enum ScoreboardPosition {
      SIDEBAR,
      BELOW_NAME,
      LIST;

      // $FF: synthetic method
      private static Scoreboard.ScoreboardPosition[] $values() {
         return new Scoreboard.ScoreboardPosition[]{SIDEBAR, BELOW_NAME, LIST};
      }
   }

   public static enum ScoreboardType {
      NEW,
      MAIN;

      // $FF: synthetic method
      private static Scoreboard.ScoreboardType[] $values() {
         return new Scoreboard.ScoreboardType[]{NEW, MAIN};
      }
   }
}
