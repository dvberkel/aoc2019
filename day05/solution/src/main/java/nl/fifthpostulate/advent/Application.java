package nl.fifthpostulate.advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) throws IOException {
        String input = readInputFromFile("input.txt");
        int[] source = toProgram(input.trim());

        IntProgram computer = new IntProgram(source);
        computer.run();
    }

    private static String readInputFromFile(String filePath) throws IOException {
        try {
            return new String(Files.readAllBytes(Path.of(filePath)));
        } catch (IOException e) {
            System.out.println(e);
            throw e;
        }
    }

    private static int[] toProgram(String input) {
        String[] literals = input.split(",");
        return Arrays.stream(literals)
                .mapToInt(s -> Integer.valueOf(s))
                .toArray();
    }

}
