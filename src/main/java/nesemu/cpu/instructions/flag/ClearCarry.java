package nesemu.cpu.instructions.flag;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.instructions.Mnemonic;

@Mnemonic("CLC")
@OpCode(code = 0x18)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ClearCarry implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setCarryFlag(false);
    }
}
