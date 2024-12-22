package nesemu.integ.cpu;

import com.google.inject.AbstractModule;
import nesemu.integ.common.TestMemory;
import nesemu.memory.Memory;

public class CpuTestsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Memory.class).to(TestMemory.class).asEagerSingleton();
    }
}
