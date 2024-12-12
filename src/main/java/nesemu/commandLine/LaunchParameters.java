package nesemu.commandLine;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LaunchParameters {
    private String romPath;
}
