package nesemu.memory.mappers;

import com.google.inject.Inject;
import nesemu.memory.ChrBankingMode;
import nesemu.memory.Mapper;
import nesemu.memory.MapperCode;
import nesemu.memory.PrgBankingMode;
import nesemu.memory.Rom;
import nesemu.ppu.MirroringMode;

@MapperCode(1)
public class MMC1 implements Mapper {

    private static final int RAM_SIZE = 0x2000;
    private static final int MEMORY_LOWER_BOUND = 0x6000;
    private static final int MEMORY_UPPER_BOUND = 0x8000;
    private static final int CONTROL_REGISTER_UPPER_BOUND = 0xA000;
    private static final int FIRST_BANK_UPPER_BOUND = 0xC000;
    private static final int SECOND_BANK_UPPER_BOUND = 0xE000;

    private int[] ram = new int[RAM_SIZE];

    private int controlRegister = 0x0C;
    private int loadRegister = 0x00;
    private int loadRegisterCounter = 0;
    private MirroringMode  mirroringMode = MirroringMode.HORIZONTAL;

    private ChrBankingMode chrBankingMode = null;
    private PrgBankingMode prgBankingMode = null;

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
        } else if (address < CONTROL_REGISTER_UPPER_BOUND) {
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
        return 0;
    }

    @Override
    public int getCpuLowerBound() {
        return 0x6000;
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
        return 0;
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
     * Control Register Range (address < CONTROL_REGISTER_UPPER_BOUND):
     * Takes lowest 5 bits of loadRegister (& 0x1F)
     * Uses bits 0-1 (& 0x03) to determine mirroring mode
     * <p>
     * First CHR Bank Range (address < FIRST_BANK_UPPER_BOUND):
     * Checks bit 4 of control register (& 0x10)
     * If bit 4 is set: CHR banking mode = LOW_4k
     * If bit 4 is clear: CHR banking mode = FULL_8k
     * <p>
     * Second CHR Bank Range (address < SECOND_BANK_UPPER_BOUND):
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
        } else if (address < FIRST_BANK_UPPER_BOUND) {
            // first CHR bank range
            if ((controlRegister & 0x10) == 0x10) {
                // if bit 4 is set
                chrBankingMode = ChrBankingMode.LOW_4k;
            } else {
                // if bit 4 is not set
                chrBankingMode = ChrBankingMode.FULL_8k;// 8KB
            }
        } else if (address < SECOND_BANK_UPPER_BOUND) {
            // second CHR bank range

            if ((controlRegister & 0x10) == 0x10) {
                // if bit 4 is set
                chrBankingMode = ChrBankingMode.HIGH_4k;
            }
        } else {
            // PRG banking mode
            // bits 2 and 3 are set in the control register for this
            int prgMode = controlRegister & 0b1100;

            prgBankingMode = switch (prgMode) {
                case 0b0000 -> PrgBankingMode.FULL_32k;
                case 0b0100 -> PrgBankingMode.LOW_16k;
                case 0b1000 -> PrgBankingMode.HIGH_16k;
                case 0b1100 -> PrgBankingMode.FULL_16k;
                default -> throw new IllegalStateException("Unexpected value: " + prgMode);
            };
        }

        loadRegister = 0x00;
        loadRegisterCounter = 0;
    }
}
