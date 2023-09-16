import primesGen.primesGen;
import primesGen.primesGenVec;

import java.io.IOException;

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
            } else if (threads > 1 && (methods == 0 || methods == 2 || methods == 4)) {
                // yet to be done
            } else throw new IllegalArgumentException("Invalid arguments");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Please submit your issue at https://github.com/Water-Buckets/findPrimes-Java/issues");
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}