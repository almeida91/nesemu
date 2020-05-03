package nesemu.cpu;

import lombok.AllArgsConstructor;
import nesemu.memory.Memory;

@AllArgsConstructor
public class InstructionCall {

    private Registers registers;

    private Memory memory;

    private Instruction instruction;

    private AddressingMode addressingMode;

    private String mnemonic;

    private int opCode;

    private int cycles;

    private boolean hasCrossPageBoundaryPenalty;

    /**
     * Runs the instruction mapped to the stored opCode
     *
     * @return the number of cycles it takes to execute it
     */
    public int run() {
        int address = getAddress();

        instruction.run(opCode, address);

        if (hasCrossPageBoundaryPenalty) {
            return cycles + 1;
        }

        return cycles;
    }

    private int getAddress() {
        switch (addressingMode) {
            case ABSOLUTE:
                return readAbsolute();
            case ABSOLUTE_X:
                return readAbsoluteX();
            case ABSOLUTE_Y:
                return readAbsoluteY();
            case IMMEDIATE:
                return readImmediate();
            case INDIRECT:
                return readIndirect();
            case INDIRECT_X:
                return readIndirectX();
            case INDIRECT_Y:
                return readIndirectY();
            case RELATIVE:
                return readRelative();
            case ZERO_PAGE:
                return readZeroPage();
            case ZERO_PAGE_X:
                return readZeroPageX();
            case ZERO_PAGE_Y:
                return readZeroPageY();
            default:
                return 0;
        }
    }

    private int readImmediate() {
        return registers.incrementPc();
    }

    private int readZeroPage() {
        int address = registers.incrementPc();
        return memory.read8Bits(address);
    }

    private int readZeroPageX() {
        return readZeroPage() + registers.getX();
    }

    private int readZeroPageY() {
        return readZeroPage() + registers.getY();
    }

    private int readAbsolute() {
        int address = registers.incrementPcByTwoAddress();
        return memory.read16Bits(address);
    }

    private int readAbsoluteX() {
        return readAbsolute() + registers.getX();
    }

    private int readAbsoluteY() {
        return readAbsolute() + registers.getY();
    }

    private int readRelative() {
        int address = registers.incrementPc();
        return address + memory.read8Bits(address);
    }

    private int readIndirect() {
        int address = memory.read16Bits(registers.incrementPcByTwoAddress());
        return memory.read16Bits(address);
    }

    private int readIndirectX() {
        int address = memory.read16Bits(registers.incrementPcByTwoAddress()) + registers.getX();
        return memory.read16Bits(address);
    }

    private int readIndirectY() {
        int address = memory.read16Bits(registers.incrementPcByTwoAddress()) + registers.getY();
        return memory.read16Bits(address);

    }
}
