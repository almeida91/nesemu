package nesemu.io;

import com.google.inject.Inject;

public class Input {

    private Controller controller;

    private boolean reading = false;
    private int readIndex = 0;

    @Inject
    public Input(Controller controller) {
        this.controller = controller;
    }

    public void write(int address, int value) {
        if (address == 0x4016) {
            boolean readValue = (value & 1) == 1;

            if (reading && !readValue) {
                // reset the index once 0 is written to 0x4016
                readIndex = 0;
            }

            reading = readValue;
        }
    }

    public int read(int address) {
        // TODO: Second controller support
        if (address == 0x4016) {
            if (readIndex > 7) {
                return 1;
            }

            ControllerKey key = ControllerKey.values()[readIndex++];
            return controller.checkPressed(key) ? 1 << readIndex : 0;
        }

        return 0;
    }
}
