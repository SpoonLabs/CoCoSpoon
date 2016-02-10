package fil.iagl.opl.cocospoon;

import java.io.File;

import org.apache.commons.io.FileUtils;

import fil.iagl.opl.cocospoon.processors.WatcherProcessor;
import fil.iagl.opl.cocospoon.tools.Params;
import spoon.Launcher;

/**
 * Hello world!
 *
 */
public class CocoSpoon {
  private static final String INSTRUMENT_SOURCE_FOLDER = "src/main/java/instrumenting";

  public static void main(String[] args) throws Exception {
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
    l.addInputResource(INSTRUMENT_SOURCE_FOLDER);

    l.setSourceOutputDirectory(params.getOutputSource() + "/src/main/java");

    l.addProcessor(new WatcherProcessor(params));
    System.out.println("Start instrumentation...");
    l.buildModel();
    l.process();
    l.prettyprint();
  }

}
