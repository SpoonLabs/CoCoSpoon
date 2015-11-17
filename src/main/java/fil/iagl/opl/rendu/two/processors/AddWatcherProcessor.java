package fil.iagl.opl.rendu.two.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

public class AddWatcherProcessor extends AbstractProcessor<CtStatement> {

  /*
   * TODO: Handle one line code
   */

  private CtClass instrumentingClass;

  private static Map<String, Set<Line>> lines = new HashMap<String, Set<Line>>();

  @Override
  public boolean isToBeProcessed(CtStatement candidate) {
    return !(candidate instanceof CtClass) && !(candidate instanceof CtBlock) && !candidate.isImplicit();
  }

  public void process(CtStatement arg0) {
    System.out.println(arg0.getParent(CtClass.class).getQualifiedName() + ":" + arg0.getPosition().getLine() + " -> " + arg0);
    instrumentLine(arg0);
  }

  private void instrumentLine(CtStatement arg0) {
    String qualifiedName = arg0.getParent(CtClass.class).getQualifiedName();
    SourcePosition position = arg0.getPosition();
    if (lines.get(qualifiedName) == null) {
      lines.put(qualifiedName, new TreeSet<Line>());
    }
    lines.get(qualifiedName).add(new Line(position.getLine()));
    arg0.insertBefore(getFactory().Code().createCodeSnippetStatement("toto"));
  }

  @Override
  public void init() {
    instrumentingClass = getFactory().Class().create(getFactory().Package().getOrCreate("iagl.opl.generated"),
      "_Instrumenting");

    CtTypeReference mapType = getFactory().Core().createTypeReference().setSimpleName("java.util.Map");
    CtTypeReference stringType = (CtTypeReference<?>) getFactory().Core().createTypeReference().setSimpleName("java.lang.String");
    CtTypeReference setType = (CtTypeReference<?>) getFactory().Core().createTypeReference().setSimpleName("java.util.Set");

    mapType.addActualTypeArgument(stringType);
    mapType.addActualTypeArgument(setType);

    CtField lines = getFactory().Core().createField();
    lines.addModifier(ModifierKind.PRIVATE);
    lines.setSimpleName("lines");
    lines.setType(mapType);
    instrumentingClass.addField(lines);

    CtClass lineClass = getFactory().Class().create(getFactory().Package().getOrCreate("iagl.opl.generated"),
      "_Line");

    CtField position = getFactory().Core().createField();
    position.addModifier(ModifierKind.PRIVATE);
    position.setSimpleName("position");
    position.setType(getFactory().Type().INTEGER);
    lineClass.addField(position);

    CtField executed = getFactory().Core().createField();
    executed.addModifier(ModifierKind.PRIVATE);
    executed.setSimpleName("executed");
    executed.setType(getFactory().Type().BOOLEAN);
    lineClass.addField(executed);

    CtParameter<Integer> param = getFactory().Core().createParameter();
    param.setSimpleName("position");
    param.setType(getFactory().Type().INTEGER);

    CtConstructor constructor = getFactory().Core().createConstructor();
    constructor.addModifier(ModifierKind.PUBLIC);
    constructor.addParameter(param);
    constructor.setBody(getFactory().Code()
      .createCtBlock(getFactory().Code().createCodeSnippetStatement("this.position = position")));
    lineClass.addConstructor(constructor);

    instrumentingClass.addNestedType(lineClass);

    System.out.println(instrumentingClass);

    //
    // CtTypeReference genericType = getFactory().Core().createTypeReference().setSimpleName("T");
    //
    // instrumentingClass.addFormalTypeParameter(genericType);
    // CtConstructor<Object> constructor = getFactory().Core().createConstructor();
    //
    // CtField referenceField = getFactory().Core().createField();
    // referenceField.addModifier(ModifierKind.PRIVATE);
    // referenceField.setSimpleName("ref");
    // referenceField.setType(genericType);
    // instrumentingClass.addField(referenceField);
    //
    // CtParameter<?> param = getFactory().Core().createParameter();
    // param.setSimpleName("ref");
    // param.setType(genericType);
    //
    // CtMethod<?> ctGetter = getFactory().Core().createMethod();
    // ctGetter.setType(genericType);
    // ctGetter.addModifier(ModifierKind.PUBLIC);
    // ctGetter.setSimpleName("getRef");
    // ctGetter.setBody(getFactory().Core().createBlock()
    // .addStatement(getFactory().Code().createCodeSnippetStatement("return this.ref")));
    //
    // CtMethod<?> ctSetter = getFactory().Core().createMethod();
    // ctSetter.setType(getFactory().Core().createTypeReference().setSimpleName("void"));
    // ctSetter.addModifier(ModifierKind.PUBLIC);
    // ctSetter.setSimpleName("setRef");
    // ctSetter.addParameter(param);
    // ctSetter.setBody(getFactory().Core().createBlock()
    // .addStatement(getFactory().Code().createCodeSnippetStatement("this.ref = ref")));
    //
    // constructor.addModifier(ModifierKind.PUBLIC);
    // constructor.addParameter(param);
    // constructor.setBody(getFactory().Code()
    // .createCtBlock(getFactory().Code().createCodeSnippetStatement("this.ref = ref")));
    //
    // // System.out.println(reference.getPackage());
    // instrumentingClass.addModifier(ModifierKind.PUBLIC);
    // instrumentingClass.addConstructor(constructor);
    // instrumentingClass.addMethod(ctGetter);
    // instrumentingClass.addMethod(ctSetter);
    // // System.out.println(reference);
  }

  @Override
  public void processingDone() {
    System.out.println(lines);
  }

  private class Line implements Comparable<Line> {
    int position;
    boolean executed;

    public Line(int position) {
      super();
      this.position = position;
      this.executed = false;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + (executed ? 1231 : 1237);
      result = prime * result + position;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Line other = (Line) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (executed != other.executed)
        return false;
      if (position != other.position)
        return false;
      return true;
    }

    private AddWatcherProcessor getOuterType() {
      return AddWatcherProcessor.this;
    }

    @Override
    public String toString() {
      return "Line [position=" + position + ", executed=" + executed + "]";
    }

    public int compareTo(Line o) {
      return position - o.position;
    }

  }

}
