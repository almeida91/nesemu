package nesemu.file;

import nesemu.memory.Rom;

public interface Loader {

    void load(byte[] data);

    RomFileOptions getRomFileOptions();

    Rom getRom();
}
