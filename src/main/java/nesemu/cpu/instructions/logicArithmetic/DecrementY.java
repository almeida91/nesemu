package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("DEY")
@OpCode(code = 0x88)
@AllArgsConstructor(onConstructor_ = @Inject)
public class DecrementY implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        int value = registers.getY() - 1;
        registers.setY(value & 0xFF);

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
