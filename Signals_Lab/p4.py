import numpy as np
import matplotlib.pyplot as plt


# not np array
months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
          'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']


north = np.array([120, 135, 128, 142, 150, 158,
                  162, 170, 168, 180, 185, 195])

south = np.array([110, 118, 125, 130, 138, 145,
                  150, 155, 160, 168, 172, 180])

central = np.array([100, 108, 115, 120, 130, 140,
                    148, 152, 158, 165, 170, 175])

plt.figure(figsize=(14, 10))

# ---------------- Plot 1 ----------------

plt.subplot(2, 2, 1)

plt.plot(months, north, 'b-', label='North') # solid line
plt.plot(months, south, 'g--', label='South') # dashed line
plt.plot(months, central, 'r:', label='Central') # dotted line

plt.title("Monthly Branch Profit (2025)")
plt.xlabel("Month")
plt.ylabel("Profit (in thousand dollars)")
plt.legend()
plt.grid(True)

# ---------------- Plot 2 ----------------

plt.subplot(2, 2, 2)

branches = ['North', 'South', 'Central']
dec_profit = [north[-1], south[-1], central[-1]]

plt.bar(branches, dec_profit,
        color=['blue', 'green', 'red'])

plt.title("December Profit Comparison")
plt.xlabel("Branch")
plt.ylabel("Profit (in thousand dollars)")
plt.grid(axis='y')

# ---------------- Plot 3 ----------------

plt.subplot(2, 2, 3)

plt.scatter(months, north,
            color='blue',
            s=60)

plt.title("North Branch Profit Distribution")
plt.xlabel("Month")
plt.ylabel("Profit (in thousand dollars)")
plt.grid(True)

# ---------------- Plot 4 ----------------

plt.subplot(2, 2, 4)

quarters = ['Q1', 'Q2', 'Q3', 'Q4']

north_q = [
    np.sum(north[0:3]),
    np.sum(north[3:6]),
    np.sum(north[6:9]),
    np.sum(north[9:12])
]

#alternative
# north_q = np.array([
#     np.sum(north[0:3]),
#     np.sum(north[3:6]),
#     np.sum(north[6:9]),
#     np.sum(north[9:12])
# ])

south_q = [
    np.sum(south[0:3]),
    np.sum(south[3:6]),
    np.sum(south[6:9]),
    np.sum(south[9:12])
]

central_q = [
    np.sum(central[0:3]),
    np.sum(central[3:6]),
    np.sum(central[6:9]),
    np.sum(central[9:12])
]

plt.bar(quarters, north_q, label="North")
plt.bar(quarters, south_q, bottom=north_q, label="South")



"""
    cast python list to numpy array
"""
bottom = np.array(north_q) + np.array(south_q)



plt.bar(quarters, central_q, bottom=bottom, label="Central")

plt.title("Quarterly Branch Profit")
plt.xlabel("Quarter")
plt.ylabel("Profit (in thousand dollars)")
plt.legend()
plt.grid(axis='y')

plt.suptitle("Company Branch Performance Analysis (2025)")

plt.tight_layout()

plt.show()