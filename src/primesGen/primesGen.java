package primesGen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class primesGen {
    protected int uL;
    protected String fileName;
    protected File file;
    protected byte method;

    public primesGen(int u, byte m, String f) throws IOException {
        this.uL = u;
        if (m >= 0 && m <= 4) {
            this.method = m;
        } else throw new IllegalArgumentException("Invaild method");
        this.fileName = f;
        this.file = new File(fileName);
        if (!file.exists() || !file.createNewFile()) {
            throw new RuntimeException("Unable to create new file: " + fileName);
        }
    }

    private void eratosthenesSieve() throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        BufferedWriter output = new BufferedWriter(out);
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
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        BufferedWriter output = new BufferedWriter(out);
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

    public String getFileName() {
        return fileName;
    }

    public void run() throws IOException {
        switch (method) {
            case 0, 2, 4 -> throw new IllegalArgumentException("Invalid method.");
            case 1 -> eratosthenesSieve();
            case 3 -> sundaramSieve();
        }
    }

    private interface markMultiples {
        // lambda features
    }
}