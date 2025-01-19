package nesemu.cpu.instructions;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import nesemu.memory.Stack;

/**
 * The BRK instruction forces an interrupt request.
 * By doing so the PC and the status flags are pushed into the stack and the PC is loaded with the value at 0xFFFE.
 */
@Mnemonic("BRK")
@OpCode(code = 0x00, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Break implements Instruction {

    private Stack stack;

    private Memory memory;

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setBreakpointFlag(true);
        registers.setInterruptFlag(true);

        stack.push16Bits(registers.getPc());
        stack.push8Bits(registers.getP());

        registers.setPc(memory.read16Bits(0xFFFE));
        registers.setBreakpointFlag(false);

    }
}
