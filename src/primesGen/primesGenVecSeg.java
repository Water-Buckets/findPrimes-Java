package primesGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class extends the primesGenVec class and generates prime numbers in a given range using various methods.
 * The methods include trial division, Eratosthenes sieve, Euler sieve, Sundaram sieve, and incremental sieve.
 * The class also allows for pre-sieved primes to be used in the generation process.
 */
public class primesGenVecSeg extends primesGenVec {
    /**
     * A list of pre-sieved primes.
     */
    private final List<Integer> preSievedPrimes;
    /**
     * The lower limit of the range.
     */
    private int lL;

    /**
     * Constructor for the primesGenVecSeg class.
     *
     * @param l   The lower limit of the range.
     * @param u   The upper limit of the range.
     * @param pSP The list of pre-sieved primes.
     * @param m   The method to be used for prime generation.
     * @param f   The file to which the primes are to be written.
     * @throws IOException If an I/O error occurs.
     */
    public primesGenVecSeg(int l, int u, List<Integer> pSP, byte m, String f) throws IOException {
        super(u, m, f);
        if (l >= u) {
            throw new IllegalArgumentException("l should be smaller than u.");
        }
        this.lL = l;
        this.preSievedPrimes = pSP;
    }

    /**
     * This method generates primes using the trial division method.
     */
    private void trialDivision() {
        if (lL <= 2 && uL >= 2) {
            primes.add(2);
        }
        if (lL % 2 == 0) {
            ++lL;
        }
        for (int i = lL; i <= uL; i += 2) {
            boolean isPrime = true;
            for (int p : preSievedPrimes) {
                if (p * p > i) {
                    break;
                }
                if (i % p == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            }
        }
    }

    /**
     * This method generates primes using the Eratosthenes sieve method.
     */
    private void eratosthenesSieve() {
        boolean[] isPrime = new boolean[uL - lL + 1];
        Arrays.fill(isPrime, true);
        for (var prime : preSievedPrimes) {
            int firstMultiple = Math.max(prime * prime, (lL + prime - 1) / prime * prime);
            for (int j = firstMultiple; j <= uL; j += prime) {
                isPrime[j - lL] = false;
            }
        }
        for (int i = (lL % 2 != 0 ? lL : lL + 1); i <= uL; i += 2) {
            if (isPrime[i - lL]) {
                primes.add(i);
            }
        }
    }

    /**
     * This method generates primes using the Euler sieve method.
     */
    private void eulerSieve() {
        boolean[] isPrime = new boolean[uL - lL + 1];
        Arrays.fill(isPrime, true);
        for (int p : preSievedPrimes) {
            for (int i = Math.max(p * p, (lL + p - 1) / p * p); i <= uL; i += p) {
                isPrime[i - lL] = false;
            }
        }

        if (lL == 1) {
            isPrime[0] = false;
        }
        if (lL <= 2 && uL >= 2) {
            primes.add(2);
        }

        for (int i = lL % 2 == 0 ? lL + 1 : lL; i <= uL; i += 2) {
            if (isPrime[i - lL]) {
                primes.add(i);
            }
        }
    }

    /**
     * This method generates primes using the Sundaram sieve method.
     */
    private void sundaramSieve() {
        final int nNew = (uL - 1) / 2;
        boolean[] isPrime = new boolean[nNew + 1];
        Arrays.fill(isPrime, true);


        for (int i = 1; i <= nNew; ++i) {
            for (int j = i; (i + j + 2 * i * j) <= nNew; ++j) {
                isPrime[i + j + 2 * i * j] = false;
            }
        }
        for (int i = Math.max(lL / 2, 1); i <= nNew; ++i) {
            if (isPrime[i]) {
                primes.add(2 * i + 1);
            }
        }
    }

    /**
     * This method generates primes using the incremental sieve method.
     * Unfixed bugs.
     */
    private void incrementalSieve() {
        primes.addAll(preSievedPrimes);
        List<Integer> mp = new ArrayList<>(primes.size());
        mp.addAll(primes);

        // Initialize the multiples of pre-sieved primes
        for (int k = 0; k < primes.size(); ++k) {
            mp.add(k, ((lL + primes.get(k) - 1) / primes.get(k)) * primes.get(k));
        }
        List<Integer> results = new ArrayList<>(primes.size());

        for (int i = lL; i <= uL; ++i) {
            boolean isPrime = true;
            int limit = (int) Math.sqrt(i);
            for (int k = 0; k < primes.size(); ++k) {
                if (primes.get(k) > limit) {
                    break;
                }
                while (mp.get(k) < i) {
                    mp.set(k, mp.get(k) + primes.get(k));
                }
                if (mp.get(k) == i) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
                mp.add(i * i);
            }
        }
        primes.removeAll(preSievedPrimes);
    }

    /**
     * This method runs the prime generation process using the specified method.
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
}
