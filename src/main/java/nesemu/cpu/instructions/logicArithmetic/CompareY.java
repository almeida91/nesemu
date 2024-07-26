package nesemu.cpu.instructions.logicArithmetic;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;

@Mnemonic("CPY")
@OpCode(code = 0xC0, mode = AddressingMode.IMMEDIATE)
@OpCode(code = 0xC4, mode = AddressingMode.ZERO_PAGE, cycles = 3)
@OpCode(code = 0xCC, mode = AddressingMode.ABSOLUTE, cycles = 4)
@AllArgsConstructor(onConstructor_ = @Inject)
public class CompareY implements Instruction {

    private Registers registers;

    private Memory memory;

    @Override
    public void run(int opCode, int address) {
        int value = registers.getY() - memory.read8Bits(address);

        registers.setCarryFlag(registers.getY() >= memory.read8Bits(address));
        registers.setNegativeFlag((value & 0x80) != 0);
        registers.setZeroFlag((value & 0xFF) == 0);
    }
}
