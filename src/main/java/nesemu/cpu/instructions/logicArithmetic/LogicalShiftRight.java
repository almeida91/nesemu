package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("LSR")
@OpCode(code = 0x4A)
@OpCode(code = 0x46, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0x56, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0x4E, mode = AddressingMode.ABSOLUTE, cycles = 6)
@OpCode(code = 0x5E, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class LogicalShiftRight implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value;

        if (opCode == 0x4A) {
            registers.setCarryFlag((registers.getA() & 1) == 1);
            value = registers.getA() >> 1;
            registers.setA(value);
        }
        else {
            value = memory.read8Bits(address);
            registers.setCarryFlag((value & 1) == 1);
            value >>= 1;
            memory.write8Bits(address, value);
        }

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
