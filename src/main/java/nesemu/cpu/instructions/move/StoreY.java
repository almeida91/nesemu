package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("STY")
@OpCode(code = 0x84, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0x94, mode = AddressingMode.ZERO_PAGE_X, cycles = 4)
@OpCode(code = 0x8C, mode = AddressingMode.ABSOLUTE, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class StoreY implements Instruction {

    private Registers registers;
    private Memory memory;

    @Override
    public void run(int opcode, int address) {
        memory.write8Bits(address, registers.getY());
    }

}
