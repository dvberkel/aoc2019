package nl.fifthpostulate.advent;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class IntProgramTest {
    @Test
    public void shouldStepCorrectOnExampleProgram() {
        IntProgram computer = new IntProgram(1,9,10,3,2,3,11,0,99,30,40,50);

        computer.step();

        assertThat(computer.dump(), is(new int[] {1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50}));
    }

    @Test
    public void shouldRunCorrectOnExampleProgram() {
        IntProgram computer = new IntProgram(1,9,10,3,2,3,11,0,99,30,40,50);

        computer.run();

        assertThat(computer.dump(), is(new int[] {3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50}));
    }
}
