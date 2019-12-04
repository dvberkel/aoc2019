package nl.fifthpostulate.advent;

public class IntProgram {
    private final int[] program;
    private int programCounter = 0;


    public IntProgram(int... program) {
        this.program = program;
    }

    public void run() {
        Opcode opcode;
        do {
            opcode = step();
        } while (!Opcode.Stop.equals(opcode));
    }

    public Opcode step() {
        Opcode opcode = Opcode.from(program[programCounter]);
        opcode.execute(programCounter, program);
        programCounter += 4;
        return opcode;
    }

    public int[] dump() {
        return program;
    }

    private enum Opcode {
        Add {
            @Override
            public void execute(int programCounter, int[] program) {
                int leftIndex = program[programCounter + 1];
                int left = program[leftIndex];
                int rightIndex = program[programCounter + 2];
                int right = program[rightIndex];
                int index = program[programCounter + 3];
                program[index] = left + right;
            }
        },
        Multiply {
            @Override
            public void execute(int programCounter, int[] program) {
                int leftIndex = program[programCounter + 1];
                int left = program[leftIndex];
                int rightIndex = program[programCounter + 2];
                int right = program[rightIndex];
                int index = program[programCounter + 3];
                program[index] = left * right;
            }
        },
        Stop {
            @Override
            public void execute(int programCounter, int[] program) {
                // do nothing
            }
        },
        Unknown {
            @Override
            public void execute(int programCounter, int[] program) {
                throw new IllegalArgumentException(String.format("unknown opcode '%d' at index %d", program[programCounter], programCounter));
            }
        };

        public static Opcode from(int description) {
            switch (description) {
                case 1: return Add;
                case 2: return Multiply;
                case 99: return Stop;
                default: return Unknown;
            }
        }

        public abstract void execute(int programCounter, int[] program);
    }
}

