package namenshame;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

public class PrintFails
{
    private static Logger log = LogManager.getLogger("NameNShame");
    public static LinkedHashMap<String, Integer> failMods = new LinkedHashMap<String, Integer>();

    public static void checkFail(ItemStack stack, Object... args)
    {
        if (invalidStack(stack)) printFail();
        else
        {
            for (Object o:args)
            {
                if (o instanceof ItemStack)
                {
                    if (invalidStack((ItemStack)o))
                    {
                        printFail();
                        break;
                    }
                }
            }
        }
    }

    public static void checkFail(IRecipe recipe)
    {
        if (invalidStack(recipe.getRecipeOutput())) printFail();
    }

    public static void checkFail(Block block, ItemStack stack)
    {
        if (block==null || invalidStack(stack)) printFail();
    }

    public static void checkFail(Item item, ItemStack stack)
    {
        if (item==null || invalidStack(stack)) printFail();
    }

    public static void checkFail(ItemStack input, ItemStack stack)
    {
        if (invalidStack(input) || invalidStack(stack)) printFail();
    }

    private static void printFail()
    {
        ModContainer mod = Loader.instance().activeModContainer();
        if (mod!=null)
        {
            String modId = mod.getModId();
            addFail(modId);
            log.log(Level.WARN,modId + " has registered an invalid recipe.");
        }
    }

    private static void addFail(String modId)
    {
        Integer fails = failMods.get(modId);
        if (fails == null) fails = 0;
        failMods.put(modId,fails+1);
    }

    private static boolean invalidStack(ItemStack stack)
    {
        return stack == null || stack.getItem() == null || stack.stackSize < 1 || stack.getItemDamage() < 0;
    }

}
