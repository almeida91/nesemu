package nesemu.memory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import nesemu.file.RomFileOptions;
import org.reflections.Reflections;

import java.util.Set;

public class MemoryModule extends AbstractModule {

    private Multibinder<Mapper> mapperBinder;

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        mapperBinder = Multibinder.newSetBinder(binder(), Mapper.class);

        bind(Memory.class).to(NesMemory.class).asEagerSingleton();
        bind(Stack.class).asEagerSingleton();

        bindMappers();
    }

    @Provides
    @Inject
    @Singleton
    public Mapper provideMapper(RomFileOptions romFileOptions, Rom rom, Set<Mapper> mappers) {
        int mapperCode = romFileOptions.getMapperCode();

        for (Mapper mapper : mappers) {
            MapperCode mapperCodeAnnotation = mapper.getClass().getAnnotation(MapperCode.class);
            if (mapperCodeAnnotation != null && mapperCodeAnnotation.value() == mapperCode) {
                return mapper;
            }
        }

        throw new IllegalArgumentException(String.format("Mapper %d not implemented", mapperCode));
    }

    private void bindMappers() {
        Reflections reflections = new Reflections("nesemu.memory.mappers");
        reflections.getSubTypesOf(Mapper.class).forEach(aClass -> mapperBinder.addBinding().to(aClass));
    }
}
