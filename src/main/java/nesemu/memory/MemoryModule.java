package nesemu.memory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nesemu.file.RomFileOptions;
import nesemu.memory.mappers.NROM;

public class MemoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Memory.class).asEagerSingleton();
        bind(Stack.class).asEagerSingleton();
    }

    @Provides
    @Inject
    @Singleton
    public Mapper provideMapper(RomFileOptions romFileOptions, Rom rom) {
        if (romFileOptions.getMapperCode() == 0) {
            return new NROM(rom);
        }
        throw new IllegalArgumentException("Mapper not implemented");
    }
}
