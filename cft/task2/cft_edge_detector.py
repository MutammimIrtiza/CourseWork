import numpy as np
import matplotlib.pyplot as plt
from imageio.v2 import imread


class ContinuousImage:
    """Represents a grayscale image as a continuous 2D spatial signal. (Given)"""

    def __init__(self, image_path):
        self.image = imread(image_path, mode='L').astype(float)
        self.image = self.image / np.max(self.image)

        # Continuous spatial coordinate vectors, both spanning [-1, 1]
        self.x = np.linspace(-1, 1, self.image.shape[1]) # no. of columns
        self.y = np.linspace(-1, 1, self.image.shape[0]) # no. of rows

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

        # Frequency axes conjugate to x and y (given), spanning the full
        # Nyquist range implied by the sample spacing (dx, dy). This is
        # what lets the transform represent fine, edge-scale spatial
        # detail instead of only very coarse (near-DC) variation.
        dx = self.x[1] - self.x[0]
        dy = self.y[1] - self.y[0]
        self.u = np.linspace(-1 / (2 * dx), 1 / (2 * dx), self.I.shape[1])
        self.v = np.linspace(-1 / (2 * dy), 1 / (2 * dy), self.I.shape[0])

    def compute_cft(self):
        """
        Compute the real and imaginary parts of the 2D Continuous Fourier
        Transform of self.I, using SEPARABLE trapezoidal integration:

            Re{F(u,v)} =  Integral Integral I(x,y) cos(2*pi*(u*x + v*y)) dx dy
            Im{F(u,v)} = -Integral Integral I(x,y) sin(2*pi*(u*x + v*y)) dx dy

        Do NOT evaluate this as a direct 4-nested-loop double integral over
        (x, y, u, v) -- that is O(N^4) and will not finish in reasonable
        time. Instead exploit separability: expand cos(2*pi*(ux+vy)) and
        sin(2*pi*(ux+vy)) with the angle-sum identities, first integrate
        over x for every (y, u) pair, then integrate the result over y for
        every (u, v) pair. Each of the two stages is an O(N^3) operation
        (an O(N) numerical integral, repeated over an N x N grid), which is
        what makes this tractable.

        Use self.u and self.v (NOT self.x/self.y) as the frequency axes --
        they were already computed for you in __init__.

        Use np.trapezoid(..., axis=...) for the integration -- no built-in
        FFT/DFT routine (np.fft, scipy.fft, ...) may be used anywhere in
        this method.

        Returns
        -------
        real, imag : two 2D numpy arrays, each of shape self.I.shape
        """
        # TODO: implement this method
        Ny = self.y.size
        Nu = len(self.u)
        Nv = len(self.v)

        # shape (Nu, Nx). Nu rows, Nx columns. 
        # Each row is a cos/sin wave sampled at every x for a fixed u
        cos_2pux = np.cos(2 * np.pi * np.outer(self.u, self.x))  
        sin_2pux = np.sin(2 * np.pi * np.outer(self.u, self.x))  

        # integral over x for every (y, u) pair
        intg_x_cos = np.zeros((Ny, Nu))
        intg_x_sin = np.zeros((Ny, Nu))

        for u_i in range(Nu):
            # self.I has shape (Ny, Nx), cos_2pux[i, :] has shape (Nx,)
            # the multiplication takes place for each row of self.I 
            # essentially we get an array of length Nx for each fixed u(loop) and y
            # now we have a (Ny,Nx) grid from the multiplication 
            # now we integrate 
            # then axis=1 => integrate over x (across columns) 
            # result : an array of length Ny for each fixed u(loop)
            # so intg_x_cos and intg_x_sin will have shape (Ny, Nu) after the loop
            intg_x_cos[:, u_i] = np.trapezoid(self.I * cos_2pux[u_i, :], self.x, axis=1) 
            intg_x_sin[:, u_i] = np.trapezoid(self.I * sin_2pux[u_i, :], self.x, axis=1)

        real = np.zeros((Nv, Nu))
        imag = np.zeros((Nv, Nu))

        cos_vy = np.cos(2 * np.pi * np.outer(self.v, self.y))  
        sin_vy = np.sin(2 * np.pi * np.outer(self.v, self.y))  
        
        for v_i in range(Nv):
            # changes the shape from (Ny,) to (Ny, 1).
            # needed because numpy matches dimensions from right to left
            # wasnt neeeded in the previous stage because we were multiplying (Ny, Nx) with (Nx,) which is compatible
            c = cos_vy[v_i, :, np.newaxis] 
            s = sin_vy[v_i, :, np.newaxis]

            # shape (Ny, Nu) * shape (Ny, 1) => shape (Ny, Nu)
            vals_real = intg_x_cos * c - intg_x_sin * s
            vals_imag = -intg_x_sin * c - intg_x_cos * s

            real[v_i, :] = np.trapezoid(vals_real, self.y, axis=0)
            imag[v_i, :] = np.trapezoid(vals_imag, self.y, axis=0)

        return real, imag

    def plot_magnitude(self):
        """
        Plot the log-scaled magnitude spectrum of the 2D CFT computed by
        compute_cft(), i.e. plt.imshow(np.log(1 + magnitude), ...) where
        magnitude = sqrt(real**2 + imag**2). Purely for your own visual
        debugging -- not called by the command-line entry point below.
        """
        # TODO: implement this method
        real, imag = self.compute_cft()
        magnitude = np.sqrt(real**2 + imag**2)
        plt.imshow(np.log(1+magnitude), cmap='gray')
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
        Perform the inverse 2D Continuous Fourier Transform:

            I(x,y) = Integral Integral F(u,v) exp(j*2*pi*(u*x + v*y)) du dv

        using the same separable-integration strategy as compute_cft():
        expand the complex exponential into cos/sin via Euler's identity,
        integrate over v first (for every (y, u) pair), then integrate
        that result over u (for every (x, y) pair). Use np.trapezoid.

        self.real, self.imag are the (possibly filtered) frequency-domain
        components; self.u, self.v are the frequency axes they were
        computed on; self.x, self.y are the spatial axes to reconstruct
        onto.

        Returns
        -------
        image : 2D numpy array of shape (len(self.y), len(self.x))
            The reconstructed real-valued spatial-domain signal. Note
            that after a high-pass filter this is NOT a valid image on
            its own (it will contain negative values, since the DC/
            low-frequency component that carried the average brightness
            has been removed) -- see the command-line entry point below
            for how it gets turned into a displayable edge map.
        """
        # TODO: implement this method
        Ny = self.y.size
        Nx = self.x.size
        Nu = self.u.size

        intg_v_cos = np.zeros((Ny, Nu))
        intg_v_sin = np.zeros((Ny, Nu))

        for i in range(Ny):
            y_i = self.y[i]
            cos_vy = np.cos(2 * np.pi * self.v * y_i)[:, np.newaxis]  # shape (Nv, 1)
            sin_vy = np.sin(2 * np.pi * self.v * y_i)[:, np.newaxis]  # shape (Nv, 1)
            intg_v_cos[i, :] = np.trapezoid(self.real * cos_vy - self.imag * sin_vy, self.v, axis=0)
            intg_v_sin[i, :] = np.trapezoid(self.real * sin_vy + self.imag * cos_vy, self.v, axis=0)

        image = np.zeros((Ny, Nx))
        for i in range(Nx):
            x_i = self.x[i]
            cos_ux = np.cos(2 * np.pi * self.u * x_i)[np.newaxis, :]  # shape (Nu,)
            sin_ux = np.sin(2 * np.pi * self.u * x_i)[np.newaxis, :]  # shape (Nu,)
            image[:, i] = np.trapezoid(intg_v_cos * cos_ux - intg_v_sin * sin_ux, self.u, axis=1)

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
