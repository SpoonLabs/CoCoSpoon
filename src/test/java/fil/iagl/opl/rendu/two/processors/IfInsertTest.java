package fil.iagl.opl.rendu.two.processors;

import java.util.stream.Collectors;

import org.fest.assertions.Assertions;
import org.junit.Test;

import fil.iagl.opl.rendu.two.insert.Insertion;
import fil.iagl.opl.rendu.two.insert.impl.IfInsert;
import fil.iagl.opl.rendu.two.tools.ContainsSameElementFilter;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.NameFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class IfInsertTest {

  @Test
  public void instrumentIfTest() throws Exception {
    Launcher l = new Launcher();

    l.addInputResource("src/test/java");
    l.buildModel();

    CtClass<?> ifSample = (CtClass<?>) l.getFactory().Package().getRootPackage().getElements(new NameFilter<>("IfSample")).get(0);

    Integer nbIf = 12;
    Integer nbStatementToInsert = 15;
    Insertion insertionStrategy = new IfInsert();
    CtStatement statementToInsert = l.getFactory().Code().createCodeSnippetStatement("TO BE INSERT");
    Assertions.assertThat(
      ifSample.getElements(new TypeFilter<CtElement>(CtElement.class))
        .stream().filter(insertionStrategy::match).collect(Collectors.toList()))
      .hasSize(nbIf);

    ifSample.getElements(new TypeFilter<CtElement>(CtElement.class))
      .stream().filter(insertionStrategy::match).forEach(element -> insertionStrategy.apply(element, statementToInsert));

    System.out.println(ifSample);
    Assertions.assertThat(
      ifSample.getElements(new ContainsSameElementFilter(statementToInsert)))
      .hasSize(nbStatementToInsert);

  }

}
