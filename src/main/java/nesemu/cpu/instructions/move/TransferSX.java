package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("TSX")
@OpCode(code = 0xBA)
@AllArgsConstructor(onConstructor_ = @Inject)
public class TransferSX implements Instruction {

    private Registers registers;

    @Override
    public void run(int opcode, int address) {
        registers.setX(registers.getSp());

        registers.setZeroFlag(registers.getX() == 0);
        registers.setNegativeFlag((registers.getX() & 0x80) != 0);
    }
}