package nesemu.cpu.instructions;

import nesemu.cpu.Instruction;
import nesemu.cpu.OpCode;

/**
 * NOP code.
 * It should not make any change to the CPU state.
 */
@Mnemonic("NOP")
@OpCode(code = 0xEA)
public class NoOperation implements Instruction {

    @Override
    public void run(int opCode, int address) {

    }
}
