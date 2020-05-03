package nesemu.cpu.instructions.branch;

import nesemu.cpu.Registers;
import nesemu.testUtils.RegistersAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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