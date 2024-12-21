package nesemu.memory.mappers;

import com.google.inject.Inject;
import nesemu.memory.Mapper;
import nesemu.memory.MapperCode;
import nesemu.memory.Rom;
import nesemu.ppu.MirroringMode;

@MapperCode(1)
public class MMC1 implements Mapper {

    private static final int RAM_SIZE = 0x2000;
    private static final int MEMORY_LOWER_BOUND = 0x6000;
    private static final int MEMORY_UPPER_BOUND = 0x8000;

    private static final int CHR_FIRST_BANK_UPPER_BOUND = 0x1000;

    private static final int CONTROL_REGISTER_UPPER_BOUND = 0xA000;
    private static final int FIRST_BANK_UPPER_BOUND = 0xC000;
    private static final int FIRST_CHR_BANK_REGISTER_BOUND = FIRST_BANK_UPPER_BOUND;
    private static final int SECOND_CHR_BANK_REGISTER_BOUND = 0xE000;

    private static final int PRG_32K_MODE = 0;
    private static final int ALT_PRG_32k_MODE = 1;
    private static final int FIXED_FIRST_16K_BANK = 2;
    private static final int FIXED_LAST_16K_BANK = 3;

    private int[] ram = new int[RAM_SIZE];

    private int controlRegister = 0x0C;
    private int loadRegister = 0x00;
    private int loadRegisterCounter = 0;
    private MirroringMode  mirroringMode = MirroringMode.HORIZONTAL;

    private int prg16kLowBank = 0x00;
    private int prg16kHighBank = 0x00;
    private int prg32kBank = 0x00;

    private int chr4kLowBank = 0x00;
    private int chr4kHighBank = 0x00;
    private int chr8kBank = 0x00;

    private Rom rom;

    @Inject
    public MMC1(Rom rom) {
        // TODO: RAM save reading
        this.rom = rom;
    }

    @Override
    public void writeCpu(int address, int value) {
        if (address < MEMORY_UPPER_BOUND) {
            ram[address - MEMORY_LOWER_BOUND] = value;
        } else {
            if ((value & 0x80) == 0x80) { // reset flag
                loadRegister = 0x00;
                loadRegisterCounter = 0;
                controlRegister = 0x0C;
            } else {
                loadRegister >>= 1;
                loadRegister |= (value & 0x01) << 4;
                loadRegisterCounter++;

                if (loadRegisterCounter == 5) {
                    writeControlRegister(address);
                }
            }
        }
    }

    @Override
    public int readCpu(int address) {
        if (address < MEMORY_UPPER_BOUND) {
            return ram[address - MEMORY_LOWER_BOUND];
        } else if ((controlRegister & 0b01000) != 0) {
            if (address < FIRST_BANK_UPPER_BOUND) {
                // 16k bank low
                return rom.readPrgBank16k(prg16kLowBank, address - MEMORY_UPPER_BOUND);
            }
            return rom.readPrgBank16k(prg16kHighBank, address - MEMORY_UPPER_BOUND);
        } else {
            return rom.readPrgBank32k(prg32kBank, address - MEMORY_UPPER_BOUND);
        }
    }

    @Override
    public int getCpuLowerBound() {
        return MEMORY_LOWER_BOUND;
    }

    @Override
    public int getCpuUpperBound() {
        return 0xFFFF;
    }

    @Override
    public void writePpu(int address, int value) {

    }

    @Override
    public int readPpu(int address) {
        if ((controlRegister & 0b10000) != 0) {
            if (address < CHR_FIRST_BANK_UPPER_BOUND) {
                return rom.readChrBank(chr4kLowBank, address);
            }
            return rom.readChrBank(chr4kHighBank, address - CHR_FIRST_BANK_UPPER_BOUND);
        }

        return rom.readChrBank(chr8kBank, address);
    }

    @Override
    public int getPpuLowerBound() {
        return 0;
    }

    @Override
    public int getPpuUpperBound() {
        return 0x1FFF;
    }

    @Override
    public MirroringMode getMirroringMode() {
        return mirroringMode;
    }

    /**
     * Writes to the control register based on the address ranges and control register bits:
     * <p>
     * Control Register Range (address < 0xA000):
     * Takes lowest 5 bits of loadRegister (& 0x1F)
     * Uses bits 0-1 (& 0x03) to determine mirroring mode
     * <p>
     * First CHR Bank Range (address < 0xC000):
     * Checks bit 4 of control register (& 0x10)
     * If bit 4 is set: CHR banking mode = LOW_4k
     * If bit 4 is clear: CHR banking mode = FULL_8k
     * <p>
     * Second CHR Bank Range (address < 0xE000):
     * Checks bit 4 of control register (& 0x10)
     * If bit 4 is set: CHR banking mode = HIGH_4k
     * <p>
     * PRG Banking Range (else block):
     * Uses bits 2-3 of control register (& 0b1100)
     * Mode selection:
     * 0b0000: FULL_32k mode
     * 0b0100: LOW_16k mode
     * 0b1000: HIGH_16k mode
     * 0b1100: FULL_16k mode
     *
     *
     * @param address the address to write to
     */
    private void writeControlRegister(int address) {
        if (address < CONTROL_REGISTER_UPPER_BOUND) {
            // control register range
            controlRegister = loadRegister & 0x1F;

            int mirroringValue = controlRegister & 0x03;

            mirroringMode = MirroringMode.fromValue(mirroringValue);
        } else if (address < FIRST_CHR_BANK_REGISTER_BOUND) {
            // first CHR bank range
            int bankNumber = loadRegister & 0x1F;

            // check if bit 4 is set
            if ((controlRegister & 0x10) != 0) {
                chr4kLowBank = bankNumber;
            } else {
                // we ignore the bit 0
                chr8kBank = bankNumber >> 1;
            }
        } else if (address < SECOND_CHR_BANK_REGISTER_BOUND) {
            // second CHR bank range

            // check if bit 4 is set
            if ((controlRegister & 0x10) != 0) {
                chr4kHighBank = loadRegister;
            }
        } else {
            // PRG banking mode
            // bits 2 and 3 are set in the control register for this
            int prgMode = (controlRegister & 0b1100) >> 2;
            int bankNumber = loadRegister & 0x0F;

            switch (prgMode) {
                case PRG_32K_MODE:
                case ALT_PRG_32k_MODE:
                    // we ignore the bit 0
                    prg32kBank = bankNumber >> 1;
                    break;
                case FIXED_FIRST_16K_BANK:
                    prg16kLowBank = 0;
                    prg16kHighBank = bankNumber;
                    break;
                case FIXED_LAST_16K_BANK:
                    prg16kLowBank = bankNumber;
                    prg16kLowBank = rom.getLastPrgBank();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + prgMode);
            }
        }

        loadRegister = 0x00;
        loadRegisterCounter = 0;
    }
}
