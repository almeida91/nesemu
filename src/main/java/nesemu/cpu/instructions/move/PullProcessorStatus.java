package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;


@Mnemonic("PLP")
@OpCode(code = 0x28, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class PullProcessorStatus implements Instruction {

    private Registers registers;
    private Stack stack;

    @Override
    public void run(int opcode, int address) {
        registers.setP(stack.pull8Bits());
    }
}
