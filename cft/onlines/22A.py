import numpy as np
import matplotlib.pyplot as plt

# Compatibility for numpy versions
trapz_fn = getattr(np, 'trapezoid', np.trapz)

# 1. Define time domain parameters and signal
T = 10.0          # Integration bound [-T, T]
dt = 0.001        # Time step size
t = np.arange(-T, T, dt)

# Define frequency domain parameters (in Hz)
f_max = 2.0
df = 0.01
f = np.arange(-f_max, f_max + df, df)

# 2. Define x(t) and its analytical derivatives
x = 0.5 * np.cos(4 * t) + 0.5 * np.sin(6 * t)
y1 = -2.0 * np.sin(4 * t) + 3.0 * np.cos(6 * t)
y2 = -8.0 * np.cos(4 * t) - 18.0 * np.sin(6 * t)
y3 = 32.0 * np.sin(4 * t) - 108.0 * np.cos(6 * t)

derivatives = [y1, y2, y3]

# 3. Compute CFT of x(t) using numerical integration: X(f) = integral(x(t) * exp(-j*2*pi*f*t) dt)
X_f = np.zeros(len(f), dtype=complex)
for i, fi in enumerate(f):
    integrand = x * np.exp(-1j * 2 * np.pi * fi * t)
    X_f[i] = trapz_fn(integrand, t)

# 4. Process all 3 derivatives
fig, axes = plt.subplots(3, 2, figsize=(12, 10))

for k in range(1, 4):
    yk = derivatives[k - 1]
    
    # Direct numerical CFT of y_k(t)
    Yk_direct = np.zeros(len(f), dtype=complex)
    for i, fi in enumerate(f):
        integrand = yk * np.exp(-1j * 2 * np.pi * fi * t)
        Yk_direct[i] = trapz_fn(integrand, t)
    
    # Property predicted CFT: Y_k(f) = (j * 2 * pi * f)^k * X(f)
    Yk_prop = ((1j * 2 * np.pi * f) ** k) * X_f
    
    # --- MSE Analysis ---
    mag_direct = np.abs(Yk_direct)
    mag_prop = np.abs(Yk_prop)
    mse_mag = np.mean((mag_direct - mag_prop) ** 2)
    
    phase_direct = np.angle(Yk_direct)
    phase_prop = np.angle(Yk_prop)
    # Wrap phase difference to [-pi, pi] for stable MSE calculation
    phase_diff = np.angle(np.exp(1j * (phase_direct - phase_prop)))
    mse_phase = np.mean(phase_diff ** 2)
    
    print(f"=== Derivative {k} MSE Analysis ===")
    print(f"Magnitude MSE : {mse_mag:.6e}")
    print(f"Phase MSE     : {mse_phase:.6e}\n")
    
    # --- Magnitude Overlap Plot ---
    axes[k - 1, 0].plot(f, mag_direct, 'b-', label=rf'$|Y_{k}(f)|$ (Direct CFT)')
    axes[k - 1, 0].plot(f, mag_prop, 'r--', label=rf'$|(j2\pi f)^{k} X(f)|$ (Property)')
    axes[k - 1, 0].set_title(f'Derivative {k}: Magnitude Overlap')
    axes[k - 1, 0].set_xlabel('Frequency (Hz)')
    axes[k - 1, 0].set_ylabel('Magnitude')
    axes[k - 1, 0].legend()
    axes[k - 1, 0].grid(True)
    
    # --- Phase Comparison Plot ---
    axes[k - 1, 1].plot(f, phase_direct, 'b-', label=rf'$\angle Y_{k}(f)$ (Direct CFT)')
    axes[k - 1, 1].plot(f, phase_prop, 'r--', label=rf'$\angle [(j2\pi f)^{k} X(f)]$ (Property)')
    axes[k - 1, 1].set_title(f'Derivative {k}: Phase Comparison')
    axes[k - 1, 1].set_xlabel('Frequency (Hz)')
    axes[k - 1, 1].set_ylabel('Phase (rad)')
    axes[k - 1, 1].legend()
    axes[k - 1, 1].grid(True)

plt.tight_layout()
plt.show()