package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.Mnemonic;

@Mnemonic("BCC")
@OpCode(code = 0x90, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnCarryClear implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (!registers.getCarryFlag()) {
            registers.setPc(address);
        }
    }
}
