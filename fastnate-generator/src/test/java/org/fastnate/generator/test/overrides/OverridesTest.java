package org.fastnate.generator.test.overrides;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;

import org.fastnate.generator.test.AbstractEntitySqlGeneratorTest;
import org.fastnate.generator.test.SimpleTestEntity;
import org.fastnate.generator.test.embedded.TestEmbeddedProperties;
import org.junit.Test;

/**
 * Tests that {@link AttributeOverride} and {@link AssociationOverride} are taken into account.
 *
 * @author Tobias Liefke
 */
public class OverridesTest extends AbstractEntitySqlGeneratorTest {

	/**
	 * Tests to write an entity with different {@link AttributeOverride}s and {@link AssociationOverride}s.
	 *
	 * @throws Exception
	 *             if Hibernate or the generator throws one
	 */
	@Test
	public void testOverrides() throws Exception {
		final OverrideEntity entity = new OverrideEntity();
		entity.setSimpleProperty("Test attribute override");

		final OverrideEntity otherEntity = new OverrideEntity();
		otherEntity.setSimpleProperty("Test association override");
		entity.setOtherEntity(otherEntity);

		entity.setEmbedded(new TestEmbeddedProperties("Test embedded attribute override",
				new SimpleTestEntity("Test embedded association override")));

		write(entity);

		final OverrideEntity result = super.findSingleResult(
				"SELECT e FROM OverrideEntity e where e.otherEntity IS NOT NULL", OverrideEntity.class);
		assertThat(result.getSimpleProperty()).isEqualTo(entity.getSimpleProperty());

		assertThat(result.getOtherEntity()).isNotNull();
		assertThat(result.getOtherEntity().getSimpleProperty()).isEqualTo(otherEntity.getSimpleProperty());

		assertThat(result.getEmbedded().getDescription()).isEqualTo(entity.getEmbedded().getDescription());
		assertThat(result.getEmbedded().getOtherNode().getName())
				.isEqualTo(entity.getEmbedded().getOtherNode().getName());
	}

}
