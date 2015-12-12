package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class CaseInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtCase;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtCase<?> ctCase = (CtCase<?>) element;
    if (ctCase.getStatements().isEmpty()) {
      ctCase.addStatement(statementToInsert);
    } else {
      statementToInsert.setParent(ctCase);
      ctCase.getStatements().add(0, statementToInsert);
    }
  }

}
