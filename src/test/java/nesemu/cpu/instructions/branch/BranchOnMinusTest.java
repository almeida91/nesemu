package nesemu.cpu.instructions.branch;

import nesemu.cpu.Registers;
import nesemu.testUtils.RegistersAssertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchOnMinusTest {

    public static final int ADDRESS = 0x72;
    public static final int OP_CODE = 0x00;

    @Mock
    private Registers registers;

    @InjectMocks
    private BranchOnMinus instruction;

    @Test
    public void testRunWithNegativeFlag() {
        when(registers.getNegativeFlag()).thenReturn(true);

        instruction.run(OP_CODE, ADDRESS);

        new RegistersAssertion()
                .withPc(ADDRESS)
                .doAssertion(registers);
    }

    @Test
    public void testRunWithNegativeFlagClear() {
        when(registers.getNegativeFlag()).thenReturn(false);

        instruction.run(OP_CODE, ADDRESS);

        new RegistersAssertion()
                .doAssertion(registers);
    }
}