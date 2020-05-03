package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.*;

@Mnemonic("BCS")
@OpCode(code = 0xB0, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnCarrySet implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (registers.getCarryFlag()) {
            registers.setPc(address);
        }
    }
}
