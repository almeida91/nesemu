package nesemu.debug;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import nesemu.cpu.InstructionCall;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor(onConstructor_ = @Inject)
public class PrintDebugger implements Debugger {

    private Memory memory;

    private Registers registers;

    @Override
    public void callInstruction(InstructionCall instructionCall, int address) {
        String operand = getOperand(instructionCall, address);
        String operationBytes = getOperationBytes(instructionCall, operand);
        operationBytes = StringUtils.rightPad(operationBytes, 8, ' ');

        System.out.printf("%04X\t%s\t%s %s\n",
                registers.getPc() - 1,
                operationBytes,
                instructionCall.getMnemonic(),
                operand);
    }

    private String getOperand(InstructionCall instructionCall, int address) {
        return switch (instructionCall.getAddressingMode()) {
            case ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y, INDIRECT, RELATIVE -> String.format("$%04X", address);
            case IMMEDIATE -> String.format("#$%02X", memory.read8Bits(address));
            case ZERO_PAGE, ZERO_PAGE_X, ZERO_PAGE_Y -> String.format("$%02X", memory.read8Bits(address));
            default -> "";
        };
    }

    private String getOperationBytes(InstructionCall instructionCall, String operand) {
        return "%02X %s".formatted(instructionCall.getOpCode(), getOperandBytes(operand));
    }

    private String getOperandBytes(String operand) {
        if (operand.startsWith("#$")) {
            return operand.substring(2);
        }
        if (operand.startsWith("$")) {
            if (operand.length() == 3) {
                return operand.substring(1);
            }
            if (operand.length() == 5) {
                return operand.substring( 3) + " " + operand.substring(1, 3);
            }
        }

        return "";
    }
}
