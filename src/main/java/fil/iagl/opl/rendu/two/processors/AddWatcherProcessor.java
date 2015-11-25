package fil.iagl.opl.rendu.two.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringEscapeUtils;

import instrumenting._Instrumenting;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class AddWatcherProcessor extends AbstractProcessor<CtClass> {

  private static final Predicate<CtStatement> needToBeInstrumented = (candidate) -> !(candidate instanceof CtClass) && !(candidate instanceof CtBlock) && !candidate.isImplicit();

  /*
   * TODO: Handle one line code
   */

  // private CtClass instrumentingClass;

  @Override
  public boolean isToBeProcessed(CtClass candidate) {
    boolean innerPackage = candidate.getPackage().getQualifiedName().startsWith("instrumenting");
    return !innerPackage;
  }

  @Override
  public void processingDone() {
    try {
      CtClass<Object> instrumentClass = getFactory().Class().get(_Instrumenting.class);

      File tmpFile = File.createTempFile("opl_instrumented", "");
      FileOutputStream fout = new FileOutputStream(tmpFile);
      ObjectOutputStream oos = new ObjectOutputStream(fout);
      oos.writeObject(_Instrumenting.lines);

      instrumentClass.getField("TMP_FILE_NAME")
        .setDefaultExpression(getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(tmpFile.getAbsolutePath()) + "\""));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void process(CtClass ctClass) {
    ctClass.getElements(new TypeFilter<>(CtStatement.class)).stream().filter(needToBeInstrumented).forEach(statement -> instrumentLine(statement));

    System.out.println(ctClass.getQualifiedName() + "-----------------\n" + _Instrumenting.lines.get(ctClass.getQualifiedName()));
  }

  private void instrumentLine(CtStatement statement) {
    String qualifiedName = statement.getParent(CtClass.class).getQualifiedName();
    if (!(statement instanceof CtReturn) && statement.getParent(CtReturn.class) == null) {
      _Instrumenting.addInstrumentedClass(qualifiedName);
      _Instrumenting.addInstrumentedStatement(qualifiedName, statement.getPosition().getLine());

      CtCodeSnippetStatement statementToInsert = getFactory().Code()
        .createCodeSnippetStatement("instrumenting._Instrumenting.isPassedThrough(\"" + qualifiedName + "\",new instrumenting._Line(" + statement.getPosition().getLine() + "))");
      statement
        .insertAfter(statementToInsert);
    } else {
      // return case
      // TODO: Find all call inside code and add instrumentation at next line
    }

  }

  // @Override
  // public void init() {
  // instrumentingClass = getFactory().Class().create(getFactory().Package().getOrCreate("iagl.opl.generated"),
  // "_Instrumenting");
  //
  // CtTypeReference mapType = getFactory().Core().createTypeReference().setSimpleName("java.util.Map");
  // CtTypeReference stringType = (CtTypeReference<?>) getFactory().Core().createTypeReference().setSimpleName("java.lang.String");
  // CtTypeReference setType = (CtTypeReference<?>) getFactory().Core().createTypeReference().setSimpleName("java.util.Set");
  //
  // mapType.addActualTypeArgument(stringType);
  // mapType.addActualTypeArgument(setType);
  //
  // CtField lines = getFactory().Core().createField();
  // lines.addModifier(ModifierKind.PRIVATE);
  // lines.setSimpleName("lines");
  // lines.setType(mapType);
  // instrumentingClass.addField(lines);
  //
  // CtClass lineClass = getFactory().Class().create(getFactory().Package().getOrCreate("iagl.opl.generated"),
  // "_Line");
  //
  // CtField position = getFactory().Core().createField();
  // position.addModifier(ModifierKind.PRIVATE);
  // position.setSimpleName("position");
  // position.setType(getFactory().Type().INTEGER);
  // lineClass.addField(position);
  //
  // CtField executed = getFactory().Core().createField();
  // executed.addModifier(ModifierKind.PRIVATE);
  // executed.setSimpleName("executed");
  // executed.setType(getFactory().Type().BOOLEAN);
  // lineClass.addField(executed);
  //
  // CtParameter<Integer> param = getFactory().Core().createParameter();
  // param.setSimpleName("position");
  // param.setType(getFactory().Type().INTEGER);
  //
  // CtConstructor constructor = getFactory().Core().createConstructor();
  // constructor.addModifier(ModifierKind.PUBLIC);
  // constructor.addParameter(param);
  // constructor.setBody(getFactory().Code()
  // .createCtBlock(getFactory().Code().createCodeSnippetStatement("this.position = position")));
  // lineClass.addConstructor(constructor);
  //
  // instrumentingClass.addNestedType(lineClass);
  //
  // System.out.println(instrumentingClass);
  // }

}
