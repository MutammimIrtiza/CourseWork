


import numpy as np
import matplotlib.pyplot as plt
from typing import Tuple

INF = 8

def plot(
        signal, 
        title=None, 
        y_range=(-1, 3), 
        figsize = (8, 3),
        x_label='n (Time Index)',
        y_label='x[n]',
        saveTo=None
    ):
    plt.figure(figsize=figsize)
    plt.xticks(np.arange(-INF, INF + 1, 1))
    
    y_range = (y_range[0], max(np.max(signal), y_range[1]) + 1)
    # set y range of 
    plt.ylim(*y_range)
    plt.stem(np.arange(-INF, INF + 1, 1), signal)
    plt.title(title)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    plt.grid(True)
    if saveTo is not None:
        plt.savefig(saveTo)
    # plt.show()

def init_signal():
    return np.zeros(2 * INF + 1)


def time_scale_signal(x : np.ndarray, k : int) -> np.ndarray:
    # Initialize the output signal array with zeros
    y = np.zeros_like(x)
    
    # Generate the time indices from -8 to 8
    n = np.arange(-INF, INF + 1)
    
    # Create a boolean mask where n is perfectly divisible by k
    mask = (n % k == 0)
    
    # Map the valid scaled indices back to the original signal array
    y[mask] = x[(n[mask] // k) + INF]
    
    return y


def time_scale_signal_interpolate(x : np.ndarray, k : int) -> np.ndarray:
    # Initialize the output signal array
    y = np.zeros_like(x)
    
    # Generate the time indices from -8 to 8
    n = np.arange(-INF, INF + 1)
    
    # Mask for exact multiples of k (same behavior as Task 1)
    mask_exact = (n % k == 0)
    y[mask_exact] = x[(n[mask_exact] // k) + INF]
    
    # Mask for intermediate samples (not divisible by k)
    mask_interp = ~mask_exact
    
    # Find the array indices of the two surrounding original samples
    idx_low = (n[mask_interp] // k) + INF
    idx_high = idx_low + 1
    
    # Set intermediate samples to the average of the bounding samples
    y[mask_interp] = (x[idx_low] + x[idx_high]) / 2.0
    
    return y


def main():
    img_root = '.'
    signal = init_signal()
    signal[INF] = 1
    signal[INF+1] = .5
    signal[INF-1] = 2
    signal[INF + 2] = 1
    signal[INF - 2] = .5

    plot(signal, title='Original Signal(x[n])', saveTo=f'{img_root}/x[n].png')
    plot(time_scale_signal(signal, 3), title='x[n/3]', saveTo=f'{img_root}/x[n divided by 3].png')
    plot(time_scale_signal(signal, 1), title='x[n/1]', saveTo=f'{img_root}/x[n divided by 1].png')
    plot(time_scale_signal_interpolate(signal, 3), title='x[n/3] with interpolation', saveTo=f'{img_root}/x[n divided by 3]_with_interpolation.png')
    plot(time_scale_signal_interpolate(signal, 1), title='x[n/1] with interpolation', saveTo=f'{img_root}/x[n divided by 1]_with_interpolation.png')

main()