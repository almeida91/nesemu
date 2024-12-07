package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("LDY")
@OpCode(code = 0xA0, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xA4, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xB4, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0xAC, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0xBC, mode = AddressingMode.ABSOLUTE_X, cycles = 4, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class LoadY implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        registers.setY(memory.read8Bits(address));

        registers.setZeroFlag(registers.getY() == 0);
        registers.setNegativeFlag((registers.getY() & 0x80) != 0);
    }
}
