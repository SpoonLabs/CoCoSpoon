package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtContinue;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtElement;

public class BeforeInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtBreak
      || element instanceof CtReturn
      || element instanceof CtThrow
      || element instanceof CtContinue;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    ((CtStatement) (element)).insertBefore(statementToInsert);
  }

}
