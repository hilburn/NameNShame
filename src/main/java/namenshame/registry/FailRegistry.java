package namenshame.registry;

import cpw.mods.fml.common.ModContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FailRegistry
{
    private static FailTime failTime = FailTime.UNKNOWN;
    private static Map<ModContainer,FailLogger> failLoggers = new LinkedHashMap<ModContainer, FailLogger>();

    public static void setTime(FailTime time)
    {
        failTime = time;
    }

    public static void recordFail(ModContainer mod)
    {
        FailPoint point = new FailPoint(failTime);
        FailLogger logger = failLoggers.get(mod);
        if (logger == null)
        {
            logger = new FailLogger(mod);
            failLoggers.put(mod,logger);
        }
        logger.logFail(point);
    }

    public static void printFails()
    {
        try
        {
            PrintWriter pw = new PrintWriter("NameNShame.log");
            for (FailLogger logger : failLoggers.values())
                pw.println(logger.getDetailedFails());
            pw.close();
        } catch (IOException ignored)
        {
        }
    }

    public static void getFailSummary(EntityPlayer player)
    {
        if (player == null) return;
        for (FailLogger logger : failLoggers.values())
        {
            player.addChatComponentMessage(new ChatComponentText("\u00A7l" + logger.getFailSummary()));
        }
    }
}
