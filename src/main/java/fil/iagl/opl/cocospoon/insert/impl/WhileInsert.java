package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtElement;

public class WhileInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtWhile;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtWhile ctWhile = (CtWhile) element;
    ctWhile.insertBefore(statementToInsert);
  }

}
