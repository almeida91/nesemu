package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("STA")
@OpCode(code = 0x85, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0x95, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0x8D, mode = AddressingMode.ABSOLUTE, cycles = 4)
@OpCode(code = 0x9D, mode = AddressingMode.ABSOLUTE_X, cycles = 5)
@OpCode(code = 0x99, mode = AddressingMode.ABSOLUTE_Y, cycles = 5)
@OpCode(code = 0x81, mode = AddressingMode.INDIRECT_X, cycles = 6)
@OpCode(code = 0x91, mode = AddressingMode.INDIRECT_Y, cycles = 6)
@AllArgsConstructor(onConstructor_ = @Inject)
public class StoreAccumulator implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        memory.write8Bits(address, registers.getA());
    }

}
