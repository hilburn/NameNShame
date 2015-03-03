package namenshame;

import cpw.mods.fml.common.Loader;
import namenshame.registry.FailRegistry;

public class FailHooks
{
    public static void failedStack()
    {
        FailRegistry.recordFail(Loader.instance().activeModContainer());
    }

}
