package PrimesGen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * primesGen is a class used to generate prime numbers up to a given upper limit (uL) using specified methods.
 * The generated prime numbers are written to a file.
 * The class supports two methods for generating prime numbers: the Sieve of Eratosthenes and the Sieve of Sundaram.
 * The method to be used is specified by a byte value (0-4) passed to the constructor.
 * The class also includes a method to retrieve the name of the file to which the prime numbers are written.
 * <p>
 * Note: This class throws IOException if there is an error creating the file to which the prime numbers are written.
 * It also throws IllegalArgumentException if an invalid method is specified.
 */
public class PrimesGen implements Runnable {
    /**
     * The upper limit up to which prime numbers are to be generated.
     */
    protected int uL;

    /**
     * The name of the file to which the prime numbers are written.
     */
    protected String fileName;

    /**
     * A File object representing the file to which the prime numbers are written.
     */
    protected File file;

    /**
     * A byte value specifying the method to be used for generating prime numbers.
     */
    protected byte method;

    /**
     * The constructor for the primesGen class.
     *
     * @param uL       The upper limit for prime number generation.
     * @param method   The method to be used for prime number generation.
     * @param fileName The name of the file where the prime numbers will be written.
     * @throws IOException              If there is an error creating the file.
     * @throws IllegalArgumentException If an invalid method is specified.
     * @throws RuntimeException         If it was unable to create new file.
     */
    public PrimesGen(int uL, byte method, String fileName) throws IOException {
        this.uL = uL;
        if (method >= 0 && method <= 4) {
            this.method = method;
        } else throw new IllegalArgumentException("Invaild method");
        this.fileName = fileName;
        this.file = new File(this.fileName);
        if (!file.exists() && !file.createNewFile()) {
            throw new RuntimeException("Unable to create new file: " + this.fileName);
        }
    }

    /**
     * Instantiates a new Primes gen.
     *
     * @param uL     The upper limit for prime number generation.
     * @param method The method to be used for prime number generation.
     * @param file   A File object representing the file to which the prime numbers are written.
     * @throws IllegalArgumentException If an invalid method is specified.
     * @throws RuntimeException         If it was unable to create new file.
     */
    public PrimesGen(int uL, byte method, File file) {
        this.uL = uL;
        this.file = file;
        if (method >= 0 && method <= 4) {
            this.method = method;
        } else throw new IllegalArgumentException("Invaild method");
        this.fileName = file.getName();
    }

    /**
     * Implements the Sieve of Eratosthenes algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and writes them into a file.
     * It works by iteratively marking the multiples of each prime number, starting from 2, as composite (non-prime).
     * It only checks odd numbers as even numbers (except 2) are not prime.
     * The output is a file with all prime numbers up to the given limit.
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
     * - This method requires enough memory to hold a boolean array of size 'uL'.
     * - The algorithm writes the result to a file, so it requires file write permissions and enough disk space.
     *
     * @throws IOException if an I/O error occurs when writing to the file
     */
    private void eratosthenesSieve() throws IOException {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        if (uL >= 2) {
            output.write(2 + " ");
        }
        int k = uL + 1;
        boolean[] isPrime = new boolean[k];
        Arrays.fill(isPrime, true);
        for (int i = 3; i <= uL; i += 2) {
            if (isPrime[i]) {
                output.write(i + " ");
                for (int j = i * i; j <= uL; j += 2 * i) {
                    isPrime[j] = false;
                }
            }
        }
        output.close();
    }

    /**
     * Implements the Sieve of Sundaram algorithm for finding all prime numbers up to a given limit.
     *
     * <p>This method generates all prime numbers up to the given upper limit (uL) and writes them into a file.
     * The Sieve of Sundaram works by eliminating certain numbers of the form i + j + 2ij where 1 <= i <= j from the list
     * of natural numbers 1, 2, ..., n. The remaining numbers are then doubled and incremented by one, yielding all
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
     * - This method requires enough memory to hold a boolean array of size 'uL'.
     * - The algorithm writes the result to a file, so it requires file write permissions and enough disk space.
     *
     * @throws IOException if an I/O error occurs when writing to the file
     */

    private void sundaramSieve() throws IOException {
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
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
        if (uL >= 2) {
            output.write(2 + " ");
        }
        for (int i = 1; i < k + 1; ++i) {
            if (isPrime[i]) {
                output.write((2 * i + 1) + " ");
            }
        }
        output.close();
    }

    /**
     * Returns the name of the file to which the prime numbers are written.
     *
     * @return The name of the file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
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

    private interface markMultiples {
        // lambda features
    }
}