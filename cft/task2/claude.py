import numpy as np
import matplotlib.pyplot as plt
from imageio.v2 import imread


class ContinuousImage:
    """Represents a grayscale image as a continuous 2D spatial signal. (Given)"""

    def __init__(self, image_path):
        self.image = imread(image_path, mode='L').astype(float)
        self.image = self.image / np.max(self.image)

        # Continuous spatial coordinate vectors, both spanning [-1, 1]
        self.x = np.linspace(-1, 1, self.image.shape[1])
        self.y = np.linspace(-1, 1, self.image.shape[0])

    def show(self, title="Image"):
        plt.imshow(self.image, cmap='gray')
        plt.title(title)
        plt.axis('off')
        plt.show()


class CFT2D:
    """Computes the 2D Continuous Fourier Transform of a ContinuousImage
    using separable numerical (trapezoidal) integration."""

    def __init__(self, image_obj: ContinuousImage):
        self.I = image_obj.image
        self.x = image_obj.x
        self.y = image_obj.y

        dx = self.x[1] - self.x[0]
        dy = self.y[1] - self.y[0]
        self.u = np.linspace(-1 / (2 * dx), 1 / (2 * dx), self.I.shape[1])
        self.v = np.linspace(-1 / (2 * dy), 1 / (2 * dy), self.I.shape[0])

    def compute_cft(self):
        """
        Re{F(u,v)} =  Integral Integral I(x,y) cos(2*pi*(u*x + v*y)) dx dy
        Im{F(u,v)} = -Integral Integral I(x,y) sin(2*pi*(u*x + v*y)) dx dy

        Expanding cos/sin(2*pi*(ux+vy)) via the angle-sum identities splits
        this into two separable stages:

            A(y,u) = Integral I(x,y) cos(2*pi*u*x) dx
            B(y,u) = Integral I(x,y) sin(2*pi*u*x) dx

            Re{F(u,v)} =  Integral [A(y,u)cos(2*pi*v*y) - B(y,u)sin(2*pi*v*y)] dy
            Im{F(u,v)} = -Integral [B(y,u)cos(2*pi*v*y) + A(y,u)sin(2*pi*v*y)] dy
        """
        Ny = self.y.size
        Nu = self.u.size
        Nv = self.v.size

        # ---- Stage 1: integrate over x, for every (y, u) pair ----
        # Loop over u (length Nu). Each iteration builds one cos/sin wave
        # sampled at every x, multiplies it elementwise against every row
        # of I (every y), then collapses the x-axis with one trapz call.
        # Result: A, B have shape (Ny, Nu) -- one value per (y,u) pair.
        A = np.zeros((Ny, Nu))
        B = np.zeros((Ny, Nu))
        for k in range(Nu):
            u_k = self.u[k]
            cos_ux = np.cos(2 * np.pi * u_k * self.x)   # shape (Nx,)
            sin_ux = np.sin(2 * np.pi * u_k * self.x)   # shape (Nx,)
            A[:, k] = np.trapezoid(self.I * cos_ux, self.x, axis=1)
            B[:, k] = np.trapezoid(self.I * sin_ux, self.x, axis=1)

        # ---- Stage 2: integrate over y, for every (u, v) pair ----
        # Same pattern, now consuming A and B instead of I, and producing
        # the final (Nv, Nu) spectrum -- row index = v, column index = u,
        # matching self.I's (rows=y, cols=x) convention.
        real = np.zeros((Nv, Nu))
        imag = np.zeros((Nv, Nu))
        for l in range(Nv):
            v_l = self.v[l]
            cos_vy = np.cos(2 * np.pi * v_l * self.y)   # shape (Ny,)
            sin_vy = np.sin(2 * np.pi * v_l * self.y)   # shape (Ny,)
            real[l, :] = np.trapezoid(
                A * cos_vy[:, np.newaxis] - B * sin_vy[:, np.newaxis],
                self.y, axis=0
            )
            imag[l, :] = -np.trapezoid(
                B * cos_vy[:, np.newaxis] + A * sin_vy[:, np.newaxis],
                self.y, axis=0
            )

        return real, imag

    def plot_magnitude(self):
        """Plot log(1 + |F(u,v)|) for visual debugging."""
        real, imag = self.compute_cft()
        magnitude = np.sqrt(real ** 2 + imag ** 2)
        plt.imshow(
            np.log(1 + magnitude),
            cmap='gray',
            extent=[self.u.min(), self.u.max(), self.v.min(), self.v.max()],
            origin='lower'
        )
        plt.title("Log-Scaled Magnitude Spectrum")
        plt.xlabel("u")
        plt.ylabel("v")
        plt.colorbar()
        plt.show()


