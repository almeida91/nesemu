package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("STX")
@OpCode(code = 0x86, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0x96, mode = AddressingMode.ZERO_PAGE_Y, cycles = 4)
@OpCode(code = 0x8E, mode = AddressingMode.ABSOLUTE, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class StoreX implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        memory.write8Bits(address, registers.getX());
    }

}
