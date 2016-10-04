package fil.iagl.opl.cocospoon.processors;


import fil.iagl.opl.cocospoon.insert.Insertion;
import fil.iagl.opl.cocospoon.insert.impl.WhileInsert;
import fil.iagl.opl.cocospoon.tools.ContainsSameElementFilter;
import org.fest.assertions.Assertions;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.NameFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WhileInsertTest {
	@Test
	public void instrumentSwitchTest() throws Exception {
		Launcher l = new Launcher();

		l.addInputResource("src/test/java/fil/iagl/opl/cocospoon/samples");
		l.buildModel();

		CtClass<?> sample = (CtClass<?>) l.getFactory().Package().getRootPackage().getElements(new NameFilter<>("WhileSample")).get(0);

		int nbWhile = 2;
		int nbStatementToInsert = 2;
		final Insertion insertionStrategy = new WhileInsert();
		CtStatement statementToInsert = l.getFactory().Code().createCodeSnippetStatement("TO BE INSERT");
   /* Assertions.assertThat(
	  sample.getElements(new TypeFilter<CtElement>(CtElement.class))
        .stream().filter(insertionStrategy::match).collect(Collectors.toList()))
      .hasSize(nbWhile);

    sample.getElements(new TypeFilter<CtElement>(CtElement.class))
      .stream().filter(insertionStrategy::match).forEach(element -> insertionStrategy.apply(element, statementToInsert));*/

		List<CtElement> elements = sample.getElements(new TypeFilter<CtElement>(CtElement.class) {
			@Override
			public boolean matches(CtElement element) {
				return insertionStrategy.match(element);
			}
		});

		assertEquals(nbWhile, elements.size());

		for (CtElement element : elements) {
			insertionStrategy.apply(element, statementToInsert);
		}

		System.out.println(sample);
		Assertions.assertThat(
				sample.getElements(new ContainsSameElementFilter(statementToInsert)))
				.hasSize(nbStatementToInsert);
	}

}
