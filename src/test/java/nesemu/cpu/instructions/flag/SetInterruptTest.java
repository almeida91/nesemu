package nesemu.cpu.instructions.flag;

import nesemu.cpu.Registers;
import nesemu.testUtils.RegistersAssertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SetInterruptTest {

    @Mock
    private Registers registers;

    @InjectMocks
    private SetInterrupt instruction;

    @Test
    public void testRun() {
        instruction.run(0x00, 0x00);

        new RegistersAssertion()
                .withInterruptFlag(true)
                .doAssertion(registers);
    }
}