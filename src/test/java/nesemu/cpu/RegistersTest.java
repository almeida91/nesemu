package nesemu.cpu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistersTest {

    private static final int ALL_BITS_SET = 0xFF;

    private Registers registers;

    @BeforeEach
    void setUp() {
        registers = new Registers();
    }

    @Test
    void testGetP() {
        registers.setCarryFlag(true);
        registers.setZeroFlag(true);
        registers.setInterruptFlag(true);
        registers.setDecimalFlag(true);
        registers.setBreakpointFlag(true);
        registers.setOverflowFlag(true);
        registers.setNegativeFlag(true);

        assertEquals(ALL_BITS_SET, registers.getP());
    }

    @Test
    void testSetP() {
        registers.setP(ALL_BITS_SET);

        assertTrue(registers.getCarryFlag());
        assertTrue(registers.getZeroFlag());
        assertTrue(registers.getInterruptFlag());
        assertTrue(registers.getDecimalFlag());
        assertTrue(registers.getBreakpointFlag());
        assertTrue(registers.getOverflowFlag());
        assertTrue(registers.getNegativeFlag());
    }
}