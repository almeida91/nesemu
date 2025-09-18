package nesemu;

import com.google.inject.Inject;
import nesemu.cpu.Cpu;
import nesemu.ppu.Ppu;
import nesemu.window.Window;

public class NES {

    private Cpu cpu;
    private Ppu ppu;
    private Window window;

    private int cpuCycleTime;
    private long  lastCpuCycleTime;

    @Inject
    public NES(Cpu cpu, Ppu ppu, Window window) {
        this.cpu = cpu;
        this.ppu = ppu;
        this.window = window;
        this.cpuCycleTime = 559; // nanoseconds
    }

    public void loop() {
        cpu.reset();

        while (!window.shouldClose()) {
            long time = System.nanoTime();
            cycle(time);
        }
    }

    private void cycle(long time) {
        if (time - lastCpuCycleTime >= cpuCycleTime) {
            // TODO: DMA access delays

            cpu.cycle();
            lastCpuCycleTime = time;

            for (int i = 0; i < 3; i++) {
                ppu.cycle();
            }

            if (ppu.readNmi()) {
                cpu.nmi();
            }
        }

        window.cycle();
    }
}
