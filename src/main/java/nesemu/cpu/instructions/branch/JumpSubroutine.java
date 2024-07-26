package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;

@Mnemonic("JSR")
@OpCode(code = 0x20, mode = AddressingMode.ABSOLUTE)
@AllArgsConstructor(onConstructor_ = @Inject)
public class JumpSubroutine implements Instruction {

    private Registers registers;

    private Stack stack;

    @Override
    public void run(int opCode, int address) {
        stack.push16Bits(registers.getPc() + 2); // doing so we can come back to the next instruction
        registers.setPc(address);
    }
}
