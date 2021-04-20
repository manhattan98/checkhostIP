package com.Eremej;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.Eremej.ParseUtils.*;

public class Main {
    public static final Debug DEBUG = new Debug();

    static final String EXECUTION_PATH = System.getProperty("user.dir").replace("\\", "/");
    static final String OUTPUT_FILE_NAME = "routes.bat";

    public static void main(String[] args) throws InterruptedException {
        //new Tests.URLParseTest().test(args);

        if (args.length == 0 || args[0].equals("-h")) {
            help();
            return;
        }
        if (args[0].equals("-d")) {
            DEBUG.enableDebug();
            args = Arrays.copyOfRange(args, 1, args.length);
        }

        BufferedReader inputReader = null;
        FileWriter fileWriter = null;

        try {
            List<String> lines = new LinkedList<>();

            // reading input file
            inputReader = new BufferedReader(new FileReader(args[0]));
            while (inputReader.ready())
                lines.add(removeSpecialSymbols(inputReader.readLine()).trim());

            // prepare async computation
            String[] finalArgs = args;
            FutureTask<String> mainComputation = new FutureTask<>(() ->
                    makeOutput(lines.toArray(new String[0]), Arrays.copyOfRange(finalArgs, 1, finalArgs.length)));
            Thread workerThread = new Thread(mainComputation);
            workerThread.start();

            // waiting for result...
            while (!mainComputation.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            System.out.println();

            File outFile = new File(EXECUTION_PATH + "/" + OUTPUT_FILE_NAME);
            fileWriter = new FileWriter(outFile);

            // writing output to file
            fileWriter.write(mainComputation.get());

            System.out.println("Output is saved to " + outFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File read error occurred");
        } catch (ExecutionException e) {
            System.out.println("Real shit happened");
            e.printStackTrace();
        } finally {
            if (inputReader != null)
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (fileWriter != null)
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void help() {
        System.out.println("Usage: app_name [-h] [[-d] input_file.txt [<gateway>] [metric <metric>] [if <interface>]]");
    }

    public static String makeOutput(String[] lines, String[] args) throws IOException, InterruptedException {
        InputLoader inputLoader = InputLoaders.getCheckHostLoader();
        ParseUtils.NetworkFinder networkFinder;
        OutBuilder outBuilder = OutBuilders.getWindowsADDParametrisedBuilder(args.length == 0 ? new String[] {"0.0.0.0"} : args);

        List<String> urlList = new LinkedList<>();
        List<IPSubnet> subnetList = new LinkedList<>();

        for (String line : lines) {
            String url = parseFirstURL(line);
            if (url != null) {
                urlList.add(url);

                String rawIPs = inputLoader.loadRaw(url);
                networkFinder = ParseUtils.getNetworkParser(rawIPs);

                while (networkFinder.find())
                    subnetList.add(networkFinder.getSubnet());
            }
        }

        DEBUG.log("\nLoaded web addresses: ");
        for (String url : urlList)
            DEBUG.log("\n" + url);

        System.out.print("\nLoaded " + urlList.size() + " websites " + "\nCreated " + subnetList.size() + " routes ");

        return outBuilder.build(subnetList);
    }

    public static String removeSpecialSymbols(String s) {
        return s.replaceAll("[\\p{Cc}]|[^\\p{ASCII}]", "");
    }
}
