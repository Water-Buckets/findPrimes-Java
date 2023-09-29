package PrimesGen;

import java.io.*;
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
public class PrimesGenVec extends PrimesGen {
    /**
     * A List of generated prime numbers.
     */
    List<Integer> primes = new ArrayList<>();

    /**
     * The constructor for the primesGenVec class.
     *
     * @param uL       The upper limit for prime number generation.
     * @param method   The method to be used for prime number generation.
     * @param fileName The name of the file where the prime numbers will be written.
     * @throws IllegalArgumentException If an invalid method is specified.
     * @throws IOException              If there is an error creating the file.
     */
    public PrimesGenVec(int uL, byte method, String fileName) throws IOException {
        super(uL, method, fileName);
    }

    /**
     * The constructor for the primesGen class.
     *
     * @param uL     The upper limit for prime number generation.
     * @param method The method to be used for prime number generation.
     * @param file   The file where the prime numbers will be written.
     * @throws IllegalArgumentException If an invalid method is specified.
     * @throws RuntimeException         If it was unable to create new file.
     */
    public PrimesGenVec(int uL, byte method, File file) {
        super(uL, method, file);
    }

    /**
     * Implements the Trial Division algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and stores them in a list.
     * It works by iteratively checking each number for divisibility by all previously found prime numbers.
     * If a number is not divisible by any of the previously found prime numbers, it is considered prime and added to the list.
     * The algorithm only checks odd numbers as even numbers (except 2) are not prime.
     *
     * <p>Time Complexity: O(n^2), where n is the upper limit of numbers to check for primality.
     * However, the actual time complexity is less than this due to the break condition in the inner loop.
     *
     * <p>Space Complexity: O(n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and divisibility.
     * - Uses a list (primes) to keep track of prime numbers.
     *
     * <p>Limitations:
     * - The upper limit (uL) of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a list of size 'uL'.
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
     * Implements the Sieve of Eratosthenes algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and stores them in a list.
     * It works by iteratively marking the multiples of each prime number, starting from 2, as composite (non-prime).
     * It only checks odd numbers as even numbers (except 2) are not prime.
     *
     * <p>Time Complexity: O(n log(log n)), where n is the upper limit of numbers to check for primality.
     *
     * <p>Space Complexity: O(n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and their multiples.
     * - Uses a boolean array (isPrime) to keep track of prime numbers.
     *
     * <p>Limitations:
     * - The upper limit (uL) of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL' and a list of size 'uL'.
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
     * Implements the Euler's Sieve algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and stores them in a list.
     * It works by first adding each number to the list of primes if it is prime, then for each prime number in the list,
     * it marks the multiples of that prime number as composite (non-prime).
     * It stops marking multiples when the current number is divisible by the prime or when the multiple exceeds the upper limit.
     *
     * <p>Time Complexity: Approximately O(n), where n is the upper limit of numbers to check for primality.
     * The exact time complexity is hard to determine due to the break condition in the inner loop, but it is more efficient than a simple Sieve of Eratosthenes.
     *
     * <p>Space Complexity: O(n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers and their multiples.
     * - Uses a boolean array (isPrime) to keep track of prime numbers.
     *
     * <p>Limitations:
     * - The upper limit (uL) of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL' and a list of size 'uL'.
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
     * Implements the Sieve of Sundaram algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and stores them in a list.
     * The Sieve of Sundaram works by eliminating certain numbers of the form i + j + 2ij where 1 <= i <= j from the list
     * of natural numbers 1, 2, ..., n. The remaining numbers are doubled and incremented by one, giving all
     * the odd prime numbers (and 2) below 2n + 2.
     *
     * <p>Time Complexity: O(n log n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Space Complexity: O(n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers.
     * - Uses a boolean array (isPrime) to keep track of prime numbers.
     *
     * <p>Limitations:
     * - The upper limit (uL) of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold a boolean array of size 'uL' and a list of size 'uL'.
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
     * Implements the Incremental Sieve algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and stores them in a list.
     * It works by iterating over each number up to the upper limit and checking if it is divisible by any of the previously found primes.
     * If it is not divisible by any of the primes, it is a prime number and is added to the list of primes.
     * It also maintains a list of multiples of primes (mp), which is used to skip the multiples of primes when checking for primality.
     *
     * <p>Time Complexity: O(n^1.5), where n is the upper limit of numbers to check for primality. This is because for each number up to n,
     * it checks divisibility by numbers up to sqrt(n).
     *
     * <p>Space Complexity: O(n), where n is the upper limit of numbers to check for primality.
     *
     * <p>Algorithm Characteristics:
     * - Uses the mathematical property of prime numbers.
     * - Uses a list to keep track of prime numbers and their multiples.
     *
     * <p>Limitations:
     * - The upper limit (uL) of numbers to check for primality must fit in an integer data type.
     * - This method requires enough memory to hold two lists of size 'uL'.
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

    /**
     * Writes the generated prime numbers to a BufferedWriter.
     *
     * @param output the BufferedWriter to be output.
     * @throws IOException the io exception
     */
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
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), StandardCharsets.UTF_8));
        output(output);
        output.close();
    }
}