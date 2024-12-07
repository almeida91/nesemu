package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;

@Mnemonic("PLA")
@OpCode(code = 0x68, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class PullAccumulator implements Instruction {

    private Registers registers;
    private Stack stack;

    @Override
    public void run(int opcode, int address) {
        registers.setA(stack.pull8Bits());

        registers.setZeroFlag(registers.getA() == 0);
        registers.setNegativeFlag((registers.getA() & 0x80) != 0);
    }
}
