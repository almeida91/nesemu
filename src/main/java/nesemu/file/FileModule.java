package nesemu.file;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import nesemu.commandLine.LaunchParameters;
import nesemu.file.loaders.Ines;
import nesemu.memory.Rom;

public class FileModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        bind(FileLoader.class).asEagerSingleton();
    }

    @Provides
    @Inject
    @Singleton
    public Loader provideLoader(LaunchParameters launchParameters, FileLoader fileLoader) {
        String romPath = launchParameters.getRomPath();
        String extension = romPath.substring(romPath.lastIndexOf(".") + 1);

        if (extension.equals("nes")) {
            Ines ines = new Ines();
            ines.load(fileLoader.readFile(romPath));

            return ines;
        } else {
            throw new IllegalArgumentException("File extension not supported");
        }
    }

    @Provides
    @Inject
    @Singleton
    public Rom provideRom(Loader loader) {
        return loader.getRom();
    }

    @Provides
    @Inject
    @Singleton
    public RomFileOptions provideRomFileOptions(Loader loader) {
        return loader.getRomFileOptions();
    }
}
