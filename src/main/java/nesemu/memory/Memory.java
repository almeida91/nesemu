package nesemu.memory;

public interface Memory {
    int read8Bits(int address);

    int read16Bits(int address);

    void write8Bits(int address, int value);

}
