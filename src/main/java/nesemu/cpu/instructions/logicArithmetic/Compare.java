package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("CMP")
@OpCode(code = 0xC9, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xC5, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xD5, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0xC1, mode = AddressingMode.INDIRECT_X, cycles = 6)
@OpCode(code = 0xD1, mode = AddressingMode.INDIRECT_Y, cycles = 5, crossBoundaryPenalty = true)
@OpCode(code = 0xCD, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0xDD, mode = AddressingMode.ABSOLUTE_X, cycles = 4, crossBoundaryPenalty = true)
@OpCode(code = 0xD9, mode = AddressingMode.ABSOLUTE_Y, cycles = 4, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Compare implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value = registers.getA() - memory.read8Bits(address);

        registers.setCarryFlag(registers.getA() >= memory.read8Bits(address));
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
