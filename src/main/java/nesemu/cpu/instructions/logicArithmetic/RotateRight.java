package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("ROR")
@OpCode(code = 0x6A)
@OpCode(code = 0x66, mode = AddressingMode.ZERO_PAGE, cycles = 5)
@OpCode(code = 0x76, mode = AddressingMode.ZERO_PAGE_X, cycles = 6)
@OpCode(code = 0x6E, mode = AddressingMode.ABSOLUTE, cycles = 6)
@OpCode(code = 0x7E, mode = AddressingMode.ABSOLUTE_X, cycles = 7)
@AllArgsConstructor(onConstructor_ = @Inject)
public class RotateRight implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value;

        int carry = registers.getCarryFlag() ? 0x80 : 0;

        if (opCode == 0x6A) {
            value = registers.getA();

            registers.setCarryFlag((value & 1) == 1);

            value >>= 1;
            value |= carry;

            registers.setA(value);
        }
        else {
            value = memory.read8Bits(address);


            registers.setCarryFlag((value & 1) == 1);

            value >>= 1;
            value |= carry;

            memory.write8Bits(address, value & 0xFF);
        }

        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
