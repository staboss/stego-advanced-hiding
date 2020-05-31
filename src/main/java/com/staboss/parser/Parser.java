package com.staboss.parser;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public final class Parser {

    @Option(name = "-e", usage = "enable embedding mode", forbids = {"-d"})
    public boolean embed = false;

    @Option(name = "-d", usage = "enable extracting mode", forbids = {"-e"})
    public boolean extract = false;

    @Option(name = "-s", usage = "source image name / path", required = true, metaVar = "FILE")
    public String sourceImagePath;

    @Option(name = "-r", usage = "result image name / path", metaVar = "FILE")
    public String resultImagePath;

    @Option(name = "-k", usage = "secret key filename / path", required = true, metaVar = "FILE")
    public String secretKeyPath;

    @Option(name = "-f", usage = "file with secret message", metaVar = "FILE")
    public String messageFile;

    @Option(name = "-m", usage = "steganographic method [KUTTER, COX, LSB]", required = true, metaVar = "METHOD")
    public String algorithm;

    private static Parser parser = null;
    private static CmdLineParser cmdLineParser = null;

    private Parser() {
    }

    public static Parser getInstance() {
        if (parser == null) {
            parser = new Parser();
            cmdLineParser = new CmdLineParser(parser);
        }
        return parser;
    }

    public boolean parseArgs(String[] args) {
        try {
            cmdLineParser.parseArgument(args);
            File imageFile = new File(sourceImagePath);

            if (!imageFile.exists()) {
                throw new IllegalArgumentException("Source Image File does not exist");
            }

            if ((!embed && !extract)) {
                throw new IllegalArgumentException("Program mode is not specified");
            }

            if (algorithm.isEmpty()) {
                throw new IllegalArgumentException("Method type is empty");
            }

            if (embed && messageFile == null) {
                throw new IllegalArgumentException("Secret message is required for embedding");
            }

            return true;
        } catch (IllegalArgumentException | CmdLineException e) {
            System.err.println(e.getMessage() + "\n");
            usage();
            return false;
        }
    }

    public static void usage() {
        System.err.println("usage: java -jar stego-advanced-hiding.jar -e|-d -a METHOD -s FILE [-r FILE] [-f FILE] -k FILE\n");
        System.err.println(arguments);
    }

    private static final String arguments = "optional arguments:\n" +
            "  -d           : enable embedding mode\n" +
            "  -e           : enable extracting mode\n" +
            "  -s FILE      : source img file\n" +
            "  -r FILE      : result img file\n" +
            "  -k FILE      : secret key file\n" +
            "  -f FILE      : secret msg file\n" +
            "  -m METHOD    : steganographic method [KUTTER, COX, LSB]";
}
