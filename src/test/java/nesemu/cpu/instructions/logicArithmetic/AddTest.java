package nesemu.cpu.instructions.logicArithmetic;

import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddTest {

    public static final int ADDRESS = 34;
    public static final int MEMORY_VALUE = 2;
    public static final int REGISTER_VALUE = 3;
    @Mock
    private Registers registers;

    @Mock
    private Memory memory;

    @InjectMocks
    private Add addInstruction;


    @Test
    public void testWithTwoPositiveValues() {
        when(memory.read8Bits(ADDRESS)).thenReturn(MEMORY_VALUE);   
        when(registers.getA()).thenReturn(REGISTER_VALUE);

        addInstruction.run(0, ADDRESS);

        verify(registers).setA(MEMORY_VALUE + REGISTER_VALUE);
        verify(registers).setCarryFlag(false);
        verify(registers).setOverflowFlag(false);
        verify(registers).setNegativeFlag(false);
        verify(registers).setZeroFlag(false);
    }
}