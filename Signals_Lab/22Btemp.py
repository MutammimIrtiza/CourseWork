import numpy as np
import matplotlib.pyplot as plt


def sinusoid(n: np.ndarray, A: float, Omega0: float, phi: float) -> np.ndarray:
    return A * np.cos(Omega0 * n + phi)


def time_shift_sinusoid(n: np.ndarray, A: float, Omega0: float, phi: float, n0: int) -> np.ndarray:
    return sinusoid(n-n0, A, Omega0, phi)


def phase_change_sinusoid(n: np.ndarray, A: float, Omega0: float, phi: float, phi0: float) -> np.ndarray:
    return sinusoid(n, A, Omega0, phi+phi0)


# -----------------------------
# 2) Utility functions
# -----------------------------
def mse(a: np.ndarray, b: np.ndarray) -> float:
    """Mean squared error between two sequences of equal length."""
    return float(np.mean((a - b) ** 2))


def stem_plot(ax, n, x, label):
    """A nicer stem plot for discrete-time sequences."""
    markerline, stemlines, baseline = ax.stem(n, x, label=label)
    baseline.set_visible(False)
    ax.grid(True, alpha=0.3)
    ax.set_xlabel("n")
    ax.set_ylabel("Amplitude")


# -----------------------------
# 3) Main experiment
# -----------------------------
def main():
    # Base sinusoid parameters (you may change these to experiment)
    A = 1.0
    Omega0 = np.pi / 4
    phi = 0.0

    # Index range
    n = np.arange(-20, 21)  # -20, -19, ..., 20

    # Original signal
    x = sinusoid(n, A, Omega0, phi)

    n0 = 3  # integer time shift
    x_time = time_shift_sinusoid(n, A, Omega0, phi, n0)

    # TODO: Compute the phase shift phi0_equiv that makes x_phase match x_time
    phi0_equiv = - Omega0 * n0

    x_phase_equiv = phase_change_sinusoid(n, A, Omega0, phi, phi0_equiv)

    err_A = mse(x_time, x_phase_equiv)
    print("[Part A] MSE between time-shifted and equivalent phase-changed:", err_A)

    # Replace the plotting section in Part A with this:
    fig1, ax1 = plt.subplots(figsize=(9, 4))
    # Original in blue circles
    ax1.stem(n, x, linefmt='b-', markerfmt='bo', label="original x[n]") 
    # Time shift in orange squares
    ax1.stem(n, x_time, linefmt='r--', markerfmt='rs', label=f"time shift n0={n0}") 
    # Phase change in green triangles
    ax1.stem(n, x_phase_equiv, linefmt='g:', markerfmt='g^', label=f"phase change phi0={phi0_equiv:.3f}") 
    ax1.legend()
    ax1.grid(True, alpha=0.3)
    fig1.tight_layout()

    
    phi0 = 1.0  # an arbitrary phase change. eg: 1.0 radian
    x_phase = phase_change_sinusoid(n, A, Omega0, phi, phi0)

    # Search over integer shifts to see if any time shift matches this phase change
    k_min, k_max = -12, 12
    best_k = None
    best_err = None

    for k in range(k_min, k_max + 1):
        x_time_k = time_shift_sinusoid(n, A, Omega0, phi, k)
        e = mse(x_time_k, x_phase)
        if (best_err is None) or (e < best_err):
            best_err = e
            best_k = k

    print(f"[Part B] Best matching integer shift in [{k_min},{k_max}] is k={best_k} with MSE={best_err}")

    x_time_best = time_shift_sinusoid(n, A, Omega0, phi, best_k)

    # Replace the plotting section in Part B with this:
    fig2, ax2 = plt.subplots(figsize=(9, 4))
    
    # Phase change signal plotted with blue circles
    markerline1, stemlines1, baseline1 = ax2.stem(
        n, x_phase, linefmt='b-', markerfmt='bo', label=f"phase change phi0={phi0:.3f}"
    )
    
    # Best integer time shift plotted with orange/red triangles and dashed lines
    markerline2, stemlines2, baseline2 = ax2.stem(
        n, x_time_best, linefmt='r--', markerfmt='r^', label=f"best time shift k={best_k}"
    )
    
    # Clean up the baseline visual clutter
    baseline1.set_visible(False)
    baseline2.set_visible(False)
    
    ax2.grid(True, alpha=0.3)
    ax2.set_xlabel("n")
    ax2.set_ylabel("Amplitude")
    ax2.legend()
    ax2.set_title("Part B: Phase Change vs. Best Integer Time Shift (Mismatched)")
    fig2.tight_layout()

    plt.show()


if __name__ == "__main__":
    main()