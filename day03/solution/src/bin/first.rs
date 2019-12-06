extern crate advent;

use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;
use advent::{Panel, Wire};


fn main() {
    let mut panel = Panel::empty();

    if let Ok(lines) = read_lines("../input.txt") {
        for line in lines {
            if let Ok(line) = line {
                if let Ok(wire) = line.parse::<Wire>() {
                    panel.place(wire);
                }
            }
        }
    }

    let nearest = panel.nearest_intersection();
    println!("{}", nearest.distance());
}

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>> where P: AsRef<Path> {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}