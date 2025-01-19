package nesemu.memory;

import nesemu.ppu.Ppu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NesMemoryTest {

    public static final int MIRRORED_RAM_BEGINNING = 2048;

    @Mock
    private Ppu ppu;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private NesMemory memory;

    @Test
    public void testRead8BitsFromRam() {
        int address = 10;
        int value = 15;

        memory.write8Bits(address, value);

        int returnedValue = memory.read8Bits(address);

        assertEquals(value, returnedValue);
    }

    @Test
    public void testRead8BitsFromMirroredRam() {
        int address = 10;
        int mirroredAddress = 10 + MIRRORED_RAM_BEGINNING;
        int value = 15;

        memory.write8Bits(address, value);

        int returnedValue = memory.read8Bits(mirroredAddress);

        assertEquals(value, returnedValue);
    }


    @Test
    public void testRead8BitsFromMapper() {
        int address = 0xFF00;
        int value = 15;

        when(mapper.readCpu(address)).thenReturn(value);

        int returnedValue = memory.read8Bits(address);

        assertEquals(value, returnedValue);
    }

    @Test
    public void testWrite8BitsToMapper() {
        int address = 0xFF00;
        int value = 15;

        memory.write8Bits(address, value);

        verify(mapper, only()).writeCpu(address, value);
    }

    @Test
    public void testRead16Bits() {
        int lowerValue = 0xAA;
        int higherValue = 0xFF;
        int fullValue = 0xFFAA;

        int address = 300;

        memory.write8Bits(address, lowerValue);
        memory.write8Bits(address + 1, higherValue);

        int returnedValue = memory.read16Bits(address);

        assertEquals(fullValue, returnedValue);
    }
}