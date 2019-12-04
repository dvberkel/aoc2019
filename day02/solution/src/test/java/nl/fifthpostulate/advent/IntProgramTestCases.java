package nl.fifthpostulate.advent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(Parameterized.class)
public class IntProgramTestCases {
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return List.of(
                new Object[]{new int[]{1, 0, 0, 0, 99}, new int[]{2, 0, 0, 0, 99}},
                new Object[]{new int[]{2, 3, 0, 3, 99}, new int[]{2, 3, 0, 6, 99}},
                new Object[]{new int[]{2, 4, 4, 5, 99, 0}, new int[]{2, 4, 4, 5, 99, 9801}},
                new Object[]{new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99}, new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99}}
        );
    }

    private final int[] input;
    private final int[] expectedOutput;

    public IntProgramTestCases(int[] input, int[] expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void shouldRunCorrectly() {
        IntProgram computer = new IntProgram(input);

        computer.run();

        assertThat(computer.dump(), is(expectedOutput));
    }

}
