package namenshame;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import namenshame.handlers.EventHandler;
import namenshame.reference.Metadata;
import namenshame.reference.Reference;

import java.util.Map;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL)
public class NameNShame
{

    @Mod.Instance(Reference.ID)
    public static NameNShame instance = new NameNShame();

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        metadata = Metadata.init(metadata);
        FMLCommonHandler.instance().bus().register(new EventHandler());
    }

    @NetworkCheckHandler
    public final boolean networkCheck(Map<String, String> remoteVersions, Side side)
    {
        return true;
    }
}
