package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("BPL")
@OpCode(code = 0x10, mode = AddressingMode.RELATIVE)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnPlus implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (!registers.getNegativeFlag()) {
            registers.setPc(address);
        }
    }
}
