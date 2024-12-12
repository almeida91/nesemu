package nesemu;

import com.google.inject.AbstractModule;
import lombok.AllArgsConstructor;
import nesemu.commandLine.LaunchParameters;

@AllArgsConstructor
public class NesModule extends AbstractModule {

    private LaunchParameters launchParameters;

    @Override
    protected void configure() {
        bind(LaunchParameters.class).toInstance(launchParameters);
    }
}
