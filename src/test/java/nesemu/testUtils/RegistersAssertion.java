package nesemu.testUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import nesemu.cpu.Registers;

import java.util.function.BiConsumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@With
@AllArgsConstructor
@NoArgsConstructor
public class RegistersAssertion {

    private Integer a;
    private Integer x;
    private Integer y;
    private Integer sp;
    private Integer pc;

    private Boolean carryFlag;
    private Boolean zeroFlag;
    private Boolean interruptFlag;
    private Boolean decimalFlag;
    private Boolean breakpointFlag;
    private Boolean overflowFlag;
    private Boolean negativeFlag;

    public void doAssertion(Registers registers) {
        assertFlag(registers, a, Registers::setA);
        assertFlag(registers, x, Registers::setX);
        assertFlag(registers, y, Registers::setY);
        assertFlag(registers, sp, Registers::setSp);
        assertFlag(registers, pc, Registers::setPc);

        assertFlag(registers, carryFlag, Registers::setCarryFlag);
        assertFlag(registers, zeroFlag, Registers::setZeroFlag);
        assertFlag(registers, interruptFlag, Registers::setInterruptFlag);
        assertFlag(registers, decimalFlag, Registers::setDecimalFlag);
        assertFlag(registers, breakpointFlag, Registers::setBreakpointFlag);
        assertFlag(registers, overflowFlag, Registers::setOverflowFlag);
        assertFlag(registers, negativeFlag, Registers::setNegativeFlag);
    }

    private <T> void assertFlag(Registers registers,
                            T flag,
                            BiConsumer<Registers, T> setter) {
        if (flag != null) {
            setter.accept(verify(registers, times(1)), flag);
        } else {
            setter.accept(verify(registers, times(0)), any());
        }
    }
}
