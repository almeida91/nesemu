package nesemu.memory;

import com.google.inject.Inject;
import nesemu.ppu.Ppu;

public class Memory {

    public static final int RAM_UPPER_BOUND = 0x07FF;
    public static final int MIRRORED_RAM_UPPER_BOUND = 0x1FFF;
    public static final int PPU_UPPER_BOUND = 0x2007;
    public static final int MIRRORED_PPU_UPPER_BOUND = 0x3FFF;
    public static final int APU_IO_UPPER_BOUND = 0x4017;
    public static final int APU_IO_TEST_MODE_ADDRESSES = 0x401F;
    public static final int MAPPER_UPPER_BOUND = 0xFFFF;

    private Ppu ppu;

    private Mapper mapper;

    private int[] ram;

    @Inject
    public Memory(Ppu ppu, Mapper mapper) {
        this.ppu = ppu;
        this.mapper = mapper;
    }

    public int read8Bits(int address) {
        if (address <= RAM_UPPER_BOUND) {
            return ram[address];
        } else if (address <= MIRRORED_RAM_UPPER_BOUND) {
            return ram[address & RAM_UPPER_BOUND];
        } else if (address < PPU_UPPER_BOUND) {
            return ppu.readMemory(address);
        } else if (address < MIRRORED_PPU_UPPER_BOUND) {
            return ppu.readMemory(address & PPU_UPPER_BOUND);
        } else if (address < APU_IO_UPPER_BOUND) {
            // TODO: IO registers / apu registers
            return 0;
        } else if (address < APU_IO_TEST_MODE_ADDRESSES) {
            // TODO: IO/APU registers test mode
            return 0;
        } else if (address <= MAPPER_UPPER_BOUND) {
            return mapper.read(address);
        } else {
            // TODO: exception?
            // need to check the behavior of such access, might now even be possible as the NES system only address 16 bits.
            return 0;
        }
    }

    public int read16Bits(int address) {
        int value = read8Bits(address);
        value |= read8Bits(address + 1) << 8;
        return value;
    }

    public void write8Bits(int address, int value) {
        if (address <= RAM_UPPER_BOUND) {
            ram[address] = value;
        } else if (address <= MIRRORED_RAM_UPPER_BOUND) {
            ram[address & RAM_UPPER_BOUND] = value;
        } else if (address < PPU_UPPER_BOUND) {
            ppu.writeMemory(address, value);
        } else if (address < MIRRORED_PPU_UPPER_BOUND) {
            ppu.writeMemory(address & PPU_UPPER_BOUND, value);
        } else if (address < APU_IO_UPPER_BOUND) {
            // TODO: IO registers / apu registers
            throw new RuntimeException("APU/IO access not implemented.");
        } else if (address < APU_IO_TEST_MODE_ADDRESSES) {
            // TODO: IO/APU registers test mode
            throw new RuntimeException("APU/IO access not implemented.");
        } else if (address <= MAPPER_UPPER_BOUND) {
            mapper.write(address, value);
        }
    }
}
