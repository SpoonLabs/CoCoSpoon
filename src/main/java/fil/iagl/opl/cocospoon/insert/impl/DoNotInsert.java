package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
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
