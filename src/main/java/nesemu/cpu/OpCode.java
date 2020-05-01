package nesemu.cpu;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(OpCodes.class)
public @interface OpCode {

    int code();

    AddressingMode mode() default AddressingMode.IMPLIED;

    int cycles() default 2;

    boolean crossBoundaryPenalty() default false;
}