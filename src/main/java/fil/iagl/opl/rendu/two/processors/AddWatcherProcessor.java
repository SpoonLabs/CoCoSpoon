package fil.iagl.opl.rendu.two.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringEscapeUtils;

import fil.iagl.opl.rendu.two.App;
import fil.iagl.opl.rendu.two.insert.Insertion;
import fil.iagl.opl.rendu.two.insert.impl.BasicInsert;
import fil.iagl.opl.rendu.two.insert.impl.BeforeInsert;
import fil.iagl.opl.rendu.two.insert.impl.CaseInsert;
import fil.iagl.opl.rendu.two.insert.impl.CatchInsert;
import fil.iagl.opl.rendu.two.insert.impl.ConstructorInsert;
import fil.iagl.opl.rendu.two.insert.impl.DoInsert;
import fil.iagl.opl.rendu.two.insert.impl.DoNotInsert;
import fil.iagl.opl.rendu.two.insert.impl.ForInsert;
import fil.iagl.opl.rendu.two.insert.impl.IfInsert;
import fil.iagl.opl.rendu.two.insert.impl.MethodInsert;
import fil.iagl.opl.rendu.two.insert.impl.SwitchInsert;
import fil.iagl.opl.rendu.two.insert.impl.SynchronizedInsert;
import fil.iagl.opl.rendu.two.insert.impl.TryInsert;
import fil.iagl.opl.rendu.two.insert.impl.WhileInsert;
import fil.iagl.opl.rendu.two.tools.ContainsSameElementFilter;
import instrumenting._Instrumenting;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLambda;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.visitor.filter.TypeFilter;

public class AddWatcherProcessor extends AbstractProcessor<CtClass<?>> {

  public static final Predicate<CtElement> needToBeInstrumented = (candidate) -> !(candidate instanceof CtUnaryOperator)
    && !(candidate instanceof CtClass)
    && !(candidate instanceof CtBlock) && !candidate.isImplicit()
    && !(candidate instanceof CtLiteral)
    && !(candidate instanceof CtCodeSnippetStatement)
    && !(candidate instanceof CtTypeAccess)
    && !(candidate instanceof CtField)
    && !(candidate.getParent() instanceof CtLambda)
    && !isInsideIfForSwitchDoWhile(candidate);

  public static final List<Insertion> filters = Arrays.asList(
    new ConstructorInsert(),
    new MethodInsert(),
    new TryInsert(),
    new CatchInsert(),
    new ForInsert(),
    new WhileInsert(),
    new DoInsert(),
    new SynchronizedInsert(),
    new IfInsert(),
    new SwitchInsert(),
    new CaseInsert(),
    new BeforeInsert(),
    new BasicInsert());

  @Override
  public boolean isToBeProcessed(CtClass<?> candidate) {
    return !candidate.isAnonymous() && candidate.getPackage() != null && !candidate.getPackage().getQualifiedName().startsWith("instrumenting");
  }

  @Override
  public void processingDone() {
    try {
      CtClass<Object> instrumentClass = getFactory().Class().get(_Instrumenting.class);

      File tmpFile = File.createTempFile("opl_instrumented", "");
      FileOutputStream fout = new FileOutputStream(tmpFile);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(_Instrumenting.lines);
      oos.close();

      instrumentClass.getField("TMP_FILE_NAME")
        .setDefaultExpression(getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(tmpFile.getAbsolutePath()) + "\""));
      instrumentClass.getField("CURRENT_DIR")
        .setDefaultExpression(getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(App.INPUT_SOURCE_FOLDER) + "\""));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Diplay MAP
    // for (Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
    // System.out.println(entry.getKey() + " -> ");
    // for (Integer line : entry.getValue().keySet()) {
    // System.out.println("\t" + line);
    // }
    // }
  }

  @Override
  public void process(CtClass<?> ctClass) {
    ctClass.getElements(new TypeFilter<>(CtElement.class)).stream().filter(needToBeInstrumented).forEach(statement -> findMatcherAndApply(statement));
  }

  private void findMatcherAndApply(CtElement element) {
    instrumentLine(filters.stream().filter(filter -> filter.match(element)).findFirst().orElse(new DoNotInsert()), element);
  }

  private void instrumentLine(Insertion filter, CtElement element) {
    String qualifiedName = element.getParent(CtClass.class).getQualifiedName();
    if (qualifiedName.contains("$")) {
      qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("$"));
    }

    _Instrumenting.addInstrumentedClass(qualifiedName);
    _Instrumenting.addInstrumentedStatement(qualifiedName, element.getPosition().getLine());
    CtCodeSnippetStatement statementToInsert = getFactory().Code()
      .createCodeSnippetStatement(
        "instrumenting._Instrumenting.isPassedThrough(\"" + qualifiedName + "\", " + element.getPosition().getLine() + ")");
    filter.apply(element, statementToInsert);
  }

  private static boolean isInsideIfForSwitchDoWhile(CtElement candidate) {
    boolean isInsideIfExpression = false;
    boolean isInsideForExpression = false;
    boolean isInsideForInit = false;
    boolean isInsideForUpdate = false;
    boolean isInsideSwitchInit = false;
    boolean isInsideDoExpression = false;
    boolean isInsideWhileExpression = false;
    if (candidate.getParent(CtIf.class) != null) {
      CtIf ctIf = candidate.getParent(CtIf.class);
      isInsideIfExpression = !ctIf.getCondition().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
    }

    if (candidate.getParent(CtFor.class) != null) {
      CtFor ctFor = candidate.getParent(CtFor.class);
      isInsideForExpression = !(ctFor.getExpression() == null) && !ctFor.getExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
      isInsideForInit = !(ctFor.getForInit() == null)
        && ctFor.getForInit().stream().anyMatch(statement -> !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty());
      isInsideForUpdate = !(ctFor.getForUpdate() == null)
        && ctFor.getForUpdate().stream().anyMatch(statement -> !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty());
    }

    if (candidate.getParent(CtSwitch.class) != null) {
      CtSwitch<?> ctSwitch = candidate.getParent(CtSwitch.class);
      isInsideSwitchInit = !ctSwitch.getSelector().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
    }

    if (candidate.getParent(CtDo.class) != null) {
      CtDo ctDo = candidate.getParent(CtDo.class);
      isInsideDoExpression = !ctDo.getLoopingExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
    }

    if (candidate.getParent(CtWhile.class) != null) {
      CtWhile ctWhile = candidate.getParent(CtWhile.class);
      isInsideDoExpression = !ctWhile.getLoopingExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
    }

    return isInsideIfExpression || isInsideDoExpression || isInsideForExpression || isInsideForInit || isInsideForUpdate || isInsideSwitchInit || isInsideWhileExpression;
  }

}
