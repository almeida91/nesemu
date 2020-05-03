package nesemu.cpu;

import nesemu.memory.Memory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InstructionCallTest {

    public static final int INCREMENT_BY_TWO_ADDRESS_VALUE = 5;
    public static final int CYCLES = 2;
    public static final int OP_CODE = 0x00;
    public static final String MNEMONIC = "";
    public static final int MEMORY_ADDRESS = 15;
    public static final int INDIRECT_MEMORY_ADDRESS = 35;
    public static final int X_VALUE = 101;
    public static final int Y_VALUE = 103;
    public static final int INCREMENT_PC_VALUE = 22;

    @Mock
    private Registers registers;

    @Mock
    private Memory memory;

    @Mock
    private Instruction instruction;

    @Test
    public void runWithAbsoluteMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ABSOLUTE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithAbsoluteXMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ABSOLUTE_X, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(registers.getX()).thenReturn(X_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + X_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithAbsoluteYMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ABSOLUTE_Y, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(registers.getY()).thenReturn(Y_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + Y_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithImmediateMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.IMMEDIATE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, INCREMENT_PC_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithIndirectMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.INDIRECT, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(INDIRECT_MEMORY_ADDRESS);
        when(memory.read16Bits(INDIRECT_MEMORY_ADDRESS)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithIndirectXMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.INDIRECT_X, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(registers.getX()).thenReturn(X_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(INDIRECT_MEMORY_ADDRESS);
        when(memory.read16Bits(INDIRECT_MEMORY_ADDRESS + X_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithIndirectYMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.INDIRECT_Y, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(registers.getY()).thenReturn(Y_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(INDIRECT_MEMORY_ADDRESS);
        when(memory.read16Bits(INDIRECT_MEMORY_ADDRESS + Y_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithRelativeMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.RELATIVE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + INCREMENT_PC_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithZeroPageMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ZERO_PAGE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithZeroPageXMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ZERO_PAGE_X, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(registers.getX()).thenReturn(X_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + X_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithZeroPageYMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction,
                AddressingMode.ZERO_PAGE_Y, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(registers.getY()).thenReturn(Y_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + Y_VALUE);

        assertEquals(CYCLES, cycles);
    }
}