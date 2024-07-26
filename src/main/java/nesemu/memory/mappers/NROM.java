package nesemu.memory.mappers;

import nesemu.memory.Mapper;

public class NROM implements Mapper {

    @Override
    public void write(int address, int value) {

    }

    @Override
    public int read(int address) {
        return 0;
    }
}
