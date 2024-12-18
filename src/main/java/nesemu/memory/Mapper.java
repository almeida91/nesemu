package nesemu.memory;

import nesemu.ppu.MirroringMode;

public interface Mapper {

    void writeCpu(int address, int value);

    int readCpu(int address);

    int getCpuLowerBound();

    int getCpuUpperBound();

    void writePpu(int address, int value);

    int readPpu(int address);

    int getPpuLowerBound();

    int getPpuUpperBound();

    MirroringMode getMirroringMode();
}
