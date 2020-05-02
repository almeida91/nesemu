package nesemu.cpu.instructions.flag;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.instructions.Mnemonic;

@Mnemonic("CLD")
@OpCode(code = 0xD8)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ClearDecimal implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setDecimalFlag(false);
    }
}