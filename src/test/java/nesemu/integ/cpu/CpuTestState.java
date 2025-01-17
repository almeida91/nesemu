package nesemu.integ.cpu;

import lombok.Data;
import nesemu.cpu.Registers;
import nesemu.integ.common.TestMemory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Data
public class CpuTestState {

    private int pc;
    private int s;
    private int a;
    private int x;
    private int y;
    private int p;

    private List<List<Integer>> ram;

    public void loadInto(Registers registers, TestMemory memory) {
        registers.reset();

        registers.setPc(pc);
        registers.setSp(s);
        registers.setA(a);
        registers.setX(x);
        registers.setY(y);
        registers.setP(p);

        memory.clear();

        for (List<Integer> row : ram) {
            int address = row.get(0);
            int value = row.get(1);

            memory.write8Bits(address, value);
        }
    }

    public void assertEqual(Registers registers, TestMemory memory) {
        // compare register fields
        assertEquals(pc, registers.getPc(), "Program Counter mismatch - expected: %d, actual: %d".formatted(pc, registers.getPc()));
        assertEquals(s, registers.getSp(), "Stack Pointer mismatch - expected: %d, actual: %d".formatted(s, registers.getSp()));
        assertEquals(a, registers.getA(), "Accumulator mismatch - expected: %d, actual: %d".formatted(a, registers.getA()));
        assertEquals(x, registers.getX(), "X Register mismatch - expected: %d, actual: %d".formatted(x, registers.getX()));
        assertEquals(y, registers.getY(), "Y Register mismatch - expected: %d, actual: %d".formatted(y, registers.getY()));
        assertEquals(p, registers.getP(), "Flag register mismatch - expected: %d, actual: %d".formatted(p, registers.getP()));

        // TODO: test memory
    }


}

