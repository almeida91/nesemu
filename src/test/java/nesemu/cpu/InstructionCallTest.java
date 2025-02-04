package nesemu.cpu;

import nesemu.debug.Debugger;
import nesemu.memory.NesMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private NesMemory memory;

    @Mock
    private Instruction instruction;

    @Mock
    private Debugger debugger;

    @Test
    public void runWithAbsoluteMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
                AddressingMode.ABSOLUTE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPcByTwoAddress()).thenReturn(INCREMENT_BY_TWO_ADDRESS_VALUE);
        when(memory.read16Bits(INCREMENT_BY_TWO_ADDRESS_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithAbsoluteXMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
                AddressingMode.IMMEDIATE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, INCREMENT_PC_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithIndirectMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
                AddressingMode.RELATIVE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + INCREMENT_PC_VALUE);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithZeroPageMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
                AddressingMode.ZERO_PAGE, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS);

        assertEquals(CYCLES, cycles);
    }

    @Test
    public void runWithZeroPageXMode() {
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
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
        InstructionCall call = new InstructionCall(registers, memory, instruction, debugger,
                AddressingMode.ZERO_PAGE_Y, MNEMONIC, OP_CODE, CYCLES, false);

        when(registers.incrementPc()).thenReturn(INCREMENT_PC_VALUE);
        when(registers.getY()).thenReturn(Y_VALUE);
        when(memory.read8Bits(INCREMENT_PC_VALUE)).thenReturn(MEMORY_ADDRESS);

        int cycles = call.run();

        verify(instruction, only()).run(OP_CODE, MEMORY_ADDRESS + Y_VALUE);

        assertEquals(CYCLES, cycles);
    }
}