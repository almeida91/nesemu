package nesemu.memory;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rom {

    int[][] prgBanks;
    int[][] chrBanks;

    public int readPrgBank(int bank, int address) {
        if (bank > prgBanks.length) {
            throw new IllegalArgumentException("Bank is larger than the number of banks");
        }

        if (address > 0x4000) {
            throw new IllegalArgumentException("Address is larger than 16k");
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
}
