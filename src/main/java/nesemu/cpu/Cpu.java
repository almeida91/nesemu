package nesemu.cpu;

import com.google.inject.Inject;
import nesemu.memory.Memory;

import java.util.Map;

public class Cpu {

    private Memory memory;
    private Registers registers;
    private Map<Integer, InstructionCall> instructions;

    private int cycles;

    @Inject
    public Cpu(Memory memory, Registers registers, Map<Integer, InstructionCall> instructions) {
        this.memory = memory;
        this.registers = registers;
        this.instructions = instructions;
    }

    public void cycle() {
         if (cycles == 0) {
            int opcode = readMemory();

            InstructionCall call = instructions.get(opcode);
            cycles = call.run();

        } else {
            cycles--;
        }
    }

    public int getRemainingCycles() {
        return cycles;
    }

    private int readMemory() {
        Integer pc = registers.getPc();
        registers.incrementPc();

        return memory.read8Bits(pc);
    }


}
