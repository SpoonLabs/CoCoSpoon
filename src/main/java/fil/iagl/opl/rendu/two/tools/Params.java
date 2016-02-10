package fil.iagl.opl.rendu.two.tools;

import java.util.Locale;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.IllegalOptionValueException;
import com.sanityinc.jargs.CmdLineParser.Option;

import instrumenting._View.View;

public class Params {

  private String inputSource;
  private String outputSource;
  private View view;

  public void handleArgs(String[] args) {
    CmdLineParser parse = new CmdLineParser();

    Option<String> inputSourceOption = parse.addStringOption('i', "input-path");
    Option<String> outputSourceOption = parse.addStringOption('o', "output-path");
    Option<View> viewOption = parse.addOption(new ViewOption('v', "view-type", true));

    try {
      parse.parse(args);

    } catch (CmdLineParser.OptionException e) {
      System.err.println(e.getMessage());
      printUsage();
      System.exit(2);
    }

    inputSource = parse.getOptionValue(inputSourceOption);
    outputSource = parse.getOptionValue(outputSourceOption);
    view = parse.getOptionValue(viewOption);

    if (view == null) {
      view = View.OVERALL;
    }

    if (inputSource == null || outputSource == null) {
      printUsage();
      System.exit(2);
    }
  }

  private void printUsage() {
    System.err.println("Usage: CoCoSpoon\n"
      + " -i, --input-path\t\tpath_program\n"
      + " -o, --output-path\t\tinstrumented sources destination\n"
      + "[-v, --view-type]\t\tview type : | OVERALL | INTERACTIVE | TEXT \n");
  }

  public String getInputSource() {
    return inputSource;
  }

  public String getOutputSource() {
    return outputSource;
  }

  public View getView() {
    return view;
  }

  private class ViewOption extends Option<View> {

    protected ViewOption(char shortForm, String longForm, boolean wantsValue) {
      super(shortForm, longForm, wantsValue);
    }

    @Override
    protected View getDefaultValue() {
      return View.INTERACTIVE;
    }

    @Override
    protected View parseValue(String arg, Locale locale) throws IllegalOptionValueException {
      return View.valueOf(arg);
    }

  }

}
