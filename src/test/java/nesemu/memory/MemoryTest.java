package nesemu.memory;

import nesemu.ppu.Ppu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemoryTest {

    public static final int MIRRORED_RAM_BEGINNING = 2048;
    @Mock
    private Ppu ppu;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private Memory memory;

    @Test
    void testRead8BitsFromRam() {
        int address = 10;
        int value = 15;

        memory.write8Bits(address, value);

        int returnedValue = memory.read8Bits(address);

        assertEquals(value, returnedValue);
    }

    @Test
    void testRead8BitsFromMirroredRam() {
        int address = 10;
        int mirroredAddress = 10 + MIRRORED_RAM_BEGINNING;
        int value = 15;

        memory.write8Bits(address, value);

        int returnedValue = memory.read8Bits(mirroredAddress);

        assertEquals(value, returnedValue);
    }

    @Test
    void testRead8BitsFromPpu() {
        int address = 0x2005;
        int value = 15;

        when(ppu.readMemory(address)).thenReturn(value);

        int returnedValue = memory.read8Bits(address);

        assertEquals(value, returnedValue);
    }

    @Test
    void testRead8BitsFromMirroredPpu() {
        int address = 0x2000;
        int mirroredAddress = 0x2008;
        int value = 15;

        when(ppu.readMemory(address)).thenReturn(value);

        int returnedValue = memory.read8Bits(mirroredAddress);

        assertEquals(value, returnedValue);
    }

    @Test
    void testRead8BitsFromMapper() {
        int address = 0xFF00;
        int value = 15;

        when(mapper.read(address)).thenReturn(value);

        int returnedValue = memory.read8Bits(address);

        assertEquals(value, returnedValue);
    }

    @Test
    void testWrite8BitsToPpu() {
        int address = 0x2005;
        int value = 15;

        memory.write8Bits(address, value);

        verify(ppu, only()).writeMemory(address, value);
    }

    @Test
    void testWrite8BitsToMirroredMirroredPpu() {
        int address = 0x2000;
        int mirroredAddress = 0x2008;
        int value = 15;

        memory.write8Bits(mirroredAddress, value);

        verify(ppu, only()).writeMemory(address, value);
    }

    @Test
    void testWrite8BitsToMapper() {
        int address = 0xFF00;
        int value = 15;

        memory.write8Bits(address, value);

        verify(mapper, only()).write(address, value);
    }

    @Test
    void testRead16Bits() {
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