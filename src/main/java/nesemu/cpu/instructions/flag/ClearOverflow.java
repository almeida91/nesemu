package nesemu.cpu.instructions.flag;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.Mnemonic;

@Mnemonic("CLV")
@OpCode(code = 0xB8)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ClearOverflow implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setOverflowFlag(false);
    }
}
