package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

public class MethodInsert implements Insertion {
  @Override
  public boolean match(CtElement element) {
    return element instanceof CtMethod && !((CtMethod<?>) (element)).hasModifier(ModifierKind.ABSTRACT) && (((CtMethod<?>) (element)).getBody() != null);
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtMethod<?> ctMethod = (CtMethod<?>) element;
    statementToInsert.setParent(ctMethod.getBody());
    ctMethod.getBody().getStatements().add(0, statementToInsert);
  }

}
