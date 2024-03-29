package nl.fifthpostulate.advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Application {
    public static void main(String[] args) throws IOException {
        String input = readInputFromFile("input.txt");
        int[] source = toProgram(input.trim());

        outer: for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int[] program = Arrays.copyOf(source, source.length);
                program[1] = noun;
                program[2] = verb;

                IntProgram computer = new IntProgram(program);
                computer.run();

                if (program[0] == 19690720) {
                    System.out.printf("%d", 100 * noun + verb);
                    break outer;
                }
            }
        }
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
