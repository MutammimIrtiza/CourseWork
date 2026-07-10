import numpy as np
import matplotlib.pyplot as plt


t = np.linspace(-5, 5, 1001)    # continuous

n = np.arange(-10, 11)          # discrete




# sine
x = np.sin(2*np.pi*t)

# cosine
x = np.cos(2*np.pi*t)

# exponential
x = np.exp(-t)

# unit step
x = np.where(t >= 0, 1, 0) # Read it as : if t >= 0, put 1; otherwise put 0.

# ramp
x = np.where(t >= 0, t, 0)

# rectangular pulse
x = np.where(np.abs(t) <= 1, 1, 0)



plt.plot(t, x) # continuout plotting
plt.stem(n, x) # stem plotting



# circularly shifting by some amount
shift = 50        # samples
y = np.roll(x, shift)

# array of zeroes
x = np.zeros(5)
A = np.zeros((3, 4)) # 2D
# ones
x = np.ones(5)

