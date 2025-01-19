package nesemu.ppu;


import com.google.inject.Inject;
import nesemu.memory.Mapper;
import nesemu.memory.Rom;
import nesemu.ppu.registers.ControlRegister;
import nesemu.ppu.registers.StatusRegister;

public class Ppu {

    private static final int NAMETABLES_LOWER_BOUND = 0x2000;
    private static final int NAMETABLES_UPPER_BOUND = 0x3000;
    private static final int PALLETE_LOWER_BOUND = 0x3F00;
    private static final int PALLETE_UPPER_BOUND = 0x4000;

    private Mapper mapper;
    private Rom rom;
    private ControlRegister controlRegister;
    private StatusRegister statusRegister;

    private int[] ram = new int[0x2000];
    private int ramBuffer = 0;
    private int ramAddress = 0;

    private int scanline = 0;
    private int cycle = 0;
    private boolean nmi = false;

    @Inject
    public Ppu(Mapper mapper, Rom rom, ControlRegister controlRegister, StatusRegister statusRegister) {
        this.mapper = mapper;
        this.rom = rom;
        this.controlRegister = controlRegister;
        this.statusRegister = statusRegister;
    }

    public void writeMemory(int address, int value) {

    }

    public int readRegister(int address) {
        switch (address) {
            case 0x2000:
                return controlRegister.get();
            case 0x2001:
                break;
            case 0x2002:
                return readStatusRegister();
            case 0x2003:
                break;
            case 0x2004:
                break;
            case 0x2005:
                break;
            case 0x2006:
                break;
            case 0x2007:
                return readRam();
        }

        return 0;
    }

    public void writeRegister(int address, int value) {
        switch (address) {
            case 0x2000:
                controlRegister.set(value);
                break;
            case 0x2001:
                break;
            case 0x2003:
                break;
            case 0x2004:
                break;
            case 0x2005:
                break;
            case 0x2006:
                break;
            case 0x2007:
                break;
        }
    }

    public boolean readNmi() {
        boolean nmi = this.nmi;
        this.nmi = false;
        return nmi;
    }

    public void cycle() {

        if (scanline >= 241 && scanline <= 261 && cycle == 1) {
            statusRegister.setVerticalBlank(true);

            if (controlRegister.isNmiEnable()) {
                nmi = true;
            }
        } else {
            statusRegister.setVerticalBlank(false);
        }

        cycle++;

        if (cycle >= 341) {
            cycle = 0;
            scanline++;
            if (scanline >= 262) {
                scanline = -1;
            }
        }
    }

    private int readMemory(int address) {
        if (address < NAMETABLES_LOWER_BOUND) {
            if (rom.getChrSize() > 0) {
                return mapper.readPpu(address);
            }
            return ram[address];
        }

        return 0;
    }

    private int readStatusRegister() {
        int data = statusRegister.get();
        data |= ramBuffer & 0x1F;

        statusRegister.setVerticalBlank(false);

        return data;
    }

    private int readRam() {
        int data = ramBuffer;
        ramBuffer = readMemory(ramAddress);

        // TODO: palette reading

        ramAddress += controlRegister.isIncrementMode() ? 32 : 1;

        return data;
    }
}
