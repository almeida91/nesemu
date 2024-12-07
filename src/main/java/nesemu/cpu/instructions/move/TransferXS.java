package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("TXS")
@OpCode(code = 0x9A)
@AllArgsConstructor(onConstructor_ = @Inject)
public class TransferXS implements Instruction {

    private Registers registers;

    @Override
    public void run(int opcode, int address) {
        registers.setSp(registers.getX());
    }
}
