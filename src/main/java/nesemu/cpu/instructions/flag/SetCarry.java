package nesemu.cpu.instructions.flag;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.Mnemonic;

@Mnemonic("SEC")
@OpCode(code = 0x38)
@AllArgsConstructor(onConstructor_ = @Inject)
public class SetCarry implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setCarryFlag(true);
    }
}
