package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class CatchInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtCatch;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtCatch ctCatch = (CtCatch) element;
    statementToInsert.setParent(ctCatch.getBody());
    ctCatch.getBody().getStatements().add(0, statementToInsert);
  }

}
