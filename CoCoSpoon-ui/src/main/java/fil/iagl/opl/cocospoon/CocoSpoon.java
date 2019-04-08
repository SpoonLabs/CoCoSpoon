package fil.iagl.opl.cocospoon;

import java.io.File;

import fil.iagl.opl.cocospoon.processors.WatcherProcessor;
import org.apache.commons.io.FileUtils;

import fil.iagl.opl.cocospoon.tools.Params;
import spoon.Launcher;
import spoon.support.compiler.ZipFolder;

/**
 * Hello world!
 */
public class CocoSpoon {
    private static final String INSTRUMENT_SOURCE_FOLDER = "src/main/java/instrumenting";

    public static void main(String[] args) throws Exception {
        Launcher l = run(args);
        System.out.println("Rewriting...");
        l.prettyprint();
        System.out.println("Done!");
    }

    public static Launcher run(String[] args) throws Exception {
        Params params = new Params();
        params.handleArgs(args);

        File inputFile = new File(params.getInputSource());
        File outputFile = new File(params.getOutputSource());

        if (outputFile.exists()) {
            System.out.println("Deleting older instrumentation...");
            FileUtils.forceDelete(outputFile);
        }

        System.out.println("Creating output folder...");
        FileUtils.copyDirectory(inputFile, outputFile);

        Launcher l = new Launcher();
        l.addInputResource(params.getInputSource() + "/src/main/java");

        if (new File(INSTRUMENT_SOURCE_FOLDER).exists()) {
            l.addInputResource(INSTRUMENT_SOURCE_FOLDER);
        } else {
            l.getModelBuilder().addInputSource(new ZipFolder(new File(getNameRunningJar())));
        }

        if (params.getClasspath() != null && !params.getClasspath().isEmpty()) {
            l.getModelBuilder().setSourceClasspath(params.getClasspath());
        }
        l.setSourceOutputDirectory(params.getOutputSource() + "/src/main/java");
        l.addProcessor(new WatcherProcessor());

        System.out.println("Building model...");
        l.buildModel();

        System.out.println("Start instrumentation...");
        l.process();

        return l;
    }

    private static String getNameRunningJar() {
        return new File(CocoSpoon.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }

}
