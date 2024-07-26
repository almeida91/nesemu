package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("CPX")
@OpCode(code = 0xE0, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xE4, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xEC, mode = AddressingMode.ABSOLUTE, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class CompareX implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value = registers.getX() - memory.read8Bits(address);

        registers.setCarryFlag(registers.getX() >= memory.read8Bits(address));
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
