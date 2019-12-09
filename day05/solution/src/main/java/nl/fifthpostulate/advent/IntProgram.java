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
        Opcode opcode = Opcode.from(memory[instructionPointer]);
        opcode.execute(instructionPointer, memory);
        instructionPointer += opcode.increment();
        return opcode;
    }

    public int[] dump() {
        return memory;
    }

    private enum Opcode {
        Add {
            @Override
            public void execute(int instructionPointer, int[] memory) {
                int leftIndex = memory[instructionPointer + 1];
                int left = memory[leftIndex];
                int rightIndex = memory[instructionPointer + 2];
                int right = memory[rightIndex];
                int index = memory[instructionPointer + 3];
                memory[index] = left + right;
            }
        },
        Multiply {
            @Override
            public void execute(int instructionPointer, int[] memory) {
                int leftIndex = memory[instructionPointer + 1];
                int left = memory[leftIndex];
                int rightIndex = memory[instructionPointer + 2];
                int right = memory[rightIndex];
                int index = memory[instructionPointer + 3];
                memory[index] = left * right;
            }
        },
        Stop {
            @Override
            public void execute(int instructionPointer, int[] memory) {
                // do nothing
            }

            @Override
            public int increment() {
                return 1;
            }
        },
        Unknown {
            @Override
            public void execute(int instructionPointer, int[] memory) {
                throw new IllegalArgumentException(String.format("unknown opcode '%d' at index %d", memory[instructionPointer], instructionPointer));
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

        public abstract void execute(int instructionPointer, int[] memory);

        public int increment() {
            return 4;
        }
    }
}

