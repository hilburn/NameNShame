package namenshame;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class FileIO
{
    public static void printFails()
    {
        try
        {
            PrintWriter pw = new PrintWriter("NameNShame.log");
            for (Map.Entry<String,Integer> entry : PrintFails.failMods.entrySet())
                pw.println(entry.getKey() + " has registered " + entry.getValue() + " invalid recipe"+(entry.getValue()>1?"s":""));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