class FrequencyFilter:
    """Applies frequency-domain filtering operations. (Given)"""

    def high_pass(self, real, imag, cutoff):
        rows, cols = real.shape
        cx, cy = rows // 2, cols // 2

        real = real.copy()
        imag = imag.copy()
        for i in range(rows):
            for j in range(cols):
                if np.sqrt((i - cx) ** 2 + (j - cy) ** 2) <= cutoff:
                    real[i, j] = 0
                    imag[i, j] = 0
        return real, imag


class InverseCFT2D:
    """Reconstructs the spatial-domain image from a (filtered) 2D frequency
    spectrum using separable numerical integration."""

    def __init__(self, real, imag, u, v, x, y):
        self.real = real
        self.imag = imag
        self.u = u
        self.v = v
        self.x = x
        self.y = y

    def reconstruct(self):
        """
        I(x,y) = Integral Integral F(u,v) exp(j*2*pi*(u*x + v*y)) du dv

        Expanding exp(j*2*pi*(ux+vy)) via Euler's identity and separating
        real/imag gives two stages (integrate over v first, then u):

            C(y,u) = Integral [real(u,v)cos(2*pi*v*y) - imag(u,v)sin(2*pi*v*y)] dv
            D(y,u) = Integral [real(u,v)sin(2*pi*v*y) + imag(u,v)cos(2*pi*v*y)] dv

            I(x,y) = Integral [cos(2*pi*u*x)*C(y,u) - sin(2*pi*u*x)*D(y,u)] du
        """
        Ny = self.y.size
        Nx = self.x.size
        Nu = self.u.size

        # ---- Stage 1: integrate over v, for every (y, u) pair ----
        # self.real/self.imag have shape (Nv, Nu); looping over y, we
        # broadcast a cos/sin(v) column against every row (every v) and
        # collapse axis=0 (the v axis) with trapz. Result: (Ny, Nu).
        C = np.zeros((Ny, Nu))
        D = np.zeros((Ny, Nu))
        for m in range(Ny):
            y_m = self.y[m]
            cos_vy = np.cos(2 * np.pi * self.v * y_m)   # shape (Nv,)
            sin_vy = np.sin(2 * np.pi * self.v * y_m)   # shape (Nv,)
            C[m, :] = np.trapezoid(
                self.real * cos_vy - self.imag * sin_vy,
                self.v, axis=0
            )
            D[m, :] = np.trapezoid(
                self.real * sin_vy[:, np.newaxis] + self.imag * cos_vy[:, np.newaxis],
                self.v, axis=0
            )

        # ---- Stage 2: integrate over u, for every (x, y) pair ----
        # Loop over x, collapse axis=1 (the u axis) of C, D with trapz.
        # Result: image has shape (Ny, Nx), matching self.I's convention.
        image = np.zeros((Ny, Nx))
        for n in range(Nx):
            x_n = self.x[n]
            cos_ux = np.cos(2 * np.pi * self.u * x_n)   # shape (Nu,)
            sin_ux = np.sin(2 * np.pi * self.u * x_n)   # shape (Nu,)
            image[:, n] = np.trapezoid(
                cos_ux[np.newaxis, :] * C - sin_ux[np.newaxis, :] * D,
                self.u, axis=1
            )

        return image


# =====================================================
# Command-line entry point (given -- do not modify)
# Usage: python3 cft_edge_detector.py <input_image_path> <output_image_path> [cutoff]
# =====================================================
if __name__ == "__main__":
    import sys

    if len(sys.argv) < 3:
        print("Usage: python3 cft_edge_detector.py <input_image_path> <output_image_path> [cutoff]")
        print("Example: python3 cft_edge_detector.py pikachu.png pikachu_edges.png 15")
        sys.exit(1)

    input_path = sys.argv[1]
    output_path = sys.argv[2]
    cutoff = float(sys.argv[3]) if len(sys.argv) > 3 else 15

    img = ContinuousImage(input_path)
    cft2d = CFT2D(img)
    real, imag = cft2d.compute_cft()
    cft2d.plot_magnitude()

    filt = FrequencyFilter()
    real_f, imag_f = filt.high_pass(real, imag, cutoff)

    icft2d = InverseCFT2D(real_f, imag_f, cft2d.u, cft2d.v, img.x, img.y)
    edges = icft2d.reconstruct()

    edge_map = np.abs(edges)
    if edge_map.max() > 0:
        edge_map = edge_map / edge_map.max()
    edge_map = 1 - edge_map  # invert: edges black, background white

    plt.imsave(output_path, edge_map, cmap='gray')
    print(f"Saved edge map to {output_path}")