package nesemu.memory;

import com.google.inject.AbstractModule;
import nesemu.memory.mappers.NROM;

public class MemoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Memory.class).asEagerSingleton();

        bind(Mapper.class).to(NROM.class).asEagerSingleton();
    }
}
