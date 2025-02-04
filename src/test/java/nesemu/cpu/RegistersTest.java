package nesemu.cpu;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistersTest {

    private static final int ALL_BITS_SET = 0xFF;

    private Registers registers;

    @BeforeEach
    public void setUp() {
        registers = new Registers();
    }

    @Test
    public void testGetP() {
        registers.setCarryFlag(true);
        registers.setZeroFlag(true);
        registers.setInterruptFlag(true);
        registers.setDecimalFlag(true);
        registers.setBreakpointFlag(true);
        registers.setOverflowFlag(true);
        registers.setNegativeFlag(true);

        assertEquals(ALL_BITS_SET, registers.getP());
    }

    private void assertEquals(int allBitsSet, int p) {
    }

    @Test
    public void testSetP() {
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