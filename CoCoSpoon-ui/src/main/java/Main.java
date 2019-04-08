import fil.iagl.opl.cocospoon.CocoSpoon;
import fil.iagl.opl.cocospoon.tools.Params;
import instrumenting._Instrumenting;
import instrumenting._View;
import org.apache.commons.lang3.StringEscapeUtils;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 */
public class Main {

    public static void main(String[] args) {
        try {
            args = getView(args);
            Launcher launcher = CocoSpoon.run(args);
            Params params = new Params();
            params.handleArgs(args);
            CtClass<Object> instrumentClass = launcher.getFactory().Class().get(_Instrumenting.class);

            File tmpFile = File.createTempFile("opl_instrumented", "");
            FileOutputStream fout = new FileOutputStream(tmpFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(_Instrumenting.lines);
            oos.close();

            instrumentClass.getField("TMP_FILE_NAME")
                    .setDefaultExpression(launcher.getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(tmpFile.getAbsolutePath()) + "\""));
            instrumentClass.getField("CURRENT_DIR")
                    .setDefaultExpression(launcher.getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(params.getInputSource()) + "\""));
            instrumentClass.getField("VIEW")
                    .setDefaultExpression(launcher.getFactory().Code().createCodeSnippetExpression("instrumenting._View.View." + view));

            System.out.println("Rewriting...");
            launcher.prettyprint();
            System.out.println("Done!");

        } catch (Exception ignored) {

        }
    }

    private static _View.View view = _View.View.OVERALL;;

    private static String[] getView(String [] args) {
        List<String> argsWithoutView = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if ("-v".equals(args[i]) && i + 1 < args.length) {
                view = _View.View.valueOf(args[i+1]);
                i++;
            } else {
                argsWithoutView.add(args[i]);
            }
        }
        return argsWithoutView.toArray(new String[argsWithoutView.size()]);
    }
}
