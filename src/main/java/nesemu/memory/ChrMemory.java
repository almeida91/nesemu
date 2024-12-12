package nesemu.memory;

public class ChrMemory {

    private int[] ram = new int[0x800];

    public int read(int address) {
        return ram[address];
    }

    public void write(int address, int value) {
        ram[address] = value;
    }
}
