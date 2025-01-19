package nesemu.ppu;

import com.google.inject.AbstractModule;
import nesemu.ppu.registers.ControlRegister;
import nesemu.ppu.registers.StatusRegister;

public class PpuModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        bind(Ppu.class).asEagerSingleton();
        bind(StatusRegister.class).asEagerSingleton();
        bind(ControlRegister.class).asEagerSingleton();
    }
}
