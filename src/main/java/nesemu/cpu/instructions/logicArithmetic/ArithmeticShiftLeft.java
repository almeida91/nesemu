package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("ASL")
@OpCode(code = 0x0A)
@OpCode(code = 0x06, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0x16, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0x0E, mode = AddressingMode.ABSOLUTE, cycles = 6)
@OpCode(code = 0x1E, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class ArithmeticShiftLeft implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value;

        if (opCode == 0x0A) {
            registers.setCarryFlag(registers.getA() >> 7 == 1);
            value = registers.getA() << 1;
            registers.setA(value & 0xFF);
        } else {
            value = memory.read8Bits(address);
            registers.setCarryFlag(value >> 7 == 1);
            value <<= 1;
            memory.write8Bits(address, value);
        }

        registers.setCarryFlag(value > 255);
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
