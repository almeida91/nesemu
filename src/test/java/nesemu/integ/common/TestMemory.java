package nesemu.integ.common;

import lombok.Getter;
import nesemu.memory.Memory;

import java.util.HashMap;
import java.util.Map;

public class TestMemory implements Memory {

    @Getter
    private Map<Integer, Integer> ram = new HashMap<>();

    @Override
    public int read8Bits(int address) {
        int value = ram.getOrDefault(address, 0);
//        System.out.println("%d: %d".formatted(address, value));
        return value;
    }

    @Override
    public int read16Bits(int address) {
        int value = read8Bits(address);
        value |= read8Bits(address + 1) << 8;
        return value;
    }

    @Override
    public void write8Bits(int address, int value) {
        ram.put(address, value & 0xFF);
    }

    public void clear() {
        ram.clear();
    }
}
