package namenshame.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import namenshame.FailHooks;
import namenshame.registry.FailRegistry;
import net.minecraft.util.ChatComponentText;

import java.util.Map;

public class EventHandler
{
    @SubscribeEvent
    public void joinServer(PlayerEvent.PlayerLoggedInEvent event)
    {
        FailRegistry.getFailSummary(event.player);
    }
}
