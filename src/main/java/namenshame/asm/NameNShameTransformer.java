package namenshame.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameNShameTransformer implements IClassTransformer
{
    public static Logger log = LogManager.getLogger("NameNShame Transformer");

    private enum ClassName
    {
        PRINT_FAILS("namenshame.FailHooks"),
        ITEM_STACK("net.minecraft.item.ItemStack", "add", Method.ITEMSTACK_INIT);

        private String name;
        private String obfName;
        private Method[] methods;

        ClassName(String name, Method... methods)
        {
            this.name = name;
            this.methods = methods;
        }

        ClassName(String name, String obf, Method... methods)
        {
            this(name, methods);
            this.obfName = obf;
        }


        public String getName()
        {
            return !LoadingPlugin.runtimeDeobfEnabled || obfName == null?name:obfName;
        }

        public String getASMName()
        {
            return name.replace(".","/");
        }
    }

    private enum Method
    {
        ITEMSTACK_INIT("<init>","(Lnet/minecraft/item/Item;II)V","(Ladb;II)V");

        public String name;
        public String obfName;
        public String args;
        public String obfArgs;
        public InsnList instructions;

        Method(String name, String args)
        {
            this.name = name;
            this.args = args;
        }
        Method(String name, String args, String obfArgs)
        {
            this(name, args);
            this.obfArgs = obfArgs;
        }


        public String getName()
        {
            return !LoadingPlugin.runtimeDeobfEnabled || obfName == null?name:obfName;
        }

        public String getArgs()
        {
            return !LoadingPlugin.runtimeDeobfEnabled || obfArgs == null?args:obfArgs;
        }

        static
        {
            ITEMSTACK_INIT.instructions = setItemStackInstructions();
        }

        private static InsnList setItemStackInstructions()
        {
            InsnList instructions = new InsnList();
            LabelNode label = new LabelNode(new Label());
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            instructions.add(new JumpInsnNode(Opcodes.IFNONNULL,label));
            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.PRINT_FAILS.getASMName(), "failedStack", "()V"));
            instructions.add(label);
            return instructions;
        }
    }

    private static Map<String,ClassName> classMap = new HashMap<String, ClassName>();

    static
    {
        //classMap.put(ClassName.GAME_REGISTRY.getName(),ClassName.GAME_REGISTRY);
        classMap.put(ClassName.ITEM_STACK.name,ClassName.ITEM_STACK);
        classMap.put(ClassName.ITEM_STACK.obfName,ClassName.ITEM_STACK);
    }

    public NameNShameTransformer()
    {
        log.log(Level.INFO, "Loading Name 'N' Shame Transformer");
    }

    @Override
    public byte[] transform(String className, String className2, byte[] bytes)
    {
        ClassName clazz = classMap.get(className);
        if (clazz!=null)
        {
//            switch (clazz)
//            {
//                case GAME_REGISTRY:
//                    for (Method method:clazz.methods)
//                    {
//                        bytes = insertFirst(method,bytes);
//                    }
//                    break;
                for (Method method:clazz.methods)
                {
                    bytes = insertFirst(method,bytes);
                }
//            }
            classMap.clear();//(className);
        }
        return bytes;
    }

    private byte[] insertFirst(Method method, byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        MethodNode methodNode = getMethodByName(classNode,method.getName(),method.getArgs());
        if (methodNode!=null)
        {
            log.log(Level.INFO,"Applying " + method.name() + " transformer");
            AbstractInsnNode pos = methodNode.instructions.getFirst();
            methodNode.instructions.insertBefore(pos,method.instructions);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    public static MethodNode getMethodByName(ClassNode classNode, String name, String args) {
        List<MethodNode> methods = classNode.methods;
        for (MethodNode method : methods)
        {
            if (method.name.equals(name) && method.desc.equals(args))
            {
                return method;
            }
        }
        return null;
    }

}