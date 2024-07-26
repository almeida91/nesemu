package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("INC")
@OpCode(code = 0xE6, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0xF6, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0xEE, mode = AddressingMode.ABSOLUTE, cycles = 6)
@OpCode(code = 0xFE, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Increment implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value = memory.read8Bits(address) + 1;
        memory.write8Bits(address & 0xFF, value);

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
