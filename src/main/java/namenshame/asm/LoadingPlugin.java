package namenshame.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"namenshame.asm."})
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean runtimeDeobfEnabled = false;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{"namenshame.asm.NameNShameTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
