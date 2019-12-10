package nl.fifthpostulate.advent;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class ThermalEnvironmentSupervisionTerminalTest {
    @Test
    public void usingPositionModeConsiderWhetherTheInputIsEqualTo8() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "0".charAt(0));
    }

    @Test
    public void usingPositionModeConsiderWhetherTheInputIsLessThan8() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "1".charAt(0));
    }

    @Test
    public void usingImmediateModeConsiderWhetherTheInputIsEqualTo8() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 3, 1108, -1, 8, 3, 4, 3, 99 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "0".charAt(0));
    }

    @Test
    public void usingImmediateModeConsiderWhetherTheInputIsLessThan8() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 3, 1107, -1, 8, 3, 4, 3, 99 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "1".charAt(0));
    }

    @Test
    public void usingPositionModeJumpTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "1".charAt(0));
    }

    @Test
    public void usingImmediateModeJumpTest() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IntProgram program = new IntProgram(out, new int[] { 3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9 });

        program.run();

        byte[] output = out.toByteArray();
        assertEquals(output[0], (byte) "1".charAt(0));
    }
}
