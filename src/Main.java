import PrimesGen.PrimesGen;
import PrimesGen.PrimesGenSeg;
import PrimesGen.PrimesGenVec;
import PrimesGen.PrimesGenVecSeg;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 4) {
                throw new IllegalArgumentException("Invalid arguments");
            }
            int threads = Integer.parseInt(args[0]);
            byte methods = Byte.parseByte(args[1]);
            int n = Integer.parseInt(args[2]);
            String file = args[3];
            if (threads == 1 && (methods == 1 || methods == 3)) {
                PrimesGen results = new PrimesGen(n, methods, file);
                results.run();
            } else if (threads == 1 && (methods == 0 || methods == 2 || methods == 4)) {
                PrimesGenVec results = new PrimesGenVec(n, methods, file);
                results.run();
                results.outputToFile();
            } else if (threads > 1 && (methods == 1 || methods == 3)) {
                int sqrtN = (int) Math.sqrt(n);

                PrimesGenVec preSieve = new PrimesGenVec(sqrtN, methods, file);
                Thread preSievingThread = new Thread(preSieve, "Pre-sieveing thread.");
                preSievingThread.start();

                int perThread = (n - sqrtN) / threads;
                List<Thread> threadList = new ArrayList<>();
                List<PrimesGenSeg> results = new ArrayList<>();

                preSievingThread.join();
                List<Integer> preSievedPrimes = preSieve.getPrimes();

                for (int i = 0; i < threads; ++i) {
                    int lL = sqrtN + i * perThread + 1;
                    int uL = sqrtN + (i + 1) * perThread;
                    String fileName = ".temp+" + i + "+" + file;

                    if (i == threads - 1) {
                        uL = n;
                    }

                    results.add(new PrimesGenSeg(lL, uL, preSievedPrimes, methods, fileName));

                    threadList.add(new Thread(results.get(i), fileName));
                    threadList.get(i).start();
                }

                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));

                preSieve.output(output);

                for (int i = 0; i < threads; ++i) {
                    threadList.get(i).join();
                    String fileName = ".temp+" + i + "+" + file;
                    File tempFile = new File(fileName);
                    if (!tempFile.exists() && !tempFile.createNewFile()) {
                        throw new RuntimeException("Unable to create new file: " + fileName);
                    }
                    BufferedReader reader = new BufferedReader(new FileReader(tempFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.write(line);
                    }
                    if (!tempFile.delete()) {
                        throw new RuntimeException("Failed to delete file: " + tempFile);
                    }
                }
                output.close();
            } else if (threads > 1 && (methods == 0 || methods == 2 || methods == 4)) {
                int sqrtN = (int) Math.sqrt(n);

                PrimesGenVec preSieve = new PrimesGenVec(sqrtN, methods, file);
                Thread preSievingThread = new Thread(preSieve, "Pre-sieveing thread.");
                preSievingThread.start();

                int perThread = (n - sqrtN) / threads;

                List<Thread> threadList = new ArrayList<>();
                List<PrimesGenVecSeg> results = new ArrayList<>();

                preSievingThread.join();
                List<Integer> preSievedPrimes = preSieve.getPrimes();

                for (int i = 0; i < threads; ++i) {
                    int lL = sqrtN + i * perThread + 1;
                    int uL = sqrtN + (i + 1) * perThread;
                    String fileName = ".temp+" + i + "+" + file;

                    if (i == threads - 1) {
                        uL = n;
                    }

                    results.add(new PrimesGenVecSeg(lL, uL, preSievedPrimes, methods, fileName));

                    threadList.add(new Thread(results.get(i), fileName));
                    threadList.get(i).start();
                }

                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));

                preSieve.output(output);

                BufferedWriter outputAppend = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
                for (int i = 0; i < threads; ++i) {
                    threadList.get(i).join();
                    results.get(i).output(outputAppend);
                }
                output.close();
                outputAppend.close();
            } else throw new IllegalArgumentException("Invalid arguments");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Please submit your issue at https://github.com/Water-Buckets/findPrimes-Java/issues");
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}