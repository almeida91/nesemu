package nesemu.memory;

import lombok.Builder;

@Builder
public class Rom {

    int[][] prgBanks;
    int[][] chrBanks;

    public int readPrgBank16k(int bank, int address) {
        if (bank > prgBanks.length) {
            throw new IllegalArgumentException("Bank is larger than the number of banks");
        }

        if (address > 0x4000) {
            throw new IllegalArgumentException("Address is larger than 16k");
        }

        return prgBanks[bank][address];
    }

    public int readLastPrgBank(int address) {
        if (address > 0x4000) {
            throw new IllegalArgumentException("Address is larger than 16k");
        }

        return prgBanks[getLastPrgBank()][address];
    }

    public int getLastPrgBank() {
        return prgBanks.length - 1;
    }

    public int readPrgBank32k(int bank, int address) {
        if (address > 0x8000) {
            throw new IllegalArgumentException("Address is larger than 32k");
        }

        if (address > 0x4000) {
            return prgBanks[bank + 1][address - 0x4000];
        }

        return prgBanks[bank][address];
    }

    public int readChrBank(int bank, int address) {
        if (bank > chrBanks.length) {
            throw new IllegalArgumentException("Bank is larger than the number of banks");
        }

        if (address > 0x2000) {
            throw new IllegalArgumentException("Address is larger than 8k");
        }

        return chrBanks[bank][address];
    }

    public int getChrSize() {
        return chrBanks.length * 0x2000;
    }
}
