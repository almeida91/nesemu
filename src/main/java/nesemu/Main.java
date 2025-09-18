package nesemu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nesemu.commandLine.LaunchParameters;
import nesemu.commandLine.LaunchParametersParser;
import nesemu.io.IoModule;
import nesemu.cpu.CpuModule;
import nesemu.file.FileModule;
import nesemu.memory.MemoryModule;
import nesemu.ppu.PpuModule;
import nesemu.window.Window;
import nesemu.window.WindowModule;

public class Main {
    public static void main(String[] args) {
        LaunchParametersParser parser = new LaunchParametersParser();
        LaunchParameters parameters = parser.parse(args);


        Injector injector = Guice.createInjector(
                new NesModule(parameters),
                new WindowModule(),
                new FileModule(),
                new CpuModule(),
                new MemoryModule(),
                new PpuModule(),
                new IoModule()
        );

        NES nes = injector.getInstance(NES.class);
        Window window = injector.getInstance(Window.class);
        window.init();

        nes.loop();

        window.destroy();
    }
}
