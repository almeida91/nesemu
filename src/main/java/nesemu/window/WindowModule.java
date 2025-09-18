package nesemu.window;

import com.google.inject.AbstractModule;

public class WindowModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(KeyboardHandler.class).asEagerSingleton();
        bind(Window.class).asEagerSingleton();
    }
}
