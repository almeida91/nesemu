package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("LDX")
@OpCode(code = 0xA2, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xA6, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xB6, mode = AddressingMode.ZERO_PAGE_Y, cycles = 4)
@OpCode(code = 0xAE, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0xAE, mode = AddressingMode.ABSOLUTE_Y, cycles = 4, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class LoadX implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        registers.setX(memory.read8Bits(address));

        registers.setZeroFlag(registers.getX() == 0);
        registers.setNegativeFlag((registers.getX() & 0x80) != 0);
    }
}
