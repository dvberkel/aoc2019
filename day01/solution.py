def fuel_for(weight):
    fuel = weight//3 - 2
    return fuel if fuel > 0 else 0

def accumulated_fuel_for(weight):
    accumulated_fuel = 0
    while weight > 0:
        fuel = fuel_for(weight)
        accumulated_fuel += fuel
        weight = fuel
    return accumulated_fuel

def calculate_fuel(modules):
    total_fuel = 0
    for weight in modules:
        total_fuel += accumulated_fuel_for(weight)

    return total_fuel

f = open("input.txt", "r")

modules = list()
for line in f:
    weight = int(line.strip())
    modules.append(weight)

total_fuel = calculate_fuel(modules)
print(total_fuel)
