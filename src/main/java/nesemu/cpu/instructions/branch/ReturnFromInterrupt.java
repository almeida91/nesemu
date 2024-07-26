package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;

@Mnemonic("RTI")
@OpCode(code = 0x40, cycles = 6)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ReturnFromInterrupt implements Instruction {

    private Registers registers;

    private Stack stack;

    @Override
    public void run(int opCode, int address) {
        registers.setP(stack.pull8Bits());
        registers.setPc(stack.pull16Bits());
    }
}
