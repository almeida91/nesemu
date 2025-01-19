package nesemu.io;

import com.google.inject.AbstractModule;

public class IoModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        bind(Input.class).asEagerSingleton();
        bind(Controller.class).to(KeyboardController.class).asEagerSingleton();
    }
}
