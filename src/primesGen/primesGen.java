package primesGen;

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
public class primesGen implements Runnable {
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
     * @param u The upper limit for prime number generation.
     * @param m The method to be used for prime number generation.
     * @param f The name of the file where the prime numbers will be written.
     * @throws IOException              If there is an error creating the file.
     * @throws IllegalArgumentException If an invalid method is specified.
     */
    public primesGen(int u, byte m, String f) throws IOException {
        this.uL = u;
        if (m >= 0 && m <= 4) {
            this.method = m;
        } else throw new IllegalArgumentException("Invaild method");
        this.fileName = f;
        this.file = new File(fileName);
        if (!file.exists() && !file.createNewFile()) {
            throw new RuntimeException("Unable to create new file: " + fileName);
        }
    }

    /**
     * Implements the Sieve of Eratosthenes for generating prime numbers.
     *
     * @throws IOException If there is an error writing to the file.
     */
    private void eratosthenesSieve() throws IOException {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
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
     * Implements the Sieve of Sundaram for generating prime numbers.
     *
     * @throws IOException If there is an error writing to the file.
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
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
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