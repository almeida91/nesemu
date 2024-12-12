package nesemu.commandLine;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class LaunchParametersParser {

    public static final String ROM_OPTION = "rom";

    public LaunchParameters parse(String[] args) {
        CommandLine commandLine = getCommandLine(args);

        return LaunchParameters.builder()
            .romPath(commandLine.getOptionValue(ROM_OPTION))
            .build();
    }

    private CommandLine getCommandLine(String[] args) {
        Options options = new Options();

        Option romOption = new Option("r", ROM_OPTION, true, "Path to the ROM file");
        romOption.setRequired(true);
        options.addOption(romOption);

        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
