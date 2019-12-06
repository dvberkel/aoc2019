#! /usr/bin/env ruby

def has_double(digits)
    for index in (0..digits.length-2) do
        if digits[index] == digits[index + 1] then
           return true
        end
    end
    false
end

def decreases(digits)
    for index in (0..digits.length-2) do
        if digits[index] > digits[index + 1] then
           return true
        end
    end
    false
end

count = 0
for n in (359282..820401) do
    digits = n.digits.reverse()
    if !has_double(digits) then next end
    if decreases(digits) then next end
    count += 1
end

puts count;