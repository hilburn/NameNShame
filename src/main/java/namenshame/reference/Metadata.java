package namenshame.reference;

import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

public class Metadata
{
    public static ModMetadata init(ModMetadata metadata)
    {
        metadata.modId = Reference.ID;
        metadata.name = Reference.NAME;
        metadata.description = "Discover Modder Fails";
        metadata.version = Reference.V_MAJOR + "." + Reference.V_MINOR + "." + Reference.V_REVIS;
        metadata.authorList = Arrays.asList("hilburn");
        metadata.autogenerated = false;
        return metadata;
    }
}
