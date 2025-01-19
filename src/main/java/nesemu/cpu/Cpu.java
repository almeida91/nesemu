package nesemu.cpu;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import nesemu.memory.Memory;
import nesemu.memory.Stack;

import java.util.Map;

@Slf4j
public class Cpu {

    private static final int NMI_ADDRESS = 0xFFFA;
    private static final int IRQ_ADDRESS = 0xFFFE;
    public static final int IRQ_CYCLES = 7;
    public static final int NMI_CYCLES = 8;

    private Memory memory;
    private Registers registers;
    private Stack stack;
    private Map<Integer, InstructionCall> instructions;

    private int cycles;
    private boolean halt;

    @Inject
    public Cpu(Memory memory, Registers registers, Map<Integer, InstructionCall> instructions, Stack stack) {
        this.memory = memory;
        this.registers = registers;
        this.instructions = instructions;
        this.stack = stack;
    }

    public void cycle() {
        if (halt) {
            return;
        }

        if (cycles == 0) {
            int opcode = readMemory();

            InstructionCall call = instructions.get(opcode);
            try {
                cycles = call.run();
            } catch (NullPointerException e) {
                log.debug("Unknown opcode: {} at address {}", opcode, registers.getPc() - 1);
                throw new RuntimeException(e);
            }
        } else {
            cycles--;
        }
    }

    public void reset() {
        halt = false;

        registers.reset();
        registers.setPc(memory.read16Bits(registers.getPc()));
    }

    public void irq() {
        if (registers.getInterruptFlag() || halt) {
            return;
        }

        interrupt(IRQ_ADDRESS, IRQ_CYCLES);
    }

    public void nmi() {
        if (halt) {
            return;
        }

        log.debug("NMI");
        interrupt(NMI_ADDRESS, NMI_CYCLES);
    }

    private void interrupt(int subProcAddress, int cycles) {
        stack.push16Bits(registers.getPc());

        registers.setBreakpointFlag(false);
        registers.setInterruptFlag(true);

        stack.push8Bits(registers.getP());

        registers.setPc(memory.read16Bits(subProcAddress));

        this.cycles = cycles;
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
