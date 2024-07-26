package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("DEC")
@OpCode(code = 0xC6, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0xD6, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0xCE, mode = AddressingMode.ABSOLUTE, cycles = 3)
@OpCode(code = 0xDE, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Decrement implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value = memory.read8Bits(address) - 1;
        memory.write8Bits(address & 0xFF, value);

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
