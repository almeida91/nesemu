package nesemu.debug;

import nesemu.cpu.InstructionCall;

public interface Debugger {

    void callInstruction(InstructionCall  instructionCall, int address);
}
