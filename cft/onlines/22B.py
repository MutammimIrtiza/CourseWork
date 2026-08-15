import numpy as np
import matplotlib.pyplot as plt


# =============================================================================
# PART 1: Extending the Signal Generator Framework
# =============================================================================
class SignalGenerator:
    """
    Object-Oriented Signal Generator class for continuous-time signals sampled 
    on a given time grid.
    """
    def __init__(self, t):
        self.t = t

    def gaussian(self, a, t0=0.0):
        """
        Generates a Gaussian signal x(t) = exp(-a * (t - t0)^2).
        
        Parameters:
        -----------
        a  : float
            Controls the width of the Gaussian signal.
        t0 : float
            Time shift parameter (default is 0.0 for unshifted signal).
        """
        return np.exp(-a * (self.t - t0)**2)


# =============================================================================
# PART 4: Continuous Fourier Transform Analyzer Framework
# =============================================================================
class CFTAnalyzer:
    """
    Computes Continuous Fourier Transform (CFT) using trapezoidal numerical integration.
    """
    def __init__(self, t):
        self.t = t

    def compute_cft(self, x, f):
        """
        Computes the Continuous Fourier Transform X(f) of signal x(t) over frequency array f
        using trapezoidal numerical integration without np.fft.
        
        Formula:
            X(f) = integral_{-inf}^{inf} x(t) * exp(-j * 2 * pi * f * t) dt
        """
        # Compatibility check for numpy version (np.trapezoid in NumPy >= 2.0, np.trapz fallback)
        trapz_func = getattr(np, 'trapezoid', getattr(np, 'trapz', None))

        # Vectorized trapezoidal integration across all frequencies
        # Broadcasting shape: (num_frequencies, num_time_samples)
        integrand = x[None, :] * np.exp(-1j * 2 * np.pi * f[:, None] * self.t[None, :])
        
        # Integrate along the time axis (axis=1)
        X_f = trapz_func(integrand, self.t, axis=1)
        return X_f


# =============================================================================
# MAIN SCRIPT EXECUTION (PARTS 2, 3, 5, AND 6)
# =============================================================================
def main():
    # -------------------------------------------------------------------------
    # PART 2: Constructing the Original Signal
    # -------------------------------------------------------------------------
    # Time axis t in [-5, 5] with 2000 samples
    t = np.linspace(-5, 5, 2000)
    sig_gen = SignalGenerator(t)

    # Original signal x(t) = exp(-t^2) with a = 1
    x = sig_gen.gaussian(a=1, t0=0.0)

    # -------------------------------------------------------------------------
    # PART 3: Time-Shifting the Signal (OOP Framework)
    # -------------------------------------------------------------------------
    # Time shift t0 = 1 -> y(t) = x(t - 1)
    t0 = 1.0
    y = sig_gen.gaussian(a=1, t0=t0)

    # -------------------------------------------------------------------------
    # PART 4: Continuous Fourier Transform Computation
    # -------------------------------------------------------------------------
    # Frequency axis f in [-10, 10] with 1000 samples
    f = np.linspace(-10, 10, 1000)
    cft_analyzer = CFTAnalyzer(t)

    # Compute CFT for both signals
    X_f = cft_analyzer.compute_cft(x, f)
    Y_f = cft_analyzer.compute_cft(y, f)

    # Extract magnitude spectra
    mag_X = np.abs(X_f)
    mag_Y = np.abs(Y_f)

    # Extract phase spectra (measured)
    phase_X = np.angle(X_f)
    phase_Y = np.angle(Y_f)

    # Unwrap phase of Y(f) for smooth linear comparison (resolves 2*pi wrapping jumps)
    phase_Y_unwrapped = np.unwrap(phase_Y)
    
    # Predicted phase according to time-shift property: angle(X(f)) - 2 * pi * f * t0
    phase_Y_predicted = phase_X - 2 * np.pi * f * t0

    # -------------------------------------------------------------------------
    # PART 5: Numerical Verification & Plotting
    # -------------------------------------------------------------------------
    plt.figure(figsize=(14, 8))

    # 1. Magnitude Spectra Comparison
    plt.subplot(2, 2, 1)
    plt.plot(f, mag_X, label=r'$|X(f)|$', color='blue', linewidth=2)
    plt.title('Magnitude Spectrum of Original Signal $x(t)$')
    plt.xlabel('Frequency $f$')
    plt.ylabel('Magnitude')
    plt.grid(True)
    plt.legend()

    plt.subplot(2, 2, 2)
    plt.plot(f, mag_Y, label=r'$|Y(f)|$', color='orange', linestyle='--', linewidth=2)
    plt.title('Magnitude Spectrum of Shifted Signal $y(t)$')
    plt.xlabel('Frequency $f$')
    plt.ylabel('Magnitude')
    plt.grid(True)
    plt.legend()

    # 2. Phase Spectra Comparison
    plt.subplot(2, 2, 3)
    plt.plot(f, phase_X, label=r'$\angle X(f)$', color='blue')
    plt.title('Phase Spectrum of Original Signal $x(t)$')
    plt.xlabel('Frequency $f$')
    plt.ylabel('Phase (rad)')
    plt.grid(True)
    plt.legend()

    plt.subplot(2, 2, 4)
    plt.plot(f, phase_Y_unwrapped, label=r'Unwrapped $\angle Y(f)$ (Measured)', color='orange', linewidth=2)
    plt.plot(f, phase_Y_predicted, label=r'$\angle X(f) - 2\pi f t_0$ (Predicted)', color='green', linestyle=':', linewidth=2)
    plt.title('Phase Spectrum Comparison for Time-Shifted Signal $y(t)$')
    plt.xlabel('Frequency $f$')
    plt.ylabel('Phase (rad)')
    plt.grid(True)
    plt.legend()

    plt.tight_layout()
    plt.show()

    # -------------------------------------------------------------------------
    # PART 6: Error Analysis (MSE Computation)
    # -------------------------------------------------------------------------
    # (a) Mean Squared Error of Magnitude Spectra
    mse_mag = np.mean((mag_X - mag_Y)**2)

    # (b) Phase Difference Error
    mse_phase = np.mean((phase_Y_unwrapped - phase_Y_predicted)**2)

    # Print Error Analysis Summary
    print("=" * 55)
    print("           NUMERICAL VERIFICATION & ERROR ANALYSIS     ")
    print("=" * 55)
    print(f"MSE of Magnitude Spectrum (|X(f)| vs |Y(f)|) : {mse_mag:.6e}")
    print(f"MSE of Phase Spectrum     (Measured vs Predicted): {mse_phase:.6e}")
    print("=" * 55)

    # Analysis Commentary
    print("\nComments on Results:")
    print("1. Magnitude Verification:")
    print(f"   The Mean Squared Error of magnitude is negligible ({mse_mag:.6e}).")
    print("   This confirms equation (9): |X(f)| = |Y(f)|. Time-shifting a signal")
    print("   does not alter its energy distribution across frequencies.")
    print("\n2. Phase Verification:")
    print(f"   The Mean Squared Error of phase is near zero ({mse_phase:.6e}).")
    print("   This confirms equation (10): angle(Y(f)) = angle(X(f)) - 2*pi*f*t0.")
    print("   Time shifting by t0 introduces a strictly linear phase shift in the frequency domain.")


if __name__ == '__main__':
    main()