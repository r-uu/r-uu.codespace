package de.ruu.lib.jsonb;

import jakarta.json.bind.Jsonb;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static de.ruu.lib.util.StringBuilders.rTrimChars;
import static de.ruu.lib.util.StringBuilders.sb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Disabled("TODO find out why these tests fail when run with maven")
class TestJsonBWithMaxEncapsulationForData
{
	private final static Type   SET_OF_PARENTS = new HashSet<Parent>()
			{
				private static final long serialVersionUID = 5432832750500023858L;
			}.getClass().getGenericSuperclass();
	private final static int NUMBER_OF_PARENTS  = 3;
	private final static int NUMBER_OF_CHILDREN = 3;

	@Test void toJson()
	{
		log.debug("\nparents with children as json\n" +  getContext().toJson(createTestData()));
	}

	@Test void fromJson()
	{
		Set<Parent> parentsWithChildrenIn = createTestData();
		Jsonb jsonb = getContext();
		String json = jsonb.toJson(parentsWithChildrenIn);
		Set<Parent> parentsWithChildrenOut = jsonb.fromJson(json, SET_OF_PARENTS);
		StringBuilder sb = sb("\nparents with children\n");
		parentsWithChildrenOut.forEach(p -> sb.append(p + "\n"));
		log.debug(rTrimChars(sb, "\n").toString());
		assertEquals(
				NUMBER_OF_PARENTS,
				parentsWithChildrenOut.size(),
				"wrong number of parents");

		parentsWithChildrenOut.forEach
		(
				parent ->
				{
					assertNotNull(parent.getChildren(), "wrong value in parent.children");
					assertNotNull(parent.getField()   , "wrong value in parent.field"   );

					assertEquals(
							NUMBER_OF_CHILDREN,
							parent.getChildren().size(),
							"wrong number of children");
					parent.getChildren().forEach(child -> assertNotNull(child.getField(), "wrong value in child.field"));
				}
		);
	}

	private Jsonb getContext()
	{
		return new JsonbConfigurator().getContext();
	}

	private Set<Parent> createTestData()
	{
		Set<Parent> result = new HashSet<>();
		for (int i = 0; i < NUMBER_OF_PARENTS; i++)
		{
			Parent parent = new Parent("" + i);
			for (int j = 0; j < NUMBER_OF_CHILDREN; j++)
			{
				parent.getChildren().add(new Child("c." + j));
			}
			result.add(parent);
		}
		return result;
	}
}