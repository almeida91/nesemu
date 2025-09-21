package nesemu.ppu.oam;

import lombok.Getter;
import lombok.Setter;

public class ObjectAttributeMemory {

    private ObjectAttribute[] objectAttributes = new ObjectAttribute[64];
    private ObjectAttribute[] visibleObjectAttributes = new ObjectAttribute[8];

    @Getter
    @Setter
    private int address = 0;
}
