package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("BNE")
@OpCode(code = 0xD0, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnNotEqual implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (!registers.getZeroFlag()) {
            registers.setPc(address);
        }
    }
}
