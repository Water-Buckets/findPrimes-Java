package primesGen;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class primesGenSeg extends primesGen {
    private final int lL;
    private final List<Integer> preSievedPrimes;


    public primesGenSeg(int l, int u, List<Integer> pSP, byte m, String f) throws IOException {
        super(u, m, f);
        if (l >= u) {
            throw new IllegalArgumentException("l should be smaller than u.");
        }
        this.lL = l;
        this.preSievedPrimes = pSP;
    }

    private void eratosthenesSieve() throws IOException {
        boolean[] isPrime = new boolean[uL - lL + 1];
        Arrays.fill(isPrime, true);
        for (var prime : preSievedPrimes) {
            int i = Math.max(prime * prime, (lL + prime - 1) / prime * prime);
            for (int j = i; j <= uL; j += prime) {
                isPrime[j - lL] = false;
            }
        }
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        BufferedWriter output = new BufferedWriter(out);
        for (int i = (lL % 2 != 0 ? lL : lL + 1); i <= uL; i += 2) {
            if (isPrime[i - lL]) {
                output.write(i + " ");
            }
        }
        output.close();
    }

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
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        BufferedWriter output = new BufferedWriter(out);
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
}
