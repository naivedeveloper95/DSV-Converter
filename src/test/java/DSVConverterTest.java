import org.junit.jupiter.api.Test;
import org.satyam.DSVConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DSVConverterTest {

    private static final String INPUT_FILE = "src/main/resources/DSV_input_1.txt";
    private static final String OUTPUT_FILE = "target/JSONL_output.jsonl";

    @Test
    public void testConvert() throws IOException {
        DSVConverter.main(new String[]{INPUT_FILE, OUTPUT_FILE});
        List<Object> expected = List.of("{\"firstName\": \"Wolfgang\",\"middleName\": \"Amadeus\",\"lastName\": \"Mozart\",\"gender\": \"Male", "dateOfBirth\": \"1756-01-27\",\"salary\": \"1000\"}," + "{\"firstName\": \"Albert\",\"lastName\": \"Einstein\",\"gender\": \"Male\",\"dateOfBirth\": \"1955-04-18\",\"salary\": \"2000\"}," + "{\"firstName\":\"Marie Salomea\", \"middleName\":\"Skłodowska |\", \"lastName\":\"Curie\", \"gender\":\"Female\", \"dateOfBirth\":\"1934-07-04\", \"salary\":\"3000\"}"

        );
        List<String> actual = Files.readAllLines(Paths.get(OUTPUT_FILE));
        assertEquals(expected, actual);
    }

    @Test
    public void testMissingInputFile() {
        assertThrows(FileNotFoundException.class, () -> DSVConverter.main(new String[]{"missing.dsv", OUTPUT_FILE}));
    }

    @Test
    public void testMissingOutputFile() {
        assertThrows(FileNotFoundException.class, () -> DSVConverter.main(new String[]{INPUT_FILE, "missing.jsonl"}));
    }

    @Test
    public void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> DSVConverter.main(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> DSVConverter.main(new String[]{INPUT_FILE}));
        assertThrows(IllegalArgumentException.class, () -> DSVConverter.main(new String[]{INPUT_FILE, OUTPUT_FILE, "extra"}));
    }
}
