package nesemu.ppu;


import nesemu.memory.Mapper;
import nesemu.memory.Rom;

public class Ppu {

    private static final int NAMETABLES_LOWER_BOUND = 0x2000;
    private static final int NAMETABLES_UPPER_BOUND = 0x3000;
    private static final int PALLETE_LOWER_BOUND = 0x3F00;
    private static final int PALLETE_UPPER_BOUND = 0x4000;

    private Mapper mapper;
    private Rom rom;
    private int[] ram = new int[0x2000];

    public int readMemory(int address) {
        if (address < NAMETABLES_LOWER_BOUND) {
            if (rom.getChrSize() > 0) {
                return mapper.readPpu(address);
            }
            return ram[address];
        }

        return 0;
    }

    public void writeMemory(int address, int value) {

    }

    public int readRegister(int address) {
        return 0;
    }

    public void writeRegister(int address, int value) {

    }

    public void cycle() {

    }
}
