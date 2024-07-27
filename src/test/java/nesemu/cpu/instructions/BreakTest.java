package nesemu.cpu.instructions;

import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import nesemu.memory.Stack;
import nesemu.testUtils.RegistersAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BreakTest {

    public static final int PROGRAM_COUNTER_VALUE = 10;
    public static final int INTERRUPT_INSTRUCTION_POINTER = 0xFFFE;
    public static final int INTERRUPT_INSTRUCTION_ADDRESS = 123;
    private static final Integer STATUS_FLAGS = 0xFF;

    @Mock
    private Stack stack;

    @Mock
    private Memory memory;

    @Mock
    private Registers registers;

    @InjectMocks
    private Break breakInstruction;

    @Test
    public void testRun() {
        when(registers.getPc()).thenReturn(PROGRAM_COUNTER_VALUE);
        when(registers.getP()).thenReturn(STATUS_FLAGS);
        when(memory.read16Bits(INTERRUPT_INSTRUCTION_POINTER)).thenReturn(INTERRUPT_INSTRUCTION_ADDRESS);

        breakInstruction.run(0x00, 0x0800);

        verify(stack, times(1)).push16Bits(PROGRAM_COUNTER_VALUE);
        verify(stack, times(1)).push8Bits(STATUS_FLAGS);

        new RegistersAssertion()
                .withBreakpointFlag(true)
                .withInterruptFlag(true)
                .withPc(INTERRUPT_INSTRUCTION_ADDRESS)
                .doAssertion(registers);
    }
}