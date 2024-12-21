package nesemu.cpu.instructions.branch;

import nesemu.cpu.Registers;
import nesemu.testUtils.RegistersAssertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchOnCarryClearTest {

    public static final int ADDRESS = 0x23;
    public static final int OP_CODE = 0x00;

    @Mock
    private Registers registers;

    @InjectMocks
    private BranchOnCarryClear instruction;

    @Test
    public void testRunWithCarryFlag() {
        when(registers.getCarryFlag()).thenReturn(true);

        instruction.run(OP_CODE, ADDRESS);

        new RegistersAssertion()
                .doAssertion(registers);
    }

    @Test
    public void testRunWithCarryFlagClear() {
        when(registers.getCarryFlag()).thenReturn(false);

        instruction.run(OP_CODE, ADDRESS);

        new RegistersAssertion()
                .withPc(ADDRESS)
                .doAssertion(registers);
    }
}