package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("INX")
@OpCode(code = 0xE8)
@AllArgsConstructor(onConstructor_ = @Inject)
public class IncrementX implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        int value = registers.getX() + 1;
        registers.setX(value & 0xFF);

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
