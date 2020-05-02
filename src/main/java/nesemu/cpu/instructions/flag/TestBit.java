package nesemu.cpu.instructions.flag;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.cpu.instructions.Mnemonic;
import nesemu.memory.Memory;

/**
 * Test bit operation, it takes the value from the memory address and check it against the mask in the register A.
 * This value either clears or sets the zero flag({@link Registers#getZeroFlag()}) in the status register.
 *
 * It also checks the value bits 6 and 7 and sets the overflow flag({@link Registers#getOverflowFlag()}) and the negative flag({@link Registers#getNegativeFlag()}) respectively.
 */
@Mnemonic("BIT")
@OpCode(code = 0x24, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0x2C, mode = AddressingMode.ABSOLUTE, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class TestBit implements Instruction {

    private Memory memory;

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        int value = memory.read8Bits(address);

        registers.setZeroFlag((value & registers.getA()) == 0);
        registers.setOverflowFlag((value & 0x40) != 0);
        registers.setNegativeFlag((value & 0x80) != 0);
    }
}
