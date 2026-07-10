S = int(input())
M = int(input())

sales = []


"""
    alt:
    vals = [int(x) for x in input().split()] 
    sales.append(vals)
"""
for _ in range(S):
    sales.append(list(map(int, input().split())))

target = list(map(int, input().split()))

K = int(input())




# Percentage matrix
P = []

for i in range(S):
    row = []
    for j in range(M):
        row.append(100 * sales[i][j] / target[j])
    P.append(row)


print("Percentage Matrix")
for row in P:
    for x in row:
        print(f"{x:.2f}", end=" ")
    print()




# Salesperson Summary
print("Salesperson Summary")

avg_salesperson = []

for i in range(S):
    avg = sum(P[i]) / M
    avg_salesperson.append((avg, i))

    """
        !!!
    """
    best_product = P[i].index(max(P[i]))

    print(f"Salesperson {i} : Average = {avg:.2f} Best Product = {best_product}")

print()



# Product Summary
print("Product Summary")
for j in range(M):
    total = 0
    best_person = 0
    for i in range(S):
        total += P[i][j]
        if P[i][j] > P[best_person][j]:
            best_person = i
    avg = total / S
    print(f"Product {j} : Average = {avg:.2f} Top Salesperson = {best_person}")
print()



# Top K Salespersons
"""
    !!!
"""
avg_salesperson.sort(key=lambda x: (-x[0], x[1]))

print(f"Top {K} Salespersons")
for i in range(K):
    print(avg_salesperson[i][1])
print()



