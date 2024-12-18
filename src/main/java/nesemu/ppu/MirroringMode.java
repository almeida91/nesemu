package nesemu.ppu;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MirroringMode {
    HORIZONTAL(3),
    VERTICAL(2),
    ONE_SCREEN_LOWER(1),
    ONE_SCREEN_UPPER(0);

    private int value;

    public static MirroringMode fromValue(int value) {
        for (MirroringMode mode : values()) {
            if (mode.value == value) {
                return mode;
            }
        }

        throw new IllegalArgumentException("Invalid mirroring mode value: " + value);
    }
}
