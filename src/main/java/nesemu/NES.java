package nesemu;

import com.google.inject.Inject;
import nesemu.cpu.Cpu;
import nesemu.ppu.Ppu;

public class NES {

    private Cpu cpu;
    private Ppu ppu;

    private int cpuCycleTime;
    private long  lastCpuCycleTime;

    @Inject
    public NES(Cpu cpu, Ppu ppu) {
        this.cpu = cpu;
        this.ppu = ppu;
        this.cpuCycleTime = 559; // nanoseconds
    }

    public void loop() {
        cpu.reset();

        while (true) {
            long time = System.nanoTime();
            cycle(time);
        }
    }

    private void cycle(long time) {
        if (time - lastCpuCycleTime >= cpuCycleTime) {
            cpu.cycle();
            lastCpuCycleTime = time;

            for (int i = 0; i < 3; i++) {
                ppu.cycle();
            }
        }

    }
}
