package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("SBC")
@OpCode(code = 0xE9, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xE5, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xF5, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0xE1, mode = AddressingMode.INDIRECT_X, cycles = 6)
@OpCode(code = 0xF1, mode = AddressingMode.INDIRECT_Y, cycles = 5, crossBoundaryPenalty = true)
@OpCode(code = 0xED, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0xFD, mode = AddressingMode.ABSOLUTE_X, cycles = 4, crossBoundaryPenalty = true)
@OpCode(code = 0xF9, mode = AddressingMode.ABSOLUTE_Y, cycles = 4, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Subtract implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int memoryValue = memory.read8Bits(address);
        int value = registers.getA() - memoryValue;

        value -= (registers.getCarryFlag() ? 0 : 1);

        registers.setA(value & 0xFF);

        boolean inputValuesHaveSameSign = ((registers.getA() ^ memoryValue) & 0x80) == 0;
        boolean resultHasSignDifference = ((registers.getA() ^ value) & 0x80) != 0;

        registers.setOverflowFlag(inputValuesHaveSameSign && resultHasSignDifference);
        registers.setCarryFlag(value > 255);
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);

    }
}
