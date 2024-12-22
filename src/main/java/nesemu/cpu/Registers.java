package nesemu.cpu;

import com.google.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the 6502 CPU internal registers' state.
 *
 * The Status register P is also represented here, it has the following mapping:
 *
 * Bit 5 is always set to 1.
 *
 * <pre>
 * bits
 * 76543210
 * --------
 * |||||||+ {@link #carryFlag}
 * ||||||+- {@link #zeroFlag}
 * |||||+-- {@link #interruptFlag}
 * ||||+--- {@link #decimalFlag}
 * |||+---- {@link #breakpointFlag}
 * |+------ {@link #overflowFlag}
 * +------- {@link #negativeFlag}
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor(onConstructor_ = @Inject)
@EqualsAndHashCode
public class Registers {

    /**
     * The A register, this is used by the CPU's ALU to do its operations.
     * It is one byte wide.
     */
    private Integer a;

    /**
     * The X register, commonly used in combination with an addressing mode.
     */
    private Integer x;

    /**
     * The Y register, as the same of X, is commonly used in combination with an addressing mode.
     */
    private Integer y;

    /**
     * The S register, used as the stack pointer.
     * The initial value is the memory address that the stack starts (256).
     */
    private Integer sp = 0xFF;

    /**
     * The PC register, used by the CPU as the program counter pointer.
     * It is two-byte wide to be able to address the whole 16-bit address space of the NES.
     * The initial value is where the CPU starts the program by reading from the ROM.
     */
    private Integer pc = 0xFFFC;

    /**
     * The carry flag.
     * It is set in case the executed operation generates a 9th bit.
     */
    private Boolean carryFlag = false;

    /**
     * The zero flag.
     * If a previous operation generated the zero value, this flag is set.
     */
    private Boolean zeroFlag = false;

    /**
     * The interrupt disable flag.
     * If set, all interrupts are inhibited.
     * The CPU also sets it in case of an IRQ interruption.
     */
    private Boolean interruptFlag = false;

    /**
     * The binary-coded decimal flag.
     * In the original 6502 specification, this allows arithmetic operations to work with a base-10 coding.
     */
    private Boolean decimalFlag = false;

    /**
     * The B flag, also known as breakpoint flag.
     * The P register only uses the four lower and the two higher bits.
     * While that means the fifth and the sixth bits are left unset, the CPU when pushes it to the stack sets theses bits with 1.
     */
    private Boolean breakpointFlag = false;

    /**
     * The overflow flag.
     * Used by arithmetic operations if the operated number was greater/lesser than what 8 bits can represent and thus have the wrong sign.
     */
    private Boolean overflowFlag = false;

    /**
     * The negative flag.
     * AS the 6502 uses the higher bit to represent the negative sign, this flag holds the 8th bit value.
     */
    private Boolean negativeFlag = false;

    /**
     * The status register also known as P.
     * All the CPU flags are held here.
     *
     * @return the int represent every bit in the register.
     */
    public int getP() {
        int p = 0;

        p |= carryFlag      ? 0b00000001 : 0;
        p |= zeroFlag       ? 0b00000010 : 0;
        p |= interruptFlag  ? 0b00000100 : 0;
        p |= decimalFlag    ? 0b00001000 : 0;
//        p |= breakpointFlag ? 0b00110000 : 0;
        p |= 0b00100000;
        p |= overflowFlag   ? 0b01000000 : 0;
        p |= negativeFlag   ? 0b10000000 : 0;

        return p;
    }

    /**
     * Sets the status register.
     *
     * @param p the int value to set the P register
     */
    public void setP(int p) {
        carryFlag      = (p & 0b00000001) != 0;
        zeroFlag       = (p & 0b00000010) != 0;
        interruptFlag  = (p & 0b00000100) != 0;
        decimalFlag    = (p & 0b00001000) != 0;
//        breakpointFlag = (p & 0b00110000) != 0;
        overflowFlag   = (p & 0b01000000) != 0;
        negativeFlag   = (p & 0b10000000) != 0;
    }

    public int incrementPc() {
        int pc = this.pc;
        this.pc++;
        this.pc &= 0xFFFF;
        return pc;
    }

    public int incrementPcByTwoAddress() {
        int pc = this.pc;
        this.pc += 2;
        this.pc &= 0xFFFF;
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc & 0xFFFF;
    }

    public void reset() {
        setP(0);

        sp = 0xFF;
        pc = 0xFFFC;

        a = 0;
        x = 0;
        y = 0;
    }
}
