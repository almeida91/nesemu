package nesemu;

import com.google.inject.AbstractModule;
import lombok.AllArgsConstructor;
import nesemu.commandLine.LaunchParameters;
import nesemu.debug.Debugger;
import nesemu.debug.PrintDebugger;

@AllArgsConstructor
public class NesModule extends AbstractModule {

    private LaunchParameters launchParameters;

    @Override
    protected void configure() {
        binder().requireExplicitBindings();

        bind(LaunchParameters.class).toInstance(launchParameters);
        bind(Debugger.class).to(PrintDebugger.class).asEagerSingleton();
        bind(NES.class).asEagerSingleton();
    }
}
