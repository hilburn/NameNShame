package namenshame.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import namenshame.PrintFails;
import net.minecraft.util.ChatComponentText;

import java.util.Map;

public class EventHandler
{
    @SubscribeEvent
    public void joinServer(PlayerEvent.PlayerLoggedInEvent event)
    {
        for (Map.Entry<String,Integer> entry : PrintFails.failMods.entrySet())
        {
            event.player.addChatComponentMessage(new ChatComponentText("\u00A7l" + entry.getKey() + " has registered " + entry.getValue() + " invalid recipe"+(entry.getValue()>1?"s":"")));
        }
    }
}
