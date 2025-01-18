package nesemu.ppu.registers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRegister {

    private boolean spriteOverflow = false;
    private boolean spriteZeroHit = false;
    private boolean verticalBlank = false;

    public int get() {
        int status = 0;

        status |= spriteOverflow ? 0b00100000 : 0;
        status |= spriteZeroHit  ? 0b01000000 : 0;
        status |= verticalBlank  ? 0b10000000 : 0;

        return status;
    }
}
