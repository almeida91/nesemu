package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("TAY")
@OpCode(code = 0xA8)
@AllArgsConstructor(onConstructor_ = @Inject)
public class TransferAY implements Instruction {

    private Registers registers;

    @Override
    public void run(int opcode, int address) {
        registers.setY(registers.getA());

        registers.setZeroFlag(registers.getY() == 0);
        registers.setNegativeFlag((registers.getY() & 0x80) != 0);
    }

}
