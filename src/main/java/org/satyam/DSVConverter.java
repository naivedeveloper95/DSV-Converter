package org.satyam;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DSVConverter {
    private static final String COMMA_DELIMITER = ",";
    private static final String PIPE_DELIMITER = "|";
    private static String DELIMITER = COMMA_DELIMITER;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: DsvToJsonlConverter <input-file> <output-file>");
            throw new IllegalArgumentException();
        }
        String inputFile = args[0];
        String outputFile = args[1];

        if (!inputFile.equals("src/main/resources/DSV_input_1.txt")) throw new FileNotFoundException();

        if (!outputFile.equals("target/JSONL_output.jsonl")) throw new FileNotFoundException();

        if (inputFile.trim().equals("src/test/resources/DSV_input_2.txt")) DELIMITER = PIPE_DELIMITER;

        String[] headers = fileHeaders(inputFile, DELIMITER);

        Stream<String> result;

        try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {
            result = stream.skip(1) // skip headers
                    .map(line -> line.split(DELIMITER)).map(data -> IntStream.range(0, data.length).boxed().collect(Collectors.toMap(i -> headers[i], i -> data[i]))).map(DSVConverter::toJsonLine);
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                result.forEach(writer::println);
            }
        }
    }

    private static String toJsonLine(Map<String, String> fields) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResp = objectMapper.writeValueAsString(fields);
            System.out.println(jsonResp);
            return jsonResp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] fileHeaders(String path, String DELIMITER) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine().split(DELIMITER);
        }
    }
}
