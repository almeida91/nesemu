package nesemu.memory;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nesemu.cpu.Registers;

/**
 * A facade to the {@link NesMemory} and {@link Registers} classes, to implement the stack push/pull logic.
 */
@AllArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class Stack {

    private static final int STACK_START = 0x0100;

    private Memory memory;

    private Registers registers;

    /**
     * Pushes the provided byte into the current address on the SP register.
     * After writing to the memory, it decrements the register by one.
     *
     * @param value the value to be pushed to the stack
     */
    public void push8Bits(int value) {
        memory.write8Bits(registers.getSp() + STACK_START, value);
        registers.setSp(decrementSp());
    }

    /**
     * Pulls the byte indexed by incrementing the current SP register value by one.
     *
     * @return the value on the top of the stack
     */
    public int pull8Bits() {
        int sp = incrementSp();
        registers.setSp(sp);

        return memory.read8Bits(sp + STACK_START);
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

    /**
     * 16-bit version of {@link #push8Bits(int)}.
     *
     * Analogue to the {@link #push16Bits(int)} internal procedure, it first pulls the lower byte and then the higher one.
     *
     * @return the two bytes on the top of the stack keeping the little endian layout.
     */
    public int pull16Bits() {
        int value = pull8Bits();
        return (pull8Bits() << 8) | value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 0xFF; i++) {
            sb.append(String.format("%02X", memory.read8Bits(i + STACK_START))).append(" ");
        }

        return sb.toString();
    }

    private int decrementSp() {
        int sp = registers.getSp() - 1;

        if (sp < 0) {
            log.debug("Stack overflow");
            return 0xFF;
        }

        return sp;
    }

    private int incrementSp() {
        return (registers.getSp() + 1) % 256;
    }
}
