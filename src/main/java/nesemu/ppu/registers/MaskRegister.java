package nesemu.ppu.registers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaskRegister {

    private boolean greyscale;
    private boolean showBackgroundLeftmost8Pixels;
    private boolean showSpritesLeftmost8Pixels;
    private boolean enableBackground;
    private boolean enableSprites;
    private boolean emphasizeRed;
    private boolean emphasizeGreen;
    private boolean emphasizeBlue;

    public int get() {
        int mask = 0;

        mask |= greyscale                     ? 0b00000001 : 0;
        mask |= showBackgroundLeftmost8Pixels ? 0b00000010 : 0;
        mask |= showSpritesLeftmost8Pixels    ? 0b00000100 : 0;
        mask |= enableBackground              ? 0b00001000 : 0;
        mask |= enableSprites                 ? 0b00010000 : 0;
        mask |= emphasizeRed                  ? 0b00100000 : 0;
        mask |= emphasizeGreen                ? 0b01000000 : 0;
        mask |= emphasizeBlue                 ? 0b10000000 : 0;

        return mask;
    }

    public void set(int value) {
        greyscale = (value & 0b00000001) != 0;
        showBackgroundLeftmost8Pixels = (value & 0b00000010) != 0;
        showSpritesLeftmost8Pixels = (value & 0b00000100) != 0;
        enableBackground = (value & 0b00001000) != 0;
        enableSprites = (value & 0b00010000) != 0;
        emphasizeRed = (value & 0b00100000) != 0;
        emphasizeGreen = (value & 0b01000000) != 0;
        emphasizeBlue = (value & 0b10000000) != 0;
    }
}
