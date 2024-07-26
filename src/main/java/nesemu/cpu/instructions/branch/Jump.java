package nesemu.cpu.instructions.branch;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.Instruction;
import nesemu.cpu.Mnemonic;
import nesemu.cpu.OpCode;
import nesemu.cpu.Registers;

@Mnemonic("JMP")
@OpCode(code = 0x4C, mode = AddressingMode.ABSOLUTE)
@OpCode(code = 0x6C, mode = AddressingMode.INDIRECT)
@AllArgsConstructor(onConstructor_ = @Inject)
public class Jump implements Instruction {

    private Registers registers;

    @Override
    public void run(int opCode, int address) {
        registers.setPc(address);
    }
}
