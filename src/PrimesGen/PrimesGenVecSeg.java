package PrimesGen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class extends the primesGenVec class and generates prime numbers in a given range using various methods.
 * The methods include trial division, Eratosthenes sieve, Euler sieve, Sundaram sieve, and incremental sieve.
 * The class also allows for pre-sieved primes to be used in the generation process.
 */
public class PrimesGenVecSeg extends PrimesGenVec {
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
     * @param lL              The lower limit of the range.
     * @param uL              The upper limit of the range.
     * @param preSievedPrimes The list of pre-sieved primes.
     * @param method          The method to be used for prime generation.
     * @param fileName        The file to which the primes are to be written.
     * @throws IOException              If an I/O error occurs.
     * @throws IllegalArgumentException If an invalid method is specified.
     *                                  Or lL is bogger than uL.
     * @throws RuntimeException         If it was unable to create new file.
     */
    public PrimesGenVecSeg(int lL, int uL, List<Integer> preSievedPrimes, byte method, String fileName) throws IOException {
        super(uL, method, fileName);
        if (lL >= uL) {
            throw new IllegalArgumentException("l should be smaller than u.");
        } else this.lL = lL;
        this.preSievedPrimes = preSievedPrimes;
    }

    /**
     * Constructor for the primesGenVecSeg class.
     *
     * @param lL              The lower limit of the range.
     * @param uL              The upper limit of the range.
     * @param preSievedPrimes The list of pre-sieved primes.
     * @param method          The method to be used for prime generation.
     * @param file            The file to which the primes are to be written.
     * @throws IllegalArgumentException If an invalid method is specified.
     *                                  Or lL is bogger than uL.
     * @throws RuntimeException         If it was unable to create new file.
     */
    public PrimesGenVecSeg(int lL, int uL, List<Integer> preSievedPrimes, byte method, File file) {
        super(uL, method, file);
        if (lL >= uL) {
            throw new IllegalArgumentException("l should be smaller than u.");
        } else this.lL = lL;
        this.preSievedPrimes = preSievedPrimes;
    }

    /**
     * Implements the Trial Division algorithm for finding all prime numbers within a given range.
     *
     * <p>This method generates all prime numbers between the given lower limit
     * (lL) and upper limit (uL) and stores them in a list.
     * It works by checking each number in the range for divisibility by previously found primes.
     * If a number is not divisible by
     * any of the primes, it is a prime number and is added to the list of primes.
     *
     * <p>Time Complexity: O(n sqrt(n)),
     * where n is the difference between the upper limit and lower limit of numbers to check for primality,
     * and m is the average value of the numbers in the range.
     *
     * <p>Space Complexity:
     * O(n), where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers.
     * - Uses a list to keep track of prime numbers.
     *
     * <p>Limitations:
     * -
     * The upper limit (uL) and lower limit (lL)
     * of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a list of size 'uL - lL + 1'.
     * - It requires a pre-sieved list of primes up to the square root of the upper limit (uL).
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
     * Implements the segmented version of the Sieve of Eratosthenes algorithm
     * for finding all prime numbers within a given range.
     *
     * <p>This method generates all prime numbers between the given lower limit
     * (lL) and upper limit (uL) and stores them in a list.
     * It works by using a pre-sieved list of primes to mark off multiples within the range [lL, uL].
     * This version of the sieve is useful
     * for generating primes in a specific interval, especially when the upper bound (uL) is large.
     *
     * <p>Time Complexity: O(n log log n),
     * where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Space Complexity:
     * O(n), where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and their multiples.
     * - Uses a boolean array (isPrime) to keep track of prime numbers within the range.
     *
     * <p>Limitations:
     * -
     * The upper limit (uL) and lower limit (lL)
     * of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL - lL + 1'.
     * - It requires a pre-sieved list of primes up to the square root of the upper limit (uL).
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
     * Implements the segmented version of the Euler's Sieve algorithm
     * for finding all prime numbers within a given range.
     *
     * <p>This method generates all prime numbers between the given lower limit
     * (lL) and upper limit (uL) and stores them in a list.
     * It works by using a pre-sieved list of primes to mark off multiples within the range [lL, uL].
     * Unlike the Sieve of Eratosthenes,
     * Euler's Sieve marks multiples of each found prime only,
     * which avoids marking multiples of composite numbers.
     *
     * <p>Time Complexity: O(n log log n),
     * where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Space Complexity:
     * O(n), where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and their multiples.
     * - Uses a boolean array (isPrime) to keep track of prime numbers within the range.
     *
     * <p>Limitations:
     * -
     * The upper limit (uL) and lower limit (lL)
     * of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL - lL + 1'.
     * - It requires a pre-sieved list of primes up to the square root of the upper limit (uL).
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
     * Implements the Sieve of Sundaram algorithm for finding all prime numbers within a given range.
     *
     * <p>This method generates all prime numbers between the given lower limit
     * (lL) and upper limit (uL) and stores them in a list.
     * The Sieve of Sundaram works by eliminating certain numbers of the form i + j + 2ij where 1 <= i <= j from the list
     * of natural numbers 1, 2, ..., n. The remaining numbers are doubled and incremented by one, giving all
     * the odd prime numbers (and 2) below 2n + 2.
     *
     * <p>Time Complexity: O(n log n),
     * where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Space Complexity:
     * O(n), where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers.
     * - Uses a boolean array (isPrime) to keep track of prime numbers.
     *
     * <p>Limitations:
     * -
     * The upper limit (uL) and lower limit (lL)
     * of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL - lL + 1'.
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
     * Implements the Incremental Sieve algorithm for finding all prime numbers within a given range.
     *
     * <p>This method generates all prime numbers between the given lower limit
     * (lL) and upper limit (uL) and stores them in a list.
     * It works by using a pre-sieved list of primes
     * and iteratively checking for multiples of these primes within the range [lL,
     * uL].
     * If a number is not a multiple of any of the pre-sieved primes, it is considered a prime number.
     *
     * <p>Time Complexity: O(n^1.5),
     * where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Space Complexity:
     * O(n), where n is the difference between the upper limit and lower limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and their multiples.
     * - Uses a list (primes) to keep track of prime numbers within the range.
     *
     * <p>Limitations:
     * -
     * The upper limit (uL) and lower limit (lL)
     * of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a list of size 'uL - lL + 1'.
     * - It requires a pre-sieved list of primes up to the square root of the upper limit (uL).
     */
    private void incrementalSieve() {
        primes.addAll(preSievedPrimes);
        List<Integer> mp = new ArrayList<>(primes.size());
        mp.addAll(primes);

        // Initialize the multiples of pre-sieved primes
        for (int k = 0; k < primes.size(); ++k) {
            mp.add(k, ((lL + primes.get(k) - 1) / primes.get(k)) * primes.get(k));
        }

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
