package nesemu.cpu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nesemu.debug.Debugger;
import nesemu.memory.Memory;

@AllArgsConstructor
public class InstructionCall {

    private Registers registers;

    private Memory memory;

    private Instruction instruction;

    private Debugger debugger;

    @Getter
    private AddressingMode addressingMode;

    @Getter
    private String mnemonic;

    @Getter
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

        debugger.callInstruction(this, address);

        instruction.run(opCode, address);

        if (hasCrossPageBoundaryPenalty && crossesPageBoundary(address)) {
            return cycles + 1;
        }

        return cycles;
    }

    private boolean crossesPageBoundary(int address) {
        int pcPage = registers.getPc() / 256;
        int addressPage = address / 256;

        return pcPage != addressPage;
    }

    private int getAddress() {
        return switch (addressingMode) {
            case ABSOLUTE -> readAbsolute();
            case ABSOLUTE_X -> readAbsoluteX();
            case ABSOLUTE_Y -> readAbsoluteY();
            case IMMEDIATE -> readImmediate();
            case INDIRECT -> readIndirect();
            case INDIRECT_X -> readIndirectX();
            case INDIRECT_Y -> readIndirectY();
            case RELATIVE -> readRelative();
            case ZERO_PAGE -> readZeroPage();
            case ZERO_PAGE_X -> readZeroPageX();
            case ZERO_PAGE_Y -> readZeroPageY();
            default -> 0;
        };
    }

    private int readImmediate() {
        return registers.incrementPc() & 0xFFFF;
    }

    private int readZeroPage() {
        return memory.read8Bits(registers.incrementPc()) & 0xFF;
    }

    private int readZeroPageX() {
        int address = memory.read8Bits(registers.incrementPc());
        address += registers.getX();
        address &= 0xFF;

        return address;
    }

    private int readZeroPageY() {
        int address = memory.read8Bits(registers.incrementPc());
        address += registers.getY();
        address &= 0xFF;

        return address;
    }

    private int readAbsolute() {
        int address = registers.incrementPcByTwoAddress();
        return memory.read16Bits(address);
    }

    private int readAbsoluteX() {
        return (readAbsolute() + registers.getX()) & 0xFFFF;
    }

    private int readAbsoluteY() {
        return (readAbsolute() + registers.getY()) & 0xFFFF;
    }

    private int readRelative() {
        int address = registers.incrementPc();
        return address + 1 + getSignedValue(memory.read8Bits(address) & 0xFF);
    }

    private int readIndirect() {
        int address = memory.read16Bits(registers.incrementPc() & 0xFFFF);
        return memory.read16Bits(address);
    }

    private int readIndirectX() {
        int operandAddress = registers.incrementPc();
        int zeroPageAddress = (memory.read8Bits(operandAddress) + registers.getX()) & 0xFF;

        int address = memory.read8Bits(zeroPageAddress);
        address |= memory.read8Bits((zeroPageAddress + 1) & 0xFF) << 8;

        return address;
    }

    private int readIndirectY() {
        int operandAddress = registers.incrementPc();
        int zeroPageAddress = (memory.read8Bits(operandAddress)) & 0xFF;

        int address = memory.read8Bits(zeroPageAddress);
        address |= memory.read8Bits((zeroPageAddress + 1) & 0xFF) << 8;

        address += registers.getY();
        address &= 0xFFFF;

        return address;

    }

    private int getSignedValue(int value) {
        return value > 127 ? value - 256 : value;
    }
}