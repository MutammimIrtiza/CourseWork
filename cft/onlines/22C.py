import numpy as np
import matplotlib.pyplot as plt

class Signal:
    """Class to define standard continuous-time signals and operations."""

    @staticmethod
    def square_pulse(t):
        """Standard rectangular unit pulse: 1 for |t| <= 0.5, 0 elsewhere."""
        return np.where(np.abs(t) <= 0.5, 1.0, 0.0)

    @staticmethod
    def triangle_pulse(t):
        """Standard triangular unit pulse: 1 - |t| for |t| <= 1.0, 0 elsewhere."""
        return np.maximum(0.0, 1.0 - np.abs(t))

    @classmethod
    def x(cls, t):
        """Original signal x(t) = Square(t) + Triangle(t)."""
        return cls.square_pulse(t) + cls.triangle_pulse(t)

    @classmethod
    def y(cls, t, a=10, f0=10):
        """
        Modified signal y(t) with:
          - Time compression by factor 'a'
          - Phase shift / modulation by 2 * pi * f0 * t
        y(t) = x(a * t) * exp(j * 2 * pi * f0 * t)
        """
        return cls.x(a * t) * np.exp(1j * 2 * np.pi * f0 * t)


class ContinuousFourierTransform:
    """Class to compute Continuous Fourier Transform numerically via integration."""

    def __init__(self, time_axis):
        self.t = time_axis

    def transform(self, signal_func, freq_axis):
        """
        Computes CFT numerically using trapezoidal integration:
        X(f) = integral( x(t) * exp(-j * 2 * pi * f * t) dt )
        """
        # Create 2D mesh grid for vectorized trapezoidal integration across frequencies
        t_mesh, f_mesh = np.meshgrid(self.t, freq_axis)
        x_vals = signal_func(self.t)
        
        # Integrand: x(t) * exp(-j * 2 * pi * f * t)
        integrand = x_vals * np.exp(-1j * 2 * np.pi * f_mesh * t_mesh)

        # Handle trapezoidal integration across NumPy versions
        if hasattr(np, 'trapezoid'):
            return np.trapezoid(integrand, self.t, axis=1)
        else:
            return np.trapz(integrand, self.t, axis=1)


class CFTAnalyzer:
    """Class to perform numerical verification, error calculation, and plotting."""

    def __init__(self, t_min=-5, t_max=5, t_samples=2000, 
                 f_min=-10, f_max=10, f_samples=1000):
        self.t = np.linspace(t_min, t_max, t_samples)
        self.f = np.linspace(f_min, f_max, f_samples)
        self.cft_engine = ContinuousFourierTransform(self.t)

    def run_analysis(self, a=10, f0=10):
        # 1. Direct CFT of y(t)
        Y_f = self.cft_engine.transform(lambda t: Signal.y(t, a=a, f0=f0), self.f)

        # 2. Predicted CFT via property: (1 / |a|) * X((f - f0) / a)
        f_scaled_shifted = (self.f - f0) / a
        X_property_f = (1.0 / np.abs(a)) * self.cft_engine.transform(Signal.x, f_scaled_shifted)

        # 3. Magnitudes and Phases
        mag_Y = np.abs(Y_f)
        mag_X_prop = np.abs(X_property_f)

        phase_Y = np.angle(Y_f)
        phase_X_prop = np.angle(X_property_f)

        # 4. Error Analysis (Mean Squared Error)
        N = len(self.f)
        mse_mag = (1.0 / N) * np.sum((mag_Y - mag_X_prop) ** 2)
        mse_phase = (1.0 / N) * np.sum((phase_Y - phase_X_prop) ** 2)

        print("===== ERROR ANALYSIS RESULTS =====")
        print(f"MSE Magnitude : {mse_mag:.8e}")
        print(f"MSE Phase     : {mse_phase:.8e}")
        
        if mse_mag < 1e-4 and mse_phase < 1e-4:
            print("Status        : VERIFICATION SUCCESSFUL (Within acceptable tolerance)")
        else:
            print("Status        : VERIFICATION FAILED")

        # 5. Plotting results
        self.plot_results(self.f, mag_Y, mag_X_prop, phase_Y, phase_X_prop)

    @staticmethod
    def plot_results(f, mag_Y, mag_X_prop, phase_Y, phase_X_prop):
        fig, axes = plt.subplots(2, 1, figsize=(10, 8))

        # Magnitude Comparison Plot
        axes[0].plot(f, mag_Y, 'b-', label=r'$|Y(f)|$', linewidth=2)
        axes[0].plot(f, mag_X_prop, 'r--', label=r'$\frac{1}{|a|}|X(\frac{f-f_0}{a})|$', linewidth=2)
        axes[0].set_title('Magnitude Comparison: Direct |Y(f)| vs Property Prediction')
        axes[0].set_xlabel('Frequency (f)')
        axes[0].set_ylabel('Magnitude')
        axes[0].grid(True, linestyle='--', alpha=0.6)
        axes[0].legend()

        # Phase Comparison Plot
        axes[1].plot(f, phase_Y, 'b-', label=r'$\angle Y(f)$', linewidth=2)
        axes[1].plot(f, phase_X_prop, 'r--', label=r'$\angle X(\frac{f-f_0}{a})$', linewidth=2)
        axes[1].set_title('Phase Comparison: Direct $\\angle Y(f)$ vs Property Prediction')
        axes[1].set_xlabel('Frequency (f)')
        axes[1].set_ylabel('Phase (radians)')
        axes[1].grid(True, linestyle='--', alpha=0.6)
        axes[1].legend()

        plt.tight_layout()
        plt.show()


# Driver Execution Block
if __name__ == "__main__":
    analyzer = CFTAnalyzer(t_samples=2000, f_samples=1000)
    analyzer.run_analysis(a=10, f0=10)