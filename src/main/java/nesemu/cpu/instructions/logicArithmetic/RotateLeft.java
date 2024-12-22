package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("ROL")
@OpCode(code = 0x2A)
@OpCode(code = 0x26, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0x36, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0x2E, mode = AddressingMode.ABSOLUTE, cycles = 6)
@OpCode(code = 0x3E, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class RotateLeft implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value;
        int carry = registers.getCarryFlag() ? 1 : 0;

        if (opCode == 0x2A) {
            value = registers.getA();

            registers.setCarryFlag(value > 255);

            value <<= 1;
            value |= carry;
            registers.setA(value & 0xFF);
        } else {
            value = memory.read8Bits(address);

            registers.setCarryFlag(value > 255);

            value <<= 1;
            value |= carry;
            memory.write8Bits(address, value);
        }

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
