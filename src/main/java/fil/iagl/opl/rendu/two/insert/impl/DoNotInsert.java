package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class DoNotInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return false;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
  }

}
