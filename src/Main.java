import primesGen.primesGen;
import primesGen.primesGenSeg;
import primesGen.primesGenVec;
import primesGen.primesGenVecSeg;

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
                primesGen results = new primesGen(n, methods, file);
                results.run();
            } else if (threads == 1 && (methods == 0 || methods == 2 || methods == 4)) {
                primesGenVec results = new primesGenVec(n, methods, file);
                results.run();
                results.outputToFile();
            } else if (threads > 1 && (methods == 1 || methods == 3)) {
                // yet to be done
                int sqrtN = (int) Math.sqrt(n);

                primesGenVec preSieve = new primesGenVec(sqrtN, methods, file);
                Thread preSievingThread = new Thread(preSieve, "Pre-sieveing thread.");
                preSievingThread.start();

                int perThread = (n - sqrtN) / threads;
                List<Thread> threadList = new ArrayList<>();
                List<primesGenSeg> results = new ArrayList<>();

                preSievingThread.join();
                List<Integer> preSievedPrimes = preSieve.getPrimes();

                for (int i = 0; i < threads; ++i) {
                    int lL = sqrtN + i * perThread + 1;
                    int uL = sqrtN + (i + 1) * perThread;
                    String fileName = ".temp+" + i + "+" + file;

                    if (i == threads - 1) {
                        uL = n;
                    }

                    results.add(new primesGenSeg(lL, uL, preSievedPrimes, methods, fileName));

                    threadList.add(new Thread(results.get(i), fileName));
                    threadList.get(i).start();
                }

                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));

                preSieve.output(output);

                for (int i = 0; i < threads; ++i) {
                    threadList.get(i).join();
                    String fileName = ".temp+" + i + "+" + file;
                    File tempFile = new File(fileName);
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

                primesGenVec preSieve = new primesGenVec(sqrtN, methods, file);
                Thread preSievingThread = new Thread(preSieve, "Pre-sieveing thread.");
                preSievingThread.start();

                int perThread = (n - sqrtN) / threads;

                List<Thread> threadList = new ArrayList<>();
                List<primesGenVecSeg> results = new ArrayList<>();

                preSievingThread.join();
                List<Integer> preSievedPrimes = preSieve.getPrimes();

                for (int i = 0; i < threads; ++i) {
                    int lL = sqrtN + i * perThread + 1;
                    int uL = sqrtN + (i + 1) * perThread;
                    String fileName = ".temp+" + i + "+" + file;

                    if (i == threads - 1) {
                        uL = n;
                    }

                    results.add(new primesGenVecSeg(lL, uL, preSievedPrimes, methods, fileName));

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