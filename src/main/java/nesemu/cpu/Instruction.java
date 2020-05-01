package nesemu.cpu;

import nesemu.memory.Memory;

public interface Instruction {

    void run(int opCode, int address, Cpu cpu, Memory memory);
}
