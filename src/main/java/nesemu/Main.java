package nesemu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nesemu.commandLine.LaunchParameters;
import nesemu.commandLine.LaunchParametersParser;
import nesemu.cpu.Cpu;
import nesemu.cpu.CpuModule;
import nesemu.file.FileModule;
import nesemu.memory.MemoryModule;
import nesemu.ppu.PpuModule;

public class Main {
    public static void main(String[] args) {
        LaunchParametersParser parser = new LaunchParametersParser();
        LaunchParameters parameters = parser.parse(args);



        Injector injector = Guice.createInjector(
            new NesModule(parameters),
            new FileModule(),
            new CpuModule(),
            new MemoryModule(),
            new PpuModule()
        );

        NES nes = injector.getInstance(NES.class);
        nes.loop();
    }
}
