package primesGen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class primesGenVecSeg extends primesGenVec {
    private final List<Integer> preSievedPrimes;
    private int lL;


    public primesGenVecSeg(int l, int u, List<Integer> pSP, byte m, String f) throws IOException {
        super(u, m, f);
        if (l >= u) {
            throw new IllegalArgumentException("l should be smaller than u.");
        }
        this.lL = l;
        this.preSievedPrimes = pSP;
    }

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

    private void sundaramSieve() {
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

        if (2 >= lL && 2 <= uL) {
            primes.add(2);
        }

        for (int i = 0; i <= newUL - newLL; ++i) {
            if (isPrime[i]) {
                primes.add(2 * (i + newLL) + 1);
            }
        }
    }

    private void incrementalSieve() {
        primes = preSievedPrimes;
        List<Integer> mp = new ArrayList<>(primes.size());
        List<Integer> results = new ArrayList<>();

        // Initialize the multiples of pre-sieved primes
        for (int k = 0; k < primes.size(); ++k) {
            mp.set(k, ((lL + primes.get(k) - 1) / primes.get(k)) * primes.get(k));
        }

        for (int i = lL; i <= uL; ++i) {
            boolean isPrime = true;
            int limit = (int) Math.sqrt(i);
            for (int k = 0; k < primes.size(); ++k) {
                if (primes.get(k) > limit) {
                    break;
                }
                while (mp.get(k) < i) {
                    mp.set(k, primes.get(k));
                }
                if (mp.get(k) == i) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
                results.add(i);
                mp.add(i * i);
            }
        }
        primes = results;
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
}
