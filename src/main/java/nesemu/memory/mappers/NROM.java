package nesemu.memory.mappers;

import com.google.inject.Inject;
import nesemu.memory.Mapper;
import nesemu.memory.MapperCode;
import nesemu.memory.Rom;
import nesemu.ppu.MirroringMode;

@MapperCode(0)
public class NROM implements Mapper {

    private static final int MEMORY_SIZE = 0x2000;
    private static final int MEMORY_UPPER_BOUND = 0x8000;
    private static final int MEMORY_LOWER_BOUND = 0x6000;

    private final Rom rom;
    private int[] ram;

    @Inject
    public NROM(Rom rom) {
        this.rom = rom;
        this.ram = new int[MEMORY_SIZE];
    }

    @Override
    public void writeCpu(int address, int value) {
        if (address < MEMORY_UPPER_BOUND) {
            ram[address - MEMORY_LOWER_BOUND] = value;
        }
    }

    @Override
    public int readCpu(int address) {
        if (address < MEMORY_UPPER_BOUND) {
            return ram[address - MEMORY_LOWER_BOUND];
        } else if (address < 0xC000) {
            return rom.readPrgBank(0, address - MEMORY_UPPER_BOUND);
        }
        return rom.readPrgBank(0, address - 0xC000);
    }

    @Override
    public int getCpuLowerBound() {
        return MEMORY_LOWER_BOUND;
    }

    @Override
    public int getCpuUpperBound() {
        return MEMORY_UPPER_BOUND;
    }

    @Override
    public void writePpu(int address, int value) {

    }

    @Override
    public int readPpu(int address) {
        return 0;
    }

    @Override
    public int getPpuLowerBound() {
        return 0;
    }

    @Override
    public int getPpuUpperBound() {
        return 0;
    }

    @Override
    public MirroringMode getMirroringMode() {
        return null;
    }
}
