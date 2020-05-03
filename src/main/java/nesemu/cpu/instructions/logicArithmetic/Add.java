package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.*;
import nesemu.memory.Memory;

@Mnemonic("ADC")
@OpCode(code = 0x69, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0x65, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0x75, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0x61, mode = AddressingMode.INDIRECT_X, cycles = 6)
@OpCode(code = 0x71, mode = AddressingMode.INDIRECT_Y, cycles = 5, crossBoundaryPenalty = true)
@OpCode(code = 0x6D, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0x7D, mode = AddressingMode.ABSOLUTE_X, cycles = 4, crossBoundaryPenalty = true)
@OpCode(code = 0x79, mode = AddressingMode.ABSOLUTE_Y, cycles = 4, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Add implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int memoryValue = memory.read8Bits(address);
        int value = registers.getA() + memoryValue;

        if (registers.getCarryFlag()) {
            value++;
        }

        boolean inputValuesHaveSameSign = ((registers.getA() ^ memoryValue) & 0x80) == 0;
        boolean resultHasSignDifference = ((registers.getA() ^ value) & 0x80) != 0;

        registers.setOverflowFlag(inputValuesHaveSameSign && resultHasSignDifference);
        registers.setCarryFlag(value > 255);
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);

        registers.setA(value & 0xFF);
    }
}
