package nesemu.cpu.instructions.flag;

import nesemu.cpu.Registers;
import nesemu.testUtils.RegistersAssertion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SetCarryTest {

    @Mock
    private Registers registers;

    @InjectMocks
    private SetCarry instruction;

    @Test
    public void testRun() {
        instruction.run(0x00, 0x00);

        new RegistersAssertion()
                .withCarryFlag(true)
                .doAssertion(registers);
    }
}