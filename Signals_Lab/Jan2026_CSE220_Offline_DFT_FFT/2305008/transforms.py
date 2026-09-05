"""
transforms.py  --  YOUR CODE GOES HERE.

The shared transform core used by BOTH tasks. Write it once; bigmul.py
(Task A) and image_conv.py (Task B) import it.

Nothing in this file may call numpy.fft, scipy.fft, numpy.convolve,
scipy.signal, or any other library routine that performs a Fourier
transform, a convolution or a correlation for you. NumPy is for array
arithmetic only.

A quick self-test you should run before touching either application:

    import numpy as np
    from transforms import DFTAnalyzer, FFTTransformer
    x = np.random.randn(64) + 1j * np.random.randn(64)
    d, f = DFTAnalyzer(), FFTTransformer()
    assert np.max(np.abs(d.transform(x) - f.transform(x))) < 1e-9
    assert np.max(np.abs(d.inverse(d.transform(x)) - x)) < 1e-9
"""

import numpy as np


def next_power_of_two(n):
    """
    Return the smallest power of two that is >= ``n`` (and at least 1).

    Both tasks need this to choose a transform length for the radix-2 FFT.
    """
    # TODO: implement this function
    if(n <= 1):
        return 1
    return 1 << (n - 1).bit_length()


class DFTAnalyzer:
    """
    The Discrete Fourier Transform, computed straight from its definition.

        Analysis:   X[k] = sum_{n=0}^{N-1} x[n] * exp(-2j*pi*k*n/N)
        Synthesis:  x[n] = (1/N) * sum_{k=0}^{N-1} X[k] * exp(+2j*pi*k*n/N)

    How you write it is up to you -- a literal double loop, a precomputed
    table of twiddle factors indexed by (k*n) % N, or a NumPy expression --
    as long as it computes these sums directly and is not secretly an FFT.
    """

    name = "dft"

    def transform(self, x):
        """
        Forward DFT.

        Parameters
        ----------
        x : 1D array_like, length N (real or complex)

        Returns
        -------
        numpy.ndarray of complex128, shape (N,)
        """
        # TODO: implement this method
        sig = np.asarray(x, dtype = np.complex128)
        N = len(sig)
        if(N == 0):
            return np.array([], dtype = np.complex128)
        n = np.arange(N) # Generates a 1D NumPy array with integers from 0 to N-1. its a row vector.
        k = n.reshape((N, 1)) # Column vector. 0 to N-1. reshape requires same number of elements.
        # k*n is a matrix of size N x N where each element is the product of the corresponding elements in k and n.
        W = np.exp(-1j * 2 * np.pi / N * k * n) # Twiddle factor matrix N x N
        # Maxtix - vector multiplication 
        # (N x N) * (N x 1) = (N x 1)
        return np.dot(W, sig) 

    def inverse(self, spectrum):
        """
        Inverse DFT, including the 1/N factor.

        Parameters
        ----------
        spectrum : 1D array_like, length N (complex)

        Returns
        -------
        numpy.ndarray of complex128, shape (N,)
            Do NOT discard the imaginary part here -- the caller decides when
            it is safe to take .real.
        """
        # TODO: implement this method
        dft = np.asarray(spectrum, dtype = np.complex128)
        N = len(dft)
        if(N == 0):
            return np.array([], dtype = np.complex128)
        n = np.arange(N)
        k = n.reshape((N, 1))
        W = np.exp(1j * 2 * np.pi / N * k * n)
        return np.dot(W, dft) / N


class FFTTransformer(DFTAnalyzer):
    """
    Radix-2 decimation-in-time (Cooley-Tukey) FFT, in O(N log N).

    It inherits from DFTAnalyzer so that both applications can treat the two
    interchangeably: they call ``engine.transform(...)`` and
    ``engine.inverse(...)`` without caring which engine they hold.

    Requirements:
      * Recursive or iterative (with bit-reversal permutation) -- your choice.
      * N must be a power of two; raise ValueError for any other length.
        The caller is responsible for zero-padding up to next_power_of_two.
      * The inverse must reuse the same butterfly machinery (conjugated
        twiddles, or conjugate-transform-conjugate), not a second copy of it.
      * Twiddle factors for a stage are computed once per stage, never once
        per butterfly.
    """

    name = "fft"

    def _fft_radix2(self, x):
        N = len(x)
        bits = N.bit_length() - 1
        indices = np.arange(N, dtype=np.intp)
        reversed_indices = np.zeros(N, dtype=np.intp)
        for index in range(N):
            reversed_index = 0
            value = index
            for _ in range(bits):
                reversed_index = (reversed_index << 1) | (value & 1)
                value >>= 1
            reversed_indices[index] = reversed_index

        values = x[reversed_indices].astype(np.complex128)
        stage_size = 2
        while stage_size <= N:
            half_size = stage_size // 2
            stage_twiddle = np.exp(-2j * np.pi / stage_size)

            for block_start in range(0, N, stage_size):
                W = 1.0 + 0.0j
                for k in range(half_size):
                    upper_index = block_start + k
                    lower_index = upper_index + half_size
                    g = values[upper_index]
                    h = W * values[lower_index]
                    values[upper_index] = g + h
                    values[lower_index] = g - h
                    W *= stage_twiddle

            stage_size *= 2

        return values

    def transform(self, x):
        """Forward FFT. Same contract as DFTAnalyzer.transform."""
        sig = np.asarray(x, dtype=np.complex128)
        N = len(sig)
        if N == 0 or (N & (N - 1)) != 0:
            raise ValueError(f"FFT input length must be a power of two, got {N}")
        return self._fft_radix2(sig)


    def inverse(self, spectrum):
        """Inverse FFT, including the 1/N factor."""
        fft = np.asarray(spectrum, dtype=np.complex128)
        N = len(fft)
        if N == 0 or (N & (N - 1)) != 0:
            raise ValueError(f"FFT input length must be a power of two, got {N}")
        return np.conj(self.transform(np.conj(fft))) / N



# ---------------------------------------------------------------------------
# BONUS (optional) -- arbitrary-length FFT.
#
# Delete this class if you are not attempting the bonus. If you do attempt it,
# run both tasks with --engine arbitrary and leave those output directories in
# your submission as the evidence.
# ---------------------------------------------------------------------------
class ArbitraryLengthFFT(FFTTransformer):
    """
    Bonus: an O(N log N) transform for ANY length N, not just powers of two.

    Bluestein's chirp-z algorithm is the usual route: rewrite the DFT as a
    convolution of two chirp sequences, and evaluate that convolution with a
    radix-2 FFT of length >= 2N-1. A mixed-radix Cooley-Tukey that factorises
    N is equally acceptable.

    With this engine, Task A no longer has to pad the digit arrays up to a
    power of two, and Task B no longer has to pad the image up to one.
    """

    name = "arbitrary"

    def transform(self, x):
        # TODO (bonus): implement this method
        raise NotImplementedError("Bonus: implement ArbitraryLengthFFT.transform")

    def inverse(self, spectrum):
        # TODO (bonus): implement this method
        raise NotImplementedError("Bonus: implement ArbitraryLengthFFT.inverse")
