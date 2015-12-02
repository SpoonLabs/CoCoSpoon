package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.declaration.CtElement;

public class SwitchInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtSwitch;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtSwitch<CtCase<?>> ctSwitch = (CtSwitch) element;
    ctSwitch.getCases().forEach(ctCase -> {
      if (ctCase.getStatements().isEmpty()) {
        ctCase.addStatement(statementToInsert);
      } else {
        statementToInsert.setParent(ctCase);
        ctCase.getStatements().add(0, statementToInsert);
      }
    });
  }

}
