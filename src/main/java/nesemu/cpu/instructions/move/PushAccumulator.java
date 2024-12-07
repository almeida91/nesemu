package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;

@Mnemonic("PHA")
@OpCode(code = 0x48, cycles = 3)
@AllArgsConstructor(onConstructor_ = @Inject)
public class PushAccumulator implements Instruction {

    private Registers registers;
    private Stack stack;

    @Override
    public void run(int opcode, int address) {
        stack.push8Bits(registers.getA());
    }
}
