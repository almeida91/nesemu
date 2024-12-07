package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("LDA")
@OpCode(code = 0xA9, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xA5, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xB5, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0xAD, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0xBD, mode = AddressingMode.ABSOLUTE_X, cycles = 4, crossBoundaryPenalty = true)
@OpCode(code = 0xB9, mode = AddressingMode.ABSOLUTE_Y, cycles = 4, crossBoundaryPenalty = true)
@OpCode(code = 0xA1, mode = AddressingMode.INDIRECT_X, cycles = 6)
@OpCode(code = 0xB1, mode = AddressingMode.INDIRECT_Y, cycles = 5, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class LoadAccumulator implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        registers.setA(memory.read8Bits(address));

        registers.setZeroFlag(registers.getA() == 0);
        registers.setNegativeFlag((registers.getA() & 0x80) != 0);
    }
}
