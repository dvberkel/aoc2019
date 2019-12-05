use std::collections::HashSet as Set;
use std::iter::repeat;
use std::num::ParseIntError;
use std::str::FromStr;

pub struct Panel {
    wires: Vec<Set<Position>>,
}

impl Panel {
    pub fn empty() -> Self {
        Self { wires: Vec::new() }
    }

    pub fn place(&mut self, wire: &Wire) {
        self.wires.push(wire.segments());
    }

    pub fn nearest_intersection(&self) -> Position {
        let initial: Set<Position> = self.wires[0].iter().cloned().collect();
        let mut entries: Vec<_> = self
            .wires
            .iter()
            .fold(initial, |acc, s| acc.intersection(&s).cloned().collect())
            .iter()
            .cloned()
            .collect();
        entries.sort_by(|l, r| l.distance().cmp(&r.distance()));
        entries[0]
    }
}

#[derive(PartialEq, Eq, Hash, Copy, Clone, Debug)]
pub struct Position(i64, i64);

impl Position {
    pub fn distance(&self) -> u64 {
        (self.0.abs() + self.1.abs()) as u64
    }

    pub fn up(&self) -> Self {
        Self(self.0, self.1 + 1)
    }

    pub fn down(&self) -> Self {
        Self(self.0, self.1 - 1)
    }

    pub fn left(&self) -> Self {
        Self(self.0 - 1, self.1)
    }

    pub fn right(&self) -> Self {
        Self(self.0 + 1, self.1)
    }
}

impl From<(i64, i64)> for Position {
    fn from((x, y): (i64, i64)) -> Self {
        Self(x, y)
    }
}

pub struct Wire {
    segments: Vec<Position>,
}

impl Wire {
    pub fn segments(&self) -> Set<Position> {
        self.segments.iter().cloned().collect()
    }
}

impl FromStr for Wire {
    type Err = ParseWireError;

    fn from_str(input: &str) -> Result<Self, Self::Err> {
        input
            .parse::<Instructions>()
            .map(Wire::from)
            .map_err(ParseWireError::from)
    }
}

#[derive(PartialEq, Debug)]
pub enum ParseWireError {
    CouldNotParseInstructions(ParseInstructionsError),
}

impl From<ParseInstructionsError> for ParseWireError {
    fn from(error: ParseInstructionsError) -> Self {
        ParseWireError::CouldNotParseInstructions(error)
    }
}

impl From<Instructions> for Wire {
    fn from(instructions: Instructions) -> Self {
        Self::from(instructions.instructions)
    }
}

impl From<Vec<Instruction>> for Wire {
    fn from(instructions: Vec<Instruction>) -> Self {
        let directions: Vec<Direction> = instructions
            .iter()
            .flat_map(|instruction| instruction.directions())
            .collect();
        Self::from(directions)
    }
}

impl From<Vec<Direction>> for Wire {
    fn from(directions: Vec<Direction>) -> Self {
        let segments = directions
            .iter()
            .fold(
                (Position::from((0, 0)), Vec::new()),
                |(current, mut partial_segment), direction| {
                    let next = direction.from(&current);
                    partial_segment.push(next);
                    (next, partial_segment)
                },
            )
            .1;
        Self { segments }
    }
}

pub struct Instructions {
    instructions: Vec<Instruction>,
}

impl FromStr for Instructions {
    type Err = ParseInstructionsError;

    fn from_str(input: &str) -> Result<Self, Self::Err> {
        let mut instructions: Vec<Instruction> = Vec::new();
        for part in input.split(',') {
            let instruction = part.parse::<Instruction>()?;
            instructions.push(instruction);
        }
        Ok(Instructions::from(instructions))
    }
}

#[derive(PartialEq, Debug)]
pub enum ParseInstructionsError {
    CouldNotParseInstruction(ParseInstructionError),
}

impl From<ParseInstructionError> for ParseInstructionsError {
    fn from(error: ParseInstructionError) -> Self {
        ParseInstructionsError::CouldNotParseInstruction(error)
    }
}

impl From<Vec<Instruction>> for Instructions {
    fn from(instructions: Vec<Instruction>) -> Self {
        Self { instructions }
    }
}

struct Instruction {
    direction: Direction,
    times: usize,
}

impl Instruction {
    fn new(direction: Direction, times: usize) -> Self {
        Self { direction, times }
    }

    fn directions(&self) -> impl Iterator<Item = Direction> {
        repeat(self.direction).take(self.times)
    }
}

impl FromStr for Instruction {
    type Err = ParseInstructionError;

    fn from_str(input: &str) -> Result<Self, Self::Err> {
        let n = input[1..].parse::<usize>()?;
        if input.starts_with('U') {
            Ok(Instruction::new(Direction::Up, n))
        } else if input.starts_with('D') {
            Ok(Instruction::new(Direction::Down, n))
        } else if input.starts_with('L') {
            Ok(Instruction::new(Direction::Left, n))
        } else if input.starts_with('R') {
            Ok(Instruction::new(Direction::Right, n))
        } else {
            Err(ParseInstructionError::UnknownDirection)
        }
    }
}

#[derive(PartialEq, Debug)]
pub enum ParseInstructionError {
    UnknownDirection,
    CouldNotParseInt(ParseIntError),
}

impl From<ParseIntError> for ParseInstructionError {
    fn from(error: ParseIntError) -> Self {
        ParseInstructionError::CouldNotParseInt(error)
    }
}

#[derive(Clone, Copy)]
enum Direction {
    Up,
    Down,
    Left,
    Right,
}

impl Direction {
    fn from(self, position: &Position) -> Position {
        match self {
            Direction::Up => position.up(),
            Direction::Down => position.down(),
            Direction::Left => position.left(),
            Direction::Right => position.right(),
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn wires_should_be_placed() {
        let mut panel = Panel::empty();

        let wire = "R8,U5,L5,D3".parse::<Wire>().expect("wire to be parsed");
        panel.place(&wire);
        let wire = "U7,R6,D4,L4".parse::<Wire>().expect("wire to be parsed");
        panel.place(&wire);

        let nearest = panel.nearest_intersection();
        assert_eq!(nearest.distance(), 6);
    }
}
