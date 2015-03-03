package namenshame;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import namenshame.handlers.EventHandler;
import namenshame.reference.Metadata;
import namenshame.reference.Reference;
import namenshame.registry.FailRegistry;
import namenshame.registry.FailTime;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, dependencies="before:*")
public class NameNShame
{

    @Mod.Instance(Reference.ID)
    public static NameNShame instance;

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        metadata = Metadata.init(metadata);
        FMLCommonHandler.instance().bus().register(new EventHandler());
        FailRegistry.setTime(FailTime.PRE_INIT);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        FailRegistry.setTime(FailTime.INIT);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        FailRegistry.setTime(FailTime.POST_INIT);
    }

    @NetworkCheckHandler
    public final boolean networkCheck(Map<String, String> remoteVersions, Side side)
    {
        return true;
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        FailRegistry.printFails();
        FailRegistry.setTime(FailTime.LOAD_COMPLETE);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        FailRegistry.setTime(FailTime.SERVER_STARTING);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        FailRegistry.setTime(FailTime.SERVER_START);
    }
}
