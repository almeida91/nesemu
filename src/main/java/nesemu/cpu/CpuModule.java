package nesemu.cpu;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.SneakyThrows;
import nesemu.memory.Memory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CpuModule extends AbstractModule {

    private Multibinder<Instruction> instructionBinder;

    @Override
    @SneakyThrows
    protected void configure() {
         instructionBinder = Multibinder.newSetBinder(binder(), Instruction.class);

         binder().bind(Registers.class).asEagerSingleton();
         binder().bind(Cpu.class).asEagerSingleton();

         loadInstructions();
    }

    @Provides
    @Singleton
    @Inject
    public Map<Integer, InstructionCall> provideInstructionCallMap(Set<Instruction> instructions, Registers registers, Memory memory) {
        Map<Integer, InstructionCall> instructionCallMap = new HashMap<>();
        Set<String> mnemonics = new HashSet<>();

        for (Instruction instruction : instructions) {
            Class<? extends Instruction> clazz = instruction.getClass();

            OpCode[] opCodes = getOpCodes(clazz);

            for (OpCode opCode : opCodes) {
                AddressingMode addressingMode = opCode.mode();
                String mnemonic = getMnemonic(clazz);
                int opCodeNumber = opCode.code();
                int numberOfCycles = opCode.cycles();
                boolean hasCrossPageBoundaryPenalty = opCode.crossBoundaryPenalty();

                InstructionCall instructionCall =
                        new InstructionCall(registers, memory, instruction, addressingMode, mnemonic, opCodeNumber, numberOfCycles, hasCrossPageBoundaryPenalty);

                if (instructionCallMap.containsKey(opCodeNumber)) {
                    throw new IllegalArgumentException(String.format("Duplicated %d opcode", opCodeNumber));
                }

                if (mnemonics.contains(mnemonic)) {
                    throw new IllegalArgumentException(String.format("Duplicated %s mnemonic", mnemonic));
                }

                instructionCallMap.put(opCodeNumber, instructionCall);
                mnemonics.add(mnemonic);
            }
        }

        return instructionCallMap;
    }

    private void loadInstructions() throws ClassNotFoundException {
        String packageName = "nesemu.cpu.instructions";
        URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
        File packageDir = new File(packageUrl.getFile());

        for (File file : packageDir.listFiles()) {
            addClass(packageName, file);
        }
    }

    private void addClass(String packageName, File file) throws ClassNotFoundException {
        if (file.getName().endsWith(".class")) {
            Class<?> clazz = Class.forName(packageName + "." + file.getName().replace(".class", ""));
            Class<Instruction> instructionClass = (Class<Instruction>) clazz;

            instructionBinder.addBinding().to(instructionClass);
        } else {
            for (File innerFile : file.listFiles()) {
                addClass(packageName + "." + file.getName(), innerFile);
            }
        }
    }

    private OpCode[] getOpCodes(Class<? extends Instruction> clazz) {
        if (clazz.isAnnotationPresent(OpCodes.class)) {
            return ((OpCodes) clazz.getAnnotation(OpCodes.class)).value();
        } else if (clazz.isAnnotationPresent(OpCode.class)) {
            return new OpCode[]{(OpCode) clazz.getAnnotation(OpCode.class)};
        }

        return new OpCode[0];
    }

    private String getMnemonic(Class<? extends Instruction> clazz) {
        return ((Mnemonic) clazz.getAnnotation(Mnemonic.class)).value();
    }
}
