package nesemu.cpu.instructions.move;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("TXA")
@OpCode(code = 0x8A)
@AllArgsConstructor(onConstructor_ = @Inject)
public class TransferXA implements Instruction {

    private Registers registers;

    @Override
    public void run(int opcode, int address) {
        registers.setA(registers.getX());

        registers.setZeroFlag(registers.getA() == 0);
        registers.setNegativeFlag((registers.getA() & 0x80) != 0);
    }

}
