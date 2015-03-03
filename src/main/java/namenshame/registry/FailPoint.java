package namenshame.registry;

import namenshame.FailHooks;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FailPoint
{
    private static Set<String> ignoreClasses = new HashSet<String>(Arrays.asList(FailPoint.class.getName(), FailTime.class.getName(), FailLogger.class.getName(), FailRegistry.class.getName(), FailHooks.class.getName(), ItemStack.class.getName()));

    private FailTime failTime;
    private StackTraceElement element;

    public FailPoint(int point)
    {
        this(FailTime.values()[point]);
    }

    public FailPoint(FailTime time)
    {
        this.failTime = time;
        this.element = getCallerClassName();
    }

    private static StackTraceElement getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ignoreClasses.contains(ste.getClassName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste;
            }
        }
        return null;
    }

    public FailTime getFailTime()
    {
        return failTime;
    }

    public StackTraceElement getElement()
    {
        return element;
    }
}
