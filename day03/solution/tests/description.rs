extern crate advent;

use advent::{Panel, Wire};

#[test]
fn first_example() {
    let mut panel = Panel::empty();

    let wire = "R75,D30,R83,U83,L12,D49,R71,U7,L72".parse::<Wire>().expect("to parse");
    panel.place(&wire);
    let wire = "U62,R66,U55,R34,D71,R55,D58,R83".parse::<Wire>().expect("to parse");
    panel.place(&wire);

    let nearest = panel.nearest_intersection();
    assert_eq!(nearest.distance(), 159);
}

#[test]
fn second_example() {
    let mut panel = Panel::empty();

    let wire = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".parse::<Wire>().expect("to parse");
    panel.place(&wire);
    let wire = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".parse::<Wire>().expect("to parse");
    panel.place(&wire);

    let nearest = panel.nearest_intersection();
    assert_eq!(nearest.distance(), 135);
}