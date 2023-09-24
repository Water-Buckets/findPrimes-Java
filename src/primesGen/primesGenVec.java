package primesGen;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * primesGenVec is a class that extends primesGen. It is used to generate prime numbers up to a given upper limit (uL) using specified methods.
 * The generated prime numbers are stored in a List.
 * The class supports five methods for generating prime numbers: Trial Division, the Sieve of Eratosthenes, the Sieve of Sundaram, Euler's Sieve and Incremental Sieve.
 * The method to be used is specified by a byte value (0-4) passed to the constructor.
 * The class also includes a method to retrieve the List of generated prime numbers and a method to write the prime numbers to a file.
 */
public class primesGenVec extends primesGen {
    /**
     * A List of generated prime numbers.
     */
    List<Integer> primes = new ArrayList<>();

    /**
     * The constructor for the primesGenVec class.
     *
     * @param u The upper limit for prime number generation.
     * @param m The method to be used for prime number generation.
     * @param f The name of the file where the prime numbers will be written.
     * @throws IllegalArgumentException If an invalid method is specified.
     * @throws IOException              If there is an error creating the file.
     */
    public primesGenVec(int u, byte m, String f) throws IOException {
        super(u, m, f);
    }

    /**
     * Implements the Trial Division algorithm for generating prime numbers.
     */
    private void trialDivision() {
        if (uL >= 2) {
            primes.add(2);
        }
        for (int i = 3; i <= uL; i += 2) {
            boolean isPrime = false;
            for (var p : primes) {
                if (p * p > i) {
                    break;
                }
                if (i % p == 0) {
                    isPrime = true;
                    break;
                }
            }
            if (!isPrime) {
                primes.add(i);
            }
        }
    }

    /**
     * Implements the Sieve of Eratosthenes for generating prime numbers.
     */
    private void eratosthenesSieve() {
        if (uL >= 2) {
            primes.add(2);
        }
        int k = uL + 1;
        boolean[] isPrime = new boolean[k];
        Arrays.fill(isPrime, true);
        for (int i = 3; i <= uL; i += 2) {
            if (isPrime[i]) {
                primes.add(i);
                for (int j = i * i; j <= uL; j += 2 * i) {
                    isPrime[j] = false;
                }
            }
        }
    }

    /**
     * Implements the Euler's Sieve for generating prime numbers.
     */
    private void eulerSieve() {
        boolean[] isPrime = new boolean[uL + 1];
        Arrays.fill(isPrime, true);
        for (int i = 2; i <= uL; ++i) {
            if (isPrime[i]) {
                primes.add(i);
            }
            for (int j = 0; j < primes.size() && i * primes.get(j) <= uL; ++j) {
                isPrime[i * primes.get(j)] = false;
                if (i % primes.get(j) == 0) {
                    break;
                }
            }
        }
    }

    /**
     * Implements the Sieve of Sundaram for generating prime numbers.
     */
    private void sundaramSieve() {
        int k = (uL - 1) / 2;
        boolean[] isPrime = new boolean[k + 1];
        Arrays.fill(isPrime, true);
        int h = (int) ((Math.sqrt(1 + 2 * k) - 1) / 2) + 1;
        for (int i = 1; i <= h; ++i) {
            int p = 2 * i + 1;
            for (int j = 2 * i + 2 * i * i; j <= k; j += p) {
                isPrime[j] = false;
            }
        }
        if (uL >= 2) {
            primes.add(2);
        }
        for (int i = 1; i < k + 1; ++i) {
            if (isPrime[i]) {
                primes.add(2 * i + 1);
            }
        }
    }

    /**
     * Implements the Incremental Sieve for generating prime numbers.
     */
    private void incrementalSieve() {
        List<Integer> mp = new ArrayList<>();
        for (int i = 2; i < uL + 1; ++i) {
            boolean flag = true;
            int limit = (int) Math.sqrt(i);
            for (int k = 0; k < primes.size(); ++k) {
                if (primes.get(k) > limit) {
                    break;
                }
                while (mp.get(k) < i) {
                    mp.set(k, mp.get(k) + primes.get(k));
                }
                if (mp.get(k) == i) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                primes.add(i);
                mp.add(i * i);
            }
        }
    }

    /**
     * Returns the List of generated prime numbers.
     *
     * @return The List of prime numbers.
     */
    public List<Integer> getPrimes() {
        return primes;
    }

    /**
     * Initiates the generation of prime numbers using the specified method.
     */
    public void run() {
        switch (method) {
            case 0 -> trialDivision();
            case 1 -> eratosthenesSieve();
            case 2 -> eulerSieve();
            case 3 -> sundaramSieve();
            case 4 -> incrementalSieve();
        }
        if (!file.delete()) {
            throw new RuntimeException("Failed to delete file: " + file);
        }
    }

    public void output(BufferedWriter output) throws IOException {
        for (Integer p : primes) {
            output.write(p + " ");
        }
    }

    /**
     * Writes the generated prime numbers to a file.
     *
     * @throws IOException If there is an error writing to the file.
     */
    public void outputToFile() throws IOException {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        output(output);
        output.close();
    }
}