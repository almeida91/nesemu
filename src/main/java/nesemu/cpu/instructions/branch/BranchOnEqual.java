package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.*;

@Mnemonic("BEQ")
@OpCode(code = 0xF0, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnEqual implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (registers.getZeroFlag()) {
            registers.setPc(address);
        }
    }
}
