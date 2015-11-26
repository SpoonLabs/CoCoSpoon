package fil.iagl.opl.rendu.two.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringEscapeUtils;

import fil.iagl.opl.rendu.two.insert.Insertion;
import fil.iagl.opl.rendu.two.insert.impl.BasicInsert;
import fil.iagl.opl.rendu.two.insert.impl.BreakOrReturnInsert;
import fil.iagl.opl.rendu.two.insert.impl.CaseInsert;
import fil.iagl.opl.rendu.two.insert.impl.CatchInsert;
import fil.iagl.opl.rendu.two.insert.impl.ConstructorInsert;
import fil.iagl.opl.rendu.two.insert.impl.DoNotInsert;
import fil.iagl.opl.rendu.two.insert.impl.IfInsert;
import fil.iagl.opl.rendu.two.insert.impl.MethodInsert;
import fil.iagl.opl.rendu.two.insert.impl.SwitchInsert;
import fil.iagl.opl.rendu.two.insert.impl.TryInsert;
import instrumenting._Instrumenting;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

public class AddWatcherProcessor extends AbstractProcessor<CtClass> {

  private static final Predicate<CtElement> needToBeInstrumented = (candidate) -> !(candidate instanceof CtUnaryOperator)
    && !(candidate instanceof CtClass)
    && !(candidate.getParent() instanceof CtIf)
    && !((candidate instanceof CtInvocation && candidate.getParent() instanceof CtSwitch))
    && !(candidate instanceof CtBlock) && !candidate.isImplicit()
    && !(candidate instanceof CtLiteral)
    && !(candidate instanceof CtCodeSnippetStatement)
    && !(candidate instanceof CtTypeAccess);

  private static final List<Insertion> filters = Arrays.asList(new ConstructorInsert(), new MethodInsert(), new TryInsert(), new CatchInsert(), new IfInsert(), new SwitchInsert(),
    new CaseInsert(),
    new BreakOrReturnInsert(),
    new BasicInsert());

  @Override
  public boolean isToBeProcessed(CtClass candidate) {
    return !candidate.isAnonymous() && !candidate.getPackage().getQualifiedName().startsWith("instrumenting");
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
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void process(CtClass ctClass) {
    ctClass.getElements(new TypeFilter<>(CtElement.class)).stream().filter(needToBeInstrumented).forEach(statement -> findMatcherAndApply(statement));

    System.out.println(ctClass.getQualifiedName() + "-----------------\n" + _Instrumenting.lines.get(ctClass.getQualifiedName()));
  }

  private void findMatcherAndApply(CtElement element) {
    instrumentLine(filters.stream().filter(filter -> filter.match(element)).findFirst().orElse(new DoNotInsert()), element);
  }

  private void instrumentLine(Insertion filter, CtElement element) {
    String qualifiedName = element.getParent(CtClass.class).getQualifiedName();
    _Instrumenting.addInstrumentedClass(qualifiedName);
    _Instrumenting.addInstrumentedStatement(qualifiedName, element.getPosition().getLine());
    CtCodeSnippetStatement statementToInsert = getFactory().Code()
      .createCodeSnippetStatement("instrumenting._Instrumenting.isPassedThrough(\"" + qualifiedName + "\",new instrumenting._Line(" + element.getPosition().getLine() + "))");

    filter.apply(element, statementToInsert);

  }

}
