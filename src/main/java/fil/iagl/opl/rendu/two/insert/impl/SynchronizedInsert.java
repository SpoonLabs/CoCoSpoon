package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSynchronized;
import spoon.reflect.declaration.CtElement;

public class SynchronizedInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtSynchronized;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtSynchronized ctSync = (CtSynchronized) element;
    ctSync.getBlock().getStatements().add(0, statementToInsert);

  }

}
