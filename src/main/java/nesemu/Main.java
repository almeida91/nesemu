package nesemu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nesemu.cpu.Cpu;
import nesemu.cpu.CpuModule;
import nesemu.memory.MemoryModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
            new CpuModule(),
            new MemoryModule()
        );

        Cpu instance = injector.getInstance(Cpu.class);
        System.out.println(instance);
    }
}
