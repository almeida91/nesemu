package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;

@Mnemonic("RTS")
@OpCode(code = 0x60, cycles = 6)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ReturnFromSubroutine implements Instruction {

    private Registers registers;

    private Stack stack;

    @Override
    public void run(int opCode, int address) {
        int value = stack.pull16Bits();
        registers.setPc(value + 1);
    }
}
