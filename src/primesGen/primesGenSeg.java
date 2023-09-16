package primesGen;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * This class extends the primesGen class and generates prime numbers in a given range using segmented sieve algorithms.
 * It supports both the Eratosthenes and Sundaram sieve algorithms.
 */
public class primesGenSeg extends primesGen {
    /**
     * Lower limit of the range
     */
    private final int lL;

    /**
     * List of pre-sieved primes
     */
    private final List<Integer> preSievedPrimes;

    /**
     * Constructor for primesGenSeg class.
     *
     * @param l   Lower limit of the range
     * @param u   Upper limit of the range
     * @param pSP List of pre-sieved primes
     * @param m   Mode of operation
     * @param f   File name to write the output
     * @throws IOException If an I/O error occurs
     */
    public primesGenSeg(int l, int u, List<Integer> pSP, byte m, String f) throws IOException {
        super(u, m, f);
        if (l >= u) {
            throw new IllegalArgumentException("l should be smaller than u.");
        }
        this.lL = l;
        this.preSievedPrimes = pSP;
    }

    /**
     * This method implements the segmented version of the Eratosthenes sieve algorithm.
     * It writes the prime numbers in the given range to the specified file.
     *
     * @throws IOException If an I/O error occurs
     */
    private void eratosthenesSieve() throws IOException {
        boolean[] isPrime = new boolean[uL - lL + 1];
        Arrays.fill(isPrime, true);
        for (var prime : preSievedPrimes) {
            int i = Math.max(prime * prime, (lL + prime - 1) / prime * prime);
            for (int j = i; j <= uL; j += prime) {
                isPrime[j - lL] = false;
            }
        }
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
        for (int i = (lL % 2 != 0 ? lL : lL + 1); i <= uL; i += 2) {
            if (isPrime[i - lL]) {
                output.write(i + " ");
            }
        }
        output.close();
    }

    /**
     * This method implements the segmented version of the Sundaram sieve algorithm.
     * It writes the prime numbers in the given range to the specified file.
     *
     * @throws IOException If an I/O error occurs
     */
    private void sundaramSieve() throws IOException {
        int newLL = (lL + 1) / 2;
        int newUL = (uL - 1) / 2;

        boolean[] isPrime = new boolean[newUL - newLL + 1];
        Arrays.fill(isPrime, true);

        int h = (int) ((Math.sqrt(1 + 2 * newUL) - 1) / 2) + 1;

        for (int i = 1; i <= h; ++i) {
            for (int j = i; j <= 2 * (newUL - i) / (2 * i + 1); ++j) {
                int index = i + j + 2 * i * j;
                if (index >= newLL && index <= newUL) {
                    isPrime[index - newLL] = false;
                }
            }
        }
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
        if (2 >= lL && 2 <= uL) {
            output.write(2 + " ");
        }
        for (int i = 0; i <= newUL - newLL; ++i) {
            if (isPrime[i]) {
                output.write((2 * (i + newLL) + 1) + " ");
            }
        }
        output.close();
    }

    /**
     * Initiates the generation of prime numbers using the specified method.
     *
     * @throws RuntimeException         If there is an error writing to the file.
     * @throws IllegalArgumentException If an invalid method is specified.
     */
    public void run() {
        try {
            switch (method) {
                case 0, 2, 4 -> throw new IllegalArgumentException("Invalid method.");
                case 1 -> eratosthenesSieve();
                case 3 -> sundaramSieve();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Please submit your issue at https://github.com/Water-Buckets/findPrimes-Java/issues");
            throw new RuntimeException(e);
        }
    }
}
