package nesemu.memory;

import nesemu.cpu.Registers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StackTest {

    private static final int INITIAL_SP_ADDRESS = 12;
    private static final int ONE_BYTE_VALUE = 5;
    private static final int STACK_BEGINNING = 255;
    private static final int STACK_ENDING = 0;

    @Mock
    private Memory memory;

    @Mock
    private Registers registers;

    @InjectMocks
    private Stack stack;

    @Test
    void testPush8Bits() {
        int decrementedSpAddress = INITIAL_SP_ADDRESS - 1;

        when(registers.getSp()).thenReturn(INITIAL_SP_ADDRESS);

        stack.push8Bits(ONE_BYTE_VALUE);

        verify(memory, only()).write8Bits(INITIAL_SP_ADDRESS, ONE_BYTE_VALUE);
        verify(registers, times(1)).setSp(decrementedSpAddress);
    }

    @Test
    void testPull8Bits() {
        int incrementedSpAddress = INITIAL_SP_ADDRESS + 1;

        when(registers.getSp()).thenReturn(INITIAL_SP_ADDRESS);
        when(memory.read8Bits(incrementedSpAddress)).thenReturn(ONE_BYTE_VALUE);

        int pulledValue = stack.pull8Bits();

        assertEquals(ONE_BYTE_VALUE, pulledValue);

        verify(registers, times(1)).setSp(incrementedSpAddress);
    }

    @Test
    void testPush8BitWhenSpDecrementsAtStackEnding() {
        when(registers.getSp()).thenReturn(STACK_ENDING);

        stack.push8Bits(ONE_BYTE_VALUE);

        verify(memory, only()).write8Bits(STACK_ENDING, ONE_BYTE_VALUE);
        verify(registers, times(1)).setSp(STACK_BEGINNING);
    }

    @Test
    void testPull8BitsWhenSpIncrementsAtStackBeginning() {
        when(registers.getSp()).thenReturn(STACK_BEGINNING);
        when(memory.read8Bits(STACK_ENDING)).thenReturn(ONE_BYTE_VALUE);

        int pulledValue = stack.pull8Bits();

        assertEquals(ONE_BYTE_VALUE, pulledValue);

        verify(registers, times(1)).setSp(STACK_ENDING);
    }
}