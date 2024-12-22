package nesemu.integ.cpu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Getter
public class CpuTestCase {

    public String name;

    @JsonProperty("initial")
    public CpuTestState initialState;

    @JsonProperty("final")
    public CpuTestState finalState;

    public List<List<Object>> cycles;

    public int getCyclesAmount() {
        return cycles.size();
    }

    public static List<CpuTestCase> loadFile(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = CpuTestCase.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            return mapper.readValue(inputStream, new TypeReference<List<CpuTestCase>>() {});
        }
    }

    @Override
    public String toString() {
        return "CpuTestCase{" +
                "name='" + name + '\'' +
                '}';
    }
}
