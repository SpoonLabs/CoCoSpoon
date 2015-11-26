package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class BreakOrReturnInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtBreak || element instanceof CtReturn;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    ((CtStatement) (element)).insertBefore(statementToInsert);
  }

}
