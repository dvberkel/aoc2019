package nl.fifthpostulate.advent;

public class IntProgram {
    private final int[] memory;
    private int instructionPointer = 0;


    public IntProgram(int... memory) {
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
        opcode.execute(instructionPointer, memory, parameters);
        instructionPointer = opcode.increment(instructionPointer, memory);
        return opcode;
    }

    public int[] dump() {
        return memory;
    }
}

enum Opcode {
    Add {
        @Override
        public void execute(int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left + right;
        }
    },
    Multiply {
        @Override
        public void execute(int instructionPointer, int[] memory, Parameters parameters) {
            int left = parameters.first.value(instructionPointer + 1, memory);
            int right = parameters.second.value(instructionPointer + 2, memory);
            int index = Parameter.Immediate.value(instructionPointer + 3, memory);
            memory[index] = left * right;
        }
    },
    Stop {
        @Override
        public int increment(int instructionPointer, int[] memory) {
            return instructionPointer + 1;
        }
    },
    Input {
        @Override
        public void execute(int instructionPointer, int[] memory, Parameters parameters) {
            Parameter parameter = Parameter.Immediate;
            int index = parameter.value(instructionPointer + 1, memory);
            memory[index] = 1; // After providing 1 to the only input instruction
        }

        @Override
        public int increment(int instructionPointer, int[] memory) { return instructionPointer + 2; }
    },
    Output {
        @Override
        public void execute(int instructionPointer, int[] memory, Parameters parameters) {
            int value = parameters.first.value(instructionPointer + 1, memory);
            System.out.format("%d\n", value);
        }

        @Override
        public int increment(int instructionPointer, int[] memory) { return instructionPointer + 2; }
    },
    Unknown {
        @Override
        public void execute(int instructionPointer, int[] memory, Parameters parameters) {
            throw new IllegalArgumentException(String.format("unknown opcode '%d' at index %d", memory[instructionPointer], instructionPointer));
        }
    };

    public static Opcode from(int description) {
        switch (description) {
            case 1: return Add;
            case 2: return Multiply;
            case 3: return Input;
            case 4: return Output;
            case 99: return Stop;
            default: return Unknown;
        }
    }

    public void execute(int instructionPointer, int[] memory, Parameters parameters) {
        // do nothing
    }

    public int increment(int instructionPointer, int[] memory) {
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

