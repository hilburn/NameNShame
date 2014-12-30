package namenshame.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameNShameTransformer implements IClassTransformer
{
    private enum ClassName
    {
        PRINT_FAILS("namenshame.PrintFails"),
        GAME_REGISTRY("cpw.mods.fml.common.registry.GameRegistry", Method.RECIPE, Method.SHAPED_RECIPE, Method.SHAPELESS_RECIPE, Method.BLOCK_SMELTING, Method.ITEM_SMELTING, Method.STACK_SMELTING);

        private String name;
        private Method[] methods;

        ClassName(String name, Method... methods)
        {
            this.name = name;
            this.methods = methods;
        }


        public String getName()
        {
            return name;
        }

        public String getASMName()
        {
            return name.replace(".","/");
        }
    }

    private enum Method
    {
        RECIPE("addRecipe","(Lnet/minecraft/item/crafting/IRecipe;)V"),
        SHAPED_RECIPE("addShapedRecipe","(Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/item/crafting/IRecipe"),
        SHAPELESS_RECIPE("addShapelessRecipe","(Lnet/minecraft/item/ItemStack;[Ljava/lang/Object;)V"),
        BLOCK_SMELTING("addSmelting","(Lnet/minecraft/block/Block;Lnet/minecraft/item/ItemStack;F)V"),
        ITEM_SMELTING("addSmelting","(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;F)V"),
        STACK_SMELTING("addSmelting","(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V"),
        CHECK_FAIL("checkFail","");


        public String name;
        public String args;
        public InsnList instructions;

        Method(String name, String args)
        {
            this.name = name;
            this.args = args;
        }

        static
        {
            RECIPE.instructions = setRecipeInstructions();
            SHAPED_RECIPE.instructions = setShapedInstructions();
            SHAPELESS_RECIPE.instructions = setShapedInstructions();
            BLOCK_SMELTING.instructions = setSmeltingInstructions(BLOCK_SMELTING);
            ITEM_SMELTING.instructions = setSmeltingInstructions(ITEM_SMELTING);
            STACK_SMELTING.instructions = setSmeltingInstructions(STACK_SMELTING);
        }

        private static InsnList setSmeltingInstructions(Method method)
        {
            InsnList instructions = new InsnList();
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.PRINT_FAILS.getASMName(), CHECK_FAIL.name, method.args.replace(";F",";")));
            return instructions;
        }

        private static InsnList setShapedInstructions()
        {
            InsnList instructions = new InsnList();
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.PRINT_FAILS.getASMName(), CHECK_FAIL.name, SHAPELESS_RECIPE.args));
            return instructions;
        }

        private static InsnList setRecipeInstructions()
        {
            InsnList instructions = new InsnList();
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.PRINT_FAILS.getASMName(), CHECK_FAIL.name, RECIPE.args));
            return instructions;
        }
    }

    private static Map<String,ClassName> classMap = new HashMap<String, ClassName>();

    static
    {
        classMap.put(ClassName.GAME_REGISTRY.getName(),ClassName.GAME_REGISTRY);
    }

    @Override
    public byte[] transform(String className, String className2, byte[] bytes)
    {
        ClassName clazz = classMap.get(className);
        if (clazz!=null)
        {
            switch (clazz)
            {
                case GAME_REGISTRY:
                    for (Method method:clazz.methods)
                    {
                        bytes = insertFirst(method,bytes);
                    }
                    break;
            }
        }
        return bytes;
    }

    private byte[] insertFirst(Method method, byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        MethodNode methodNode = getMethodByName(classNode,method.name,method.args);
        if (methodNode!=null)
        {
            AbstractInsnNode pos = methodNode.instructions.getFirst();
            methodNode.instructions.insertBefore(pos,method.instructions);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }


    public static MethodNode getMethodByName(ClassNode classNode, String name, String args) {
        List<MethodNode> methods = classNode.methods;
        for (int k = 0; k < methods.size(); k++) {
            MethodNode method = methods.get(k);
            if (method.name.equals(name) && method.desc.equals(args)) {
                return method;
            }
        }
        return null;
    }

}