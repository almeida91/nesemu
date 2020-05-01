package nesemu.memory;

public interface Mapper {

    void write(int address, int value);

    int read(int address);
}
