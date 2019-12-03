f = open("input.txt", "r")

fuel = 0
for line in f:
    weight = int(line.strip())
    fuel += weight//3 - 2

print(fuel)
