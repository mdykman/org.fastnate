package org.fastnate.generator.context;

import java.util.ArrayList;
import java.util.List;

import org.fastnate.generator.statements.EntityStatement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Encapsulates the state of a generated entity.
 *
 * @author Tobias Liefke
 */
// CHECKSTYLE OFF: FinalClass
public class GenerationState {

	/**
	 * Marks all pending updates for an entity, that was not written up to now.
	 */
	@Getter
	@RequiredArgsConstructor
	public static final class PendingState extends GenerationState {

		@Getter
		@RequiredArgsConstructor
		private static final class Update<E> {

			private final E entity;

			private final Property<E, ?> property;

			private final Object[] arguments;
		}

		private final List<Update<?>> updates = new ArrayList<>();

		/**
		 * Adds an entity that has to be updated, as soon as the associated entity (of this state) was written.
		 *
		 * @param entity
		 *            the entity to update
		 * @param property
		 *            the property to write
		 * @param arguments
		 *            additional arguments to remember
		 */
		public <E> void addPendingUpdate(final E entity, final Property<E, ?> property, final Object... arguments) {
			this.updates.add(new Update<>(entity, property, arguments));
		}

		/**
		 * Generates the update statements for all entities that are required after the given entity was generated.
		 *
		 * @param entity
		 *            the entity that exists now in the database
		 * @return the list of pending statements
		 */
		public <E> List<EntityStatement> generatePendingStatements(final Object entity) {
			final List<EntityStatement> result = new ArrayList<>();
			for (final Update<?> update : this.updates) {
				final Update<E> singleUpdate = (Update<E>) update;
				result.addAll(singleUpdate.getProperty().generatePendingStatements(singleUpdate.getEntity(), entity,
						singleUpdate.getArguments()));
			}
			return result;
		}

	}

	/** Marker for an entity, that was written already. */
	public static final GenerationState PERSISTED = new GenerationState();

	private GenerationState() {
		// Empty constructor to prevent states other than the three defined
	}

}
