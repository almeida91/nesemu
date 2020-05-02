package nesemu.cpu.instructions.flag;

import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import nesemu.testUtils.RegistersAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestBitTest {

    public static final int MEMORY_ADDRESS = 0x10;

    @Mock
    private Memory memory;

    @Mock
    private Registers registers;

    @InjectMocks
    private TestBit instruction;

    @Test
    public void testClearFlags() {
        int mask = 0x10;
        int value = 0x10;

        when(registers.getA()).thenReturn(mask);
        when(memory.read8Bits(MEMORY_ADDRESS)).thenReturn(value);

        instruction.run(0x00, MEMORY_ADDRESS);

        new RegistersAssertion()
                .withZeroFlag(false)
                .withOverflowFlag(false)
                .withNegativeFlag(false)
                .doAssertion(registers);
    }

    @Test
    public void testZeroFlag() {
        int mask = 0x10;
        int value = 0x20;

        when(registers.getA()).thenReturn(mask);
        when(memory.read8Bits(MEMORY_ADDRESS)).thenReturn(value);

        instruction.run(0x00, MEMORY_ADDRESS);

        new RegistersAssertion()
                .withZeroFlag(true)
                .withOverflowFlag(false)
                .withNegativeFlag(false)
                .doAssertion(registers);
    }

    @Test
    public void testOverflowFlag() {
        int mask = 0x10;
        int value = 0x40;

        when(registers.getA()).thenReturn(mask);
        when(memory.read8Bits(MEMORY_ADDRESS)).thenReturn(value);

        instruction.run(0x00, MEMORY_ADDRESS);

        new RegistersAssertion()
                .withZeroFlag(true)
                .withOverflowFlag(true)
                .withNegativeFlag(false)
                .doAssertion(registers);
    }

    @Test
    public void testNegativeFlag() {
        int mask = 0x10;
        int value = 0x80;

        when(registers.getA()).thenReturn(mask);
        when(memory.read8Bits(MEMORY_ADDRESS)).thenReturn(value);

        instruction.run(0x00, MEMORY_ADDRESS);

        new RegistersAssertion()
                .withZeroFlag(true)
                .withOverflowFlag(false)
                .withNegativeFlag(true)
                .doAssertion(registers);
    }
}