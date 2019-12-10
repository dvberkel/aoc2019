package nl.fifthpostulate.advent;

import java.io.OutputStream;
import java.io.PrintStream;

public class IntProgram {
    private final PrintStream out;
    private final int[] memory;
    private int instructionPointer = 0;


    public IntProgram(int... memory) {
        this(System.out, memory);
    }

    public IntProgram(OutputStream out, int[] memory) {
        this.out = new PrintStream(out, true);
        this.memory = memory;
    }

    public void run() {
        Opcode opcode;
        do {
            opcode = step();
        } while (!Opcode.Stop.equals(opcode));
    }

    public Opcode step() {
        int description = memory[instructionPointer];
        Opcode opcode = Opcode.from(description % 100);
        Parameters parameters = Parameters.from(description / 100);
        opcode.execute(out, instructionPointer, memory, parameters);
        instructionPointer = opcode.increment(instructionPointer, memory, parameters);
        return opcode;
    }

    public int[] dump() {
        return memory;
    }
}

enum Opcode {
    Add {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left + right;
        }
    },
    Multiply {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left * right;
        }
    },
    Stop {
        @Override
        public int increment(int instructionPointer, int[] memory, Parameters parameters) {
            return instructionPointer + 1;
        }
    },
    Input {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            Parameter parameter = Parameter.Immediate;
            int index = parameter.value(instructionPointer + 1, memory);
            memory[index] = 5; // This time, when the TEST diagnostic program runs its input instruction to get the ID of the system to test, provide it 5, the ID for the ship's thermal radiator controller.
        }

        @Override
        public int increment(int instructionPointer, int[] memory, Parameters parameters) { return instructionPointer + 2; }
    },
    Output {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            int value = parameters.first.value(instructionPointer + 1, memory);
            out.format("%d\n", value);
        }

        @Override
        public int increment(int instructionPointer, int[] memory, Parameters parameters) { return instructionPointer + 2; }
    },
    JumpIfTrue {
        @Override
        public int increment(int instructionPointer, int[] memory, Parameters parameters) {
            int value = parameters.first.value(instructionPointer + 1, memory);
            if (value != 0) {
                return parameters.second.value(instructionPointer + 2, memory);
            }
            return instructionPointer + 3;
        }
    },
    JumpIfFalse {
        @Override
        public int increment(int instructionPointer, int[] memory, Parameters parameters) {
            int value = parameters.first.value(instructionPointer + 1, memory);
            if (value == 0) {
                return parameters.second.value(instructionPointer + 2, memory);
            }
            return instructionPointer + 3;
        }
    },
    LessThan {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left < right ? 1: 0;
        }
    },
    Equal {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left == right ? 1: 0;
        }
    },
    Unknown {
        @Override
        public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
            throw new IllegalArgumentException(String.format("unknown opcode '%d' at index %d", memory[instructionPointer], instructionPointer));
        }
    };

    public static Opcode from(int description) {
        switch (description) {
            case 1: return Add;
            case 2: return Multiply;
            case 3: return Input;
            case 4: return Output;
            case 5: return JumpIfTrue;
            case 6: return JumpIfFalse;
            case 7: return LessThan;
            case 8: return Equal;
            case 99: return Stop;
            default: return Unknown;
        }
    }

    public void execute(PrintStream out, int instructionPointer, int[] memory, Parameters parameters) {
        // do nothing
    }

    public int increment(int instructionPointer, int[] memory, Parameters parameters) {
        return instructionPointer + 4;
    }
}

class Parameters {
    public static Parameters from(int description) {
        Parameter first = Parameter.from(description % 10);
        description /= 10;
        Parameter second = Parameter.from(description % 10);
        description /= 10;
        Parameter third = Parameter.from(description % 10);
        return new Parameters(first, second, third);

    }
    public final Parameter first;
    public final Parameter second;
    public final Parameter third;

    private Parameters(Parameter first, Parameter second, Parameter third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}

enum Parameter {
    Position {
        @Override
        public int value(int pointer, int[] memory) {
            int index = memory[pointer];
            return memory[index];
        }
    },
    Immediate {
        @Override
        public int value(int pointer, int[] memory) {
            return memory[pointer];
        }
    },
    Unknown {
        @Override
        public int value(int pointer, int[] memory) {
            throw new IllegalArgumentException(String.format("unknown parameter '%d' at index %d", memory[pointer], pointer));
        }
    };

    public static Parameter from(int code) {
        switch (code) {
            case 0: return Position;
            case 1: return Immediate;
            default: return Unknown;
        }
    }

    public abstract int value(int pointer, int[] memory);
}

