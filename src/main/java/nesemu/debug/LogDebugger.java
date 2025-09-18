package nesemu.debug;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nesemu.cpu.AddressingMode;
import nesemu.cpu.InstructionCall;
import nesemu.cpu.Registers;
import nesemu.memory.Memory;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class LogDebugger implements Debugger {

    private Memory memory;

    private Registers registers;

    @Override
    public void callInstruction(InstructionCall instructionCall, int address) {
        String operand = getOperand(instructionCall, address);
        String operationBytes = getOperationBytes(instructionCall, operand);
        operationBytes = StringUtils.rightPad(operationBytes, 8, ' ');
        int pc = getPc(instructionCall);

        String line = "%04X\t%s\t%s %s%s".formatted(pc,
                operationBytes,
                instructionCall.getMnemonic(),
                operand,
                getComment(instructionCall, address));

        log.debug(line);
    }

    private int getPc(InstructionCall instructionCall) {
        if (instructionCall.getAddressingMode() == AddressingMode.IMPLIED) {
            return registers.getPc() - 1;
        }
        return registers.getPc() - 2;
    }

    private String getComment(InstructionCall instructionCall, int address) {
        if (registers.getPc() - 1 == 0xFFFE) {
            return " ; IRQ SUB";
        }
        return StringUtils.EMPTY;
    }

    private String getOperand(InstructionCall instructionCall, int address) {
        return switch (instructionCall.getAddressingMode()) {
            case ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y, INDIRECT, RELATIVE -> "$%04X".formatted(address);
            case IMMEDIATE -> readImmediateMemory(address);
            case ZERO_PAGE, ZERO_PAGE_X, ZERO_PAGE_Y -> "$%02X".formatted(address);
            default -> "";
        };
    }

    private String readImmediateMemory(int address) {
        if (address >= 0x2000 && address < 0x4000) {
            return "PPU $%d".formatted(address);
        }
        return "#$%02X".formatted(memory.read8Bits(address));
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
