package nesemu.file.loaders;

import nesemu.file.Loader;
import nesemu.file.RomFileOptions;
import nesemu.memory.Rom;

/**
 * Implements the iNES file format.
 *
 */
public class Ines implements Loader {

    public static final int HEADER_SIZE = 16;
    public static final int TRAINER_FILE_SIZE = 512;
    public static final int PRG_PAGE_SIZE = 0x4000;
    public static final int CHR_PAGE_SIZE = 0x2000;

    private byte[] data;

    private int prgRomSize;
    private int chrRomSize;
    private int mapperCode;
    private int prgRamSize;
    private boolean trainerFlag;

    @Override
    public void load(byte[] data) {
        this.data = data;
        validateHeader();
        parseHeader();
    }

    @Override
    public RomFileOptions getRomFileOptions() {
        return RomFileOptions.builder()
                .mapperCode(mapperCode)
                .build();
    }

    @Override
    public Rom getRom() {
        return Rom.builder()
                .prgBanks(readPrgRom())
                .chrBanks(readChrRom())
                .build();
    }

    private void parseHeader() {
        prgRomSize = data[4];
        chrRomSize = data[5];

        // flags 6
        trainerFlag = (data[6] & 0x4) == 0x4;
        mapperCode = data[6] & 0xFF;

        // flags 7
        mapperCode |= (data[7] & 0xF0) << 4;

        // flags 8
        prgRamSize = data[8] & 0xFF;
    }

    private void validateHeader() {
        String header = new String(data, 0, 3);
        if (!header.equals("NES") && data[4] != 0x1A) {
            throw new RuntimeException("Invalid iNES header");
        }
    }

    private int[][] readPrgRom() {
        int[][] prgRom = new int[prgRomSize][PRG_PAGE_SIZE];
        int prgRomStart = getPrgRomStart();

        for (int i = 0; i < prgRomSize; i++) {
            for (int j = 0; j < PRG_PAGE_SIZE; j++) {
                prgRom[i][j] = data[prgRomStart + (i * PRG_PAGE_SIZE) + j];
            }
        }

        return prgRom;
    }

    private int[][] readChrRom() {
        int[][] chrRom = new int[chrRomSize][CHR_PAGE_SIZE];
        int chrRomStart = getChrRomStart();

        for (int i = 0; i < chrRomSize; i++) {
            for (int j = 0; j < CHR_PAGE_SIZE; j++) {
                chrRom[i][j] = data[chrRomStart + (i * CHR_PAGE_SIZE) + j];
            }
        }

        return chrRom;
    }

    private int getPrgRomStart() {
        return HEADER_SIZE + (trainerFlag ? TRAINER_FILE_SIZE : 0);
    }

    private int getChrRomStart() {
        return getPrgRomStart() + (prgRomSize * PRG_PAGE_SIZE);
    }
}
