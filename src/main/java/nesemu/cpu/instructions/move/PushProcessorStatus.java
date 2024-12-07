package nesemu.cpu.instructions.move;


import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Stack;


@Mnemonic("PHP")
@OpCode(code = 0x08, cycles = 3)
@AllArgsConstructor(onConstructor_ = @Inject)
public class PushProcessorStatus implements Instruction {

    private Registers registers;
    private Stack stack;

    @Override
    public void run(int opcode, int address) {
        stack.push8Bits(registers.getP());
    }

}
