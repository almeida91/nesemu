package nesemu.memory;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.Registers;

@AllArgsConstructor(onConstructor_ = @Inject)
public class Stack {

    private Memory memory;

    private Registers registers;

    public void push8Bits(int value) {
        memory.write8Bits(registers.getSp(), value);
        registers.setSp(decrementSp());
    }

    public int pull8Bits() {
        int sp = incrementSp();
        registers.setSp(sp);

        return memory.read8Bits(sp);
    }

    /**
     * 16-bit version of the {@link #push8Bits(int)} operation.
     * It works by first pushing the higher byte and then the second, this way the 16-bit number keeps the endianness of the 6502.
     *
     * Sample:
     * <pre>
     *     Byte order:   1    0
     *     Number:     0xBB 0xAA
     *
     *           Memory address  Value
     *     SP          0xFF       0xBB
     *     SP-1        0xFE       0xAA
     * </pre>
     *
     * This way if you look at just the memory you see:
     * <pre>
     *     Memory address: 0xFE 0xFF
     *     Value:          0xAA 0xBB
     * </pre>
     *
     * Just like any other little-endian chip.
     *
     * @param value the 16-bit value to store on the stack
     */
    public void push16Bits(int value) {
        push8Bits(value >> 8);
        push8Bits(value & 0xFF);
    }

    public int pull16Bits() {
        int value = pull8Bits();
        return (pull8Bits() << 8) | value;
    }

    private int decrementSp() {
        int sp = registers.getSp() - 1;

        if (sp < 0) {
            return 0xFF;
        }

        return sp;
    }

    private int incrementSp() {
        return (registers.getSp() + 1) % 256;
    }
}
