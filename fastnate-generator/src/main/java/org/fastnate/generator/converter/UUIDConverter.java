package org.fastnate.generator.converter;

import java.util.UUID;

import org.fastnate.generator.context.GeneratorContext;

/**
 * TODO Describe this class
 *
 * @author mdykman
 */
public class UUIDConverter extends AbstractValueConverter<UUID> {

	/**
	 * Creates a new instance of {@link UUIDConverter}.
	 */
	public UUIDConverter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO Describe why this method is overridden.
	 */
	@Override
	public String getExpression(final UUID value, final GeneratorContext context) {
		return context.getDialect().createUUIDExpression(value);
	}

}
