package nesemu.integ.cpu.instructions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import lombok.SneakyThrows;
import nesemu.cpu.Cpu;
import nesemu.cpu.CpuModule;
import nesemu.cpu.InstructionCall;
import nesemu.cpu.Registers;
import nesemu.integ.common.TestMemory;
import nesemu.integ.cpu.CpuTestCase;
import nesemu.integ.cpu.CpuTestsModule;
import nesemu.memory.Memory;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CpuInstructionsIntegrationTest {


    private static Injector injector;
    private Cpu cpu;
    private TestMemory memory;
    private Registers registers;

    @BeforeAll
    public static void setup() {
        injector = Guice.createInjector(
                new CpuTestsModule(),
                new CpuModule()
        );
    }


    @BeforeEach
    public void beforeEach() {
        cpu = injector.getInstance(Cpu.class);
        memory = (TestMemory) injector.getInstance(Memory.class);
        registers = injector.getInstance(Registers.class);
    }

    @ParameterizedTest
    @MethodSource("getCpuTestCases")
    public void name(CpuTestCase testCase) throws IOException {
        testCase.getInitialState().loadInto(registers, memory);

        do {
            cpu.cycle();
        } while (cpu.getRemainingCycles() > 0);

        testCase.getFinalState().assertEqual(registers, memory);
    }

    static Stream<Integer> getOpCodes() {
        TypeLiteral<Map<Integer, InstructionCall>> typeLiteral = new TypeLiteral<>() {
        };
        Map<Integer, InstructionCall> instructions = injector.getInstance(Key.get(typeLiteral));

//        return Streams.of(0x26);
        return instructions.keySet().stream();
    }

    @SneakyThrows
    public static Stream<Arguments> getCpuTestCases() {
        String path = "65x02/nes6502/v1/%02X.json";

        return getOpCodes()
                .sorted()
//                .filter(opcode -> opcode > 0x26)
                .map(opCode -> {
                    try {
                        return CpuTestCase.loadFile(String.format(path, opCode).toLowerCase());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).flatMap(List::stream)
                .map(Arguments::of);
    }
}
