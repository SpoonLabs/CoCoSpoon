package fil.iagl.opl.rendu.two.insert.impl;

import java.util.List;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ConstructorInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtConstructor;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtConstructor ctConstructor = (CtConstructor) element;
    List<CtStatement> statements = ctConstructor.getBody().getStatements();
    CtStatement insertAfterThis = statements.stream().filter(statement -> statement instanceof CtSuperAccess || statement instanceof CtThisAccess).findFirst()
      .orElse(statements.get(0));
    statementToInsert.setParent(ctConstructor.getBody());
    insertAfterThis.insertAfter(statementToInsert);
  }

}
