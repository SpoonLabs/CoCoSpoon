package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ConstructorInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtConstructor;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtConstructor<?> ctConstructor = (CtConstructor<?>) element;
    ctConstructor.getBody().insertBegin(statementToInsert);
  }

}
