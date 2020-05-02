package nesemu.memory;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import nesemu.ppu.Ppu;

/**
 * NES RAM memory and address mapping.
 * It abstracts the PPU, APU, IO and Mapper addresses under a single read/write API.
 *
 * It uses the following mapping:
 * <pre>
 *     Address range    |           Target
 *     -----------------------------------------------
 *     0x0000 - 0x07FF               RAM
 *     0x0800 - 0x1FFF          Mirrored RAM
 *     0x2000 - 0x2007               PPU
 *     0x2008 - 0x3FFF          Mirrored PPU
 *     0x4000 - 0x4017        APU/IO registers
 *     0x4018 - 0x401F    Mirrored APU/IO registers
 *     0x4020 - 0xFFFF         Mapper memory
 * </pre>
 *
 * The mirrored ranges means that the higher bits in those ranges are ignored and the previous range is actually accessed.
 *
 * Example:
 *
 * When calling the 0x08AF the it really calls the 0x00AF address as:
 * <pre>
 *     0x08AF AND 0x1FFF = 0x00AF
 * </pre>
 */
@Slf4j
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
        ram = new int[RAM_UPPER_BOUND + 1];
    }

    /**
     * Reads the value at the memory address' location.
     * It follows the mapping described on {@link Memory}.
     *
     * @param address the address on the memory to return the data
     * @return the value at the provided memory address
     */
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

    /**
     * Read two bytes at once and joins them into a single returned value by taking the first byte into lower half and the second in the high.
     * @param address the first byte's address to be read
     * @return the concatenated value of the two bytes
     */
    public int read16Bits(int address) {
        int value = read8Bits(address);
        value |= read8Bits(address + 1) << 8;
        return value;
    }

    /**
     * Writes the value at the memory address' location.
     * It follows the mapping described on {@link Memory}.
     *
     * @param address the address on the memory to write the data
     * @param value to be written at the provided address
     */
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
            log.info("Called to write memory in APU/IO range");
        } else if (address < APU_IO_TEST_MODE_ADDRESSES) {
            // TODO: IO/APU registers test mode
            log.info("Called to write memory in APU/IO range");
        } else if (address <= MAPPER_UPPER_BOUND) {
            mapper.write(address, value);
        }
    }
}
