def calculate_fuel(modules):
    fuel = 0
    for weight in modules:
        fuel += weight//3 - 2

    return fuel

f = open("input.txt", "r")

modules = list()
for line in f:
    weight = int(line.strip())
    modules.append(weight)

fuel = calculate_fuel(modules)
print(fuel)
