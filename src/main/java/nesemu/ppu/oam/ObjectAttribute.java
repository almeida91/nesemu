package nesemu.ppu.oam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectAttribute {

    private int id = 0x00;

    private int x = 0x00;
    private int y = 0x00;

    private int value = 0x00;

    public void clear() {
        id = 0x00;
        x = 0x00;
        y = 0x00;
        value = 0x00;
    }
}
