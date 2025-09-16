package nesemu.cpu;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import lombok.SneakyThrows;
import nesemu.debug.Debugger;
import nesemu.memory.Memory;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CpuModule extends AbstractModule {

    private Multibinder<Instruction> instructionBinder;

    @Override
    @SneakyThrows
    protected void configure() {
        binder().requireExplicitBindings();

        instructionBinder = Multibinder.newSetBinder(binder(), Instruction.class);

        binder().bind(Registers.class).asEagerSingleton();
        binder().bind(Cpu.class).asEagerSingleton();

        loadInstructions();
    }

    @Provides
    @Singleton
    @Inject
    public Map<Integer, InstructionCall> provideInstructionCallMap(Set<Instruction> instructions, Registers registers, Memory memory, Debugger debugger) {
        Map<Integer, InstructionCall> instructionCallMap = new HashMap<>();
        Set<String> mnemonics = new HashSet<>();

        for (Instruction instruction : instructions) {
            Class<? extends Instruction> clazz = instruction.getClass();

            OpCode[] opCodes = getOpCodes(clazz);

            String mnemonic = getMnemonic(clazz);

            if (mnemonics.contains(mnemonic)) {
                throw new IllegalArgumentException(String.format("Duplicated %s mnemonic", mnemonic));
            }

            mnemonics.add(mnemonic);

            for (OpCode opCode : opCodes) {
                AddressingMode addressingMode = opCode.mode();
                int opCodeNumber = opCode.code();
                int numberOfCycles = opCode.cycles();
                boolean hasCrossPageBoundaryPenalty = opCode.crossBoundaryPenalty();

                InstructionCall instructionCall =
                        new InstructionCall(
                                registers,
                                memory,
                                instruction,
                                debugger,
                                addressingMode,
                                mnemonic,
                                opCodeNumber,
                                numberOfCycles,
                                hasCrossPageBoundaryPenalty);

                if (instructionCallMap.containsKey(opCodeNumber)) {
                    throw new IllegalArgumentException(String.format("Duplicated %d opcode", opCodeNumber));
                }

                instructionCallMap.put(opCodeNumber, instructionCall);
            }
        }

        return instructionCallMap;
    }

    @Provides
    @Singleton
    @Named("instructionOpCodes")
    public Set<Integer> provideInstructionOpcodes() {
        Reflections reflections = new Reflections("nesemu.cpu.instructions");

        return reflections.getSubTypesOf(Instruction.class).stream()
                .flatMap(aClass -> Stream.of(getOpCodes(aClass)))
                .map(opCode -> opCode.code())
                .collect(Collectors.toSet());
    }

    private void loadInstructions() {
        Reflections reflections = new Reflections("nesemu.cpu.instructions");

        Set<Class<? extends Instruction>> classes = reflections.getSubTypesOf(Instruction.class);

        for (Class<?> clazz : classes) {
            Class<Instruction> instructionClass = (Class<Instruction>) clazz;
            instructionBinder.addBinding().to(instructionClass);
        }
    }

    private OpCode[] getOpCodes(Class<? extends Instruction> clazz) {
        if (clazz.isAnnotationPresent(OpCodes.class)) {
            return clazz.getAnnotation(OpCodes.class).value();
        } else if (clazz.isAnnotationPresent(OpCode.class)) {
            return new OpCode[]{clazz.getAnnotation(OpCode.class)};
        }

        return new OpCode[0];
    }

    private String getMnemonic(Class<? extends Instruction> clazz) {
        return clazz.getAnnotation(Mnemonic.class).value();
    }
}
