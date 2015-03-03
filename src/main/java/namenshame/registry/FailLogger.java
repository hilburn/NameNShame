package namenshame.registry;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.ModContainer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FailLogger
{
    public static Logger log = LogManager.getLogger("NameNShame");
    private ModContainer mod;
    private Multimap<FailTime,FailPoint> failPoints = LinkedHashMultimap.create();

    public FailLogger(ModContainer modContainer)
    {
        this.mod = modContainer;
    }

    public void logFail(FailPoint point)
    {
        failPoints.put(point.getFailTime(), point);
        log.log(Level.WARN,getModName()+ " has created an invalid ItemStack; "+point.getElement().toString());
    }

    public String getFailSummary()
    {
        String points = "";
        int fails = failPoints.size();
        for (FailTime point : FailTime.values())
        {
            if (failPoints.containsKey(point) && failPoints.get(point).size()>0)
            {
                points+="; "+point.name()+": "+failPoints.get(point).size();
            }
        }
        return getModName()+ " has created " + fails + " invalid ItemStack" + (fails!=1?"s":"")+points;
    }

    public String getDetailedFails()
    {
        int fails = failPoints.size();
        String details = getModName()+ " has created " + fails + " invalid ItemStack" + (fails!=1?"s":"");
        FailTime time = null;
        for (FailPoint point : failPoints.values())
        {
            if (time != point.getFailTime())
            {
                time = point.getFailTime();
                details += "\n\t" + time.name();
            }
            details+="\n\t\t"+point.getElement().toString();
        }
        return details;
    }

    public String getModName()
    {
        if (mod!=null)
        {
            return mod.getName();
        }
        return "An Unknown Mod";
    }
}
