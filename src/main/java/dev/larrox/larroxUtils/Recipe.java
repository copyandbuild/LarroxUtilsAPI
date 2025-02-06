package dev.larrox.larroxUtils;

import dev.larrox.larroxUtils.inventory.Item;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipe {
   private ShapelessRecipe shapelessRecipe;
   private ShapedRecipe shapedRecipe;
   private final Recipe.RecipeType type;
   private final Item item;

   public Recipe(Recipe.RecipeType type, Item item) {
      this.type = type;
      this.item = item;
      JavaPlugin var10005;
      String var10006;
      switch(type.ordinal()) {
      case 0:
         var10005 = LarroxUtils.getInstance().getPlugin();
         var10006 = type.name();
         this.shapelessRecipe = new ShapelessRecipe(new NamespacedKey(var10005, var10006 + "_" + item.getName() + (new Random()).nextInt(50000)), item.getItemStack());
         break;
      case 1:
         var10005 = LarroxUtils.getInstance().getPlugin();
         var10006 = type.name();
         this.shapedRecipe = new ShapedRecipe(new NamespacedKey(var10005, var10006 + "_" + item.getName() + (new Random()).nextInt(50000)), item.getItemStack());
      }

   }

   public Recipe addIngredient(Item item) {
      if (this.type == Recipe.RecipeType.SHAPELESS) {
         this.shapelessRecipe.addIngredient(item.getItemStack());
      }

      return this;
   }

   public Recipe addIngredient(short count, Item item) {
      if (this.type == Recipe.RecipeType.SHAPELESS) {
         this.shapelessRecipe.addIngredient(count, item.getItemStack());
      }

      return this;
   }

   public Recipe setIngredient(Item... items) {
      if (this.type == Recipe.RecipeType.SHAPED && items.length == 9) {
         this.shapedRecipe.shape(new String[]{"123", "456", "789"});
         this.shapedRecipe.setIngredient('1', new ExactChoice(items[0].getItemStack()));
         this.shapedRecipe.setIngredient('2', new ExactChoice(items[1].getItemStack()));
         this.shapedRecipe.setIngredient('3', new ExactChoice(items[2].getItemStack()));
         this.shapedRecipe.setIngredient('4', new ExactChoice(items[3].getItemStack()));
         this.shapedRecipe.setIngredient('5', new ExactChoice(items[4].getItemStack()));
         this.shapedRecipe.setIngredient('6', new ExactChoice(items[5].getItemStack()));
         this.shapedRecipe.setIngredient('7', new ExactChoice(items[6].getItemStack()));
         this.shapedRecipe.setIngredient('8', new ExactChoice(items[7].getItemStack()));
         this.shapedRecipe.setIngredient('9', new ExactChoice(items[8].getItemStack()));
      }

      return this;
   }

   public Recipe setIngredient(Object... objects) {
      if (this.type == Recipe.RecipeType.SHAPED && objects.length == 9) {
         this.shapedRecipe.shape(new String[]{"123", "456", "789"});

         for(int i = 0; i < objects.length; ++i) {
            if (objects[i] instanceof Item) {
               Item item = (Item)objects[i];
               this.shapedRecipe.setIngredient((char)(i + 1), new ExactChoice(item.getItemStack()));
            } else {
               if (!(objects[i] instanceof Material)) {
                  System.err.println("[LarroxUtils] RSI: 80");
                  return null;
               }

               Material material = (Material)objects[i];
               this.shapedRecipe.setIngredient((char)(i + 1), material);
            }
         }
      }

      return this;
   }

   public Recipe setIngredient(Material... materials) {
      if (this.type == Recipe.RecipeType.SHAPED && materials.length == 9) {
         this.shapedRecipe.shape(new String[]{"123", "456", "789"});
         this.shapedRecipe.setIngredient('1', materials[0]);
         this.shapedRecipe.setIngredient('2', materials[1]);
         this.shapedRecipe.setIngredient('3', materials[2]);
         this.shapedRecipe.setIngredient('4', materials[3]);
         this.shapedRecipe.setIngredient('5', materials[4]);
         this.shapedRecipe.setIngredient('6', materials[5]);
         this.shapedRecipe.setIngredient('7', materials[6]);
         this.shapedRecipe.setIngredient('8', materials[7]);
         this.shapedRecipe.setIngredient('9', materials[8]);
      }

      return this;
   }

   public void register() {
      Bukkit.addRecipe((this.type == Recipe.RecipeType.SHAPED ? this.shapedRecipe : this.shapelessRecipe));
   }

   public static enum RecipeType {
      SHAPELESS,
      SHAPED;

      // $FF: synthetic method
      private static Recipe.RecipeType[] $values() {
         return new Recipe.RecipeType[]{SHAPELESS, SHAPED};
      }
   }
}
