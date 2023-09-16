package primesGen;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class primesGenVec extends primesGen {
    List<Integer> primes = new ArrayList<>();

    public primesGenVec(int u, byte m, String f) throws IOException {
        super(u, m, f);
    }

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

    public List<Integer> getPrimes() {
        return primes;
    }

    public void run() {
        switch (method) {
            case 0 -> trialDivision();
            case 1 -> eratosthenesSieve();
            case 2 -> eulerSieve();
            case 3 -> sundaramSieve();
            case 4 -> incrementalSieve();
        }
    }

    public void outputToFile() throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        BufferedWriter output = new BufferedWriter(out);
        for (var p : primes) {
            output.write(p + " ");
        }
        output.close();
    }
}