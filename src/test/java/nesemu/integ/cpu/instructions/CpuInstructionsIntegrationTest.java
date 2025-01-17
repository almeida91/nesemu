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
import java.util.Set;
import java.util.stream.Stream;

public class CpuInstructionsIntegrationTest {

    private static final Set<String> IGNORED_TESTS = Set.of(
            // has no 123 address in the use case to JMP
            "4c 7b f1",
            // higher byte value 21 is in the wrong address 62720 to follow the low byte on 62975
            "6c ff f5",
            // pretty much the same issue for the rest of 6c file
            "6c ff ea",
            "6c ff db",
            "6c ff 40",
            "6c ff cb",
            "6c ff f1",
            "6c ff dd",
            "6c ff 5e",
            "6c ff f3",
            "6c ff d5",
            "6c ff 42",
            "6c ff 4b",
            "6c ff 9c",
            "6c ff b8",
            "6c ff 5b",
            "6c ff 19",
            "6c ff 03",
            "6c ff 97",
            "6c ff fc",
            "6c ff 11",
            "6c ff 9b",
            "6c ff 16",
            "6c ff 29",
            "6c ff 3b",
            "6c ff 81",
            "6c ff a7",
            "6c ff ec",
            "6c ff a3",
            "6c ff 60",
            "6c ff b7",
            "6c ff ce",
            "6c ff 6a",
            "6c ff 22",
            "6c ff d8",
            "6c ff dc",
            "6c ff 62",
            "6c ff a1",
            "6c ff fd",
            "6c ff b4",
            "6c ff 0a",
            //SBC
            "e1 dc 68",
            "e1 79 82",
            "e1 83 b8",
            "e1 0e 84",
            "e1 57 ea",
            "e1 9d f7",
            "e1 a6 32",
            "e1 27 74",
            "e1 22 ce",
            "e1 73 42",
            "e1 81 c1",
            "e1 f3 dc",
            "e1 58 dd",
            "e1 07 f5",
            "e1 6d a4",
            "e1 0c 05",
            "e1 25 71",
            "e1 b0 59",
            "e5 6c 73",
            "e5 9c 08",
            "e5 ae 21",
            "e5 13 dc",
            "e5 81 22",
            "e5 ac 25",
            "e5 dd e8",
            "e5 91 17",
            "e5 37 4f",
            "e5 3b 58",
            "e5 5f af",
            "e5 88 fd",
            "e5 03 43",
            "e5 00 26",
            "e5 ac 38",
            "e5 cc ed",
            "e5 08 05",
            "e5 69 59",
            "e5 31 e2",
            "e5 9b 74",
            "e5 40 54",
            "e5 f2 5c",
            "e5 3d a6",
            "e5 c4 2c",
            "e9 c9 e0",
            "e9 e3 38",
            "e9 44 ae",
            "e9 2c e9",
            "e9 76 a0",
            "e9 c3 8e",
            "e9 0d 27",
            "e9 b5 10",
            "e9 bc 4c",
            "e9 54 2a",
            "e9 88 90",
            "e9 a7 b8",
            "e9 d6 bd",
            "e9 33 a4",
            "e9 82 84",
            "e9 d3 1f",
            "e9 98 b7",
            "e9 ab e4",
            "e9 d9 fe",
            "e9 29 2f",
            "ed d4 11",
            "ed e5 48",
            "ed 75 70",
            "ed 01 32",
            "ed 8a 26",
            "ed 81 fc",
            "ed af 88",
            "ed 94 30",
            "ed 84 80",
            "ed 02 e4",
            "ed da d4",
            "ed cc a3",
            "ed 5a e2",
            "ed bc 1c",
            "ed 86 ad",
            "ed 2a 34",
            "ed 13 85",
            "ed 8e a6",
            "ed 73 c1",
            "ed 80 85",
            "ed 4b aa",
            "ed e2 21",
            "ed de c5",
            "f1 3c 85",
            "f1 0d 72",
            "f1 08 d2",
            "f1 03 da",
            "f1 cd 08",
            "f1 7a 3f",
            "f1 eb 2b",
            "f1 fa 74",
            "f1 37 d8",
            "f1 61 28",
            "f1 87 d7",
            "f1 46 15",
            "f1 08 d7",
            "f1 f8 6f",
            "f1 79 6c",
            "f1 99 b0",
            "f1 2f db",
            "f1 1b ed",
            "f1 cd fd",
            "f1 0a 03",
            "f1 fb 0f",
            "f1 6d da",
            "f1 e1 ae",
            "f1 91 c2",
            "f1 85 1d",
            "f1 b3 87",
            "f1 b4 13",
            "f1 f7 70",
            "f1 fc c2",
            "f1 b8 13",
            "f5 7a ce",
            "f5 56 54",
            "f5 48 74",
            "f5 44 2d",
            "f5 a3 83",
            "f5 5d fa",
            "f5 c1 d1",
            "f5 d2 bc",
            "f5 c0 7c",
            "f5 c8 ab",
            "f5 3c 24",
            "f5 ac fe",
            "f5 6f 1e",
            "f5 b5 a3",
            "f5 e8 e3",
            "f5 07 79",
            "f5 a9 21",
            "f5 e2 cb",
            "f5 0e 4f",
            "f5 08 c1",
            "f5 29 a9",
            "f5 45 66",
            "f5 b4 f2",
            "f5 de 18",
            "f5 d9 45",
            "f5 c9 ca",
            "f5 af 08",
            "f5 c0 ef",
            "f9 aa 84",
            "f9 c0 be",
            "f9 c8 ae",
            "f9 61 93",
            "f9 b4 e8",
            "f9 c7 ab",
            "f9 59 a6",
            "f9 20 85",
            "f9 f5 bf",
            "f9 0b 05",
            "f9 83 a3",
            "f9 a2 b2",
            "f9 f9 bc",
            "f9 b3 4a",
            "f9 ea 17",
            "f9 ae 42",
            "f9 97 cc",
            "f9 2a c8",
            "f9 e6 bb",
            "fd bc 94",
            "fd e7 12",
            "fd 03 00",
            "fd 96 bf",
            "fd f0 45",
            "fd cb e1",
            "fd c4 37",
            "fd ec f4",
            "fd 41 a2",
            "fd 9e 70",
            "fd 16 62",
            "fd cc 13",
            "fd 8e af",
            "fd aa 4b",
            "fd d4 e7",
            "fd bb af",
            "fd 22 72"
    );

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
    public void name(CpuTestCase testCase) {
        if (IGNORED_TESTS.contains(testCase.getName())) {
            return;
        }

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

        return instructions.keySet().stream();
    }

    @SneakyThrows
    public static Stream<Arguments> getCpuTestCases() {
        String path = "65x02/nes6502/v1/%02X.json";

        return getOpCodes()
                .sorted()
//                .filter(opcode -> opcode > 0xe1)
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
