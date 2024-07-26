package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("BVS")
@OpCode(code = 0x70, mode = AddressingMode.RELATIVE, crossBoundaryPenalty = true)
@AllArgsConstructor(onConstructor_ = @Inject)
public class BranchOnOverflowSet implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        if (registers.getOverflowFlag()) {
            registers.setPc(address);
        }
    }
}
