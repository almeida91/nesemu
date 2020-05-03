package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.*;

@Mnemonic("BMI")
@OpCode(code = 0x30, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnMinus implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (registers.getNegativeFlag()) {
            registers.setPc(address);
        }
    }
}
