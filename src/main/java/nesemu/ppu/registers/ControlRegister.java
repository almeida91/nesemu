package nesemu.ppu.registers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlRegister {

    private int baseNameTable = 0x00;
    private boolean incrementMode = false;
    private boolean spritePatternTable = false;
    private boolean backgroundPatternTable = false;
    private boolean spriteSize = false;
    private boolean masterSlaveSelect = false;
    private boolean nmiEnable = false;

    public int get() {
        int control = 0;

        control |= baseNameTable;
        control |= incrementMode          ? 0b00000010 : 0;
        control |= spritePatternTable     ? 0b00000100 : 0;
        control |= backgroundPatternTable ? 0b00001000 : 0;
        control |= spriteSize             ? 0b00010000 : 0;
        control |= masterSlaveSelect      ? 0b00100000 : 0;
        control |= nmiEnable              ? 0b10000000 : 0;

        return control;
    }

    public void set(int value) {
        baseNameTable = value & 0b00000011;
        incrementMode = (value & 0b00000010) != 0;
        spritePatternTable = (value & 0b00000100) != 0;
        backgroundPatternTable = (value & 0b00001000) != 0;
        spriteSize = (value & 0b00010000) != 0;
        masterSlaveSelect = (value & 0b00100000) != 0;
        nmiEnable = (value & 0b10000000) != 0;
    }
}
