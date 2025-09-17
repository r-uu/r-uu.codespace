package de.ruu.app.jeeeraaah.common.api.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RemoveNeighboursFromTaskConfigTest
{
	@Test void constructorCopiesSets_andSetsAllFlags()
	{
		RemoveNeighboursFromTaskConfig cfg = new RemoveNeighboursFromTaskConfig(
				42L,
				true,
				Set.of(1L, 2L),
				Set.of(3L),
				Set.of(4L, 5L)
		);

		assertThat(cfg.idTask(), is(42L));
		assertThat(cfg.removeFromSuperTask(), is(true));
		assertThat(cfg.removeFromPredecessors(), containsInAnyOrder(1L, 2L));
		assertThat(cfg.removeFromSubTasks(), contains(3L));
		assertThat(cfg.removeFromSuccessors(), containsInAnyOrder(4L, 5L));

		// mutating the original sets does not affect config (defensive copy assertion)
		Set<Long> original = Set.of(9L);
		RemoveNeighboursFromTaskConfig cfg2 = new RemoveNeighboursFromTaskConfig(7L, false, original, original, original);
		assertThat(cfg2.removeFromPredecessors(), contains(9L));
		assertThat(cfg2.removeFromSubTasks(), contains(9L));
		assertThat(cfg2.removeFromSuccessors(), contains(9L));
	}

	@Test void defaultNoArgs_isNotRemovingFromSuperTask_andHasEmptySets()
	{
		RemoveNeighboursFromTaskConfig cfg = new RemoveNeighboursFromTaskConfig();
		// idTask is @NonNull but not set by default; not asserting it here
		assertThat(cfg.removeFromSuperTask(), is(false));
		assertThat(cfg.removeFromPredecessors(), empty());
		assertThat(cfg.removeFromSubTasks(), empty());
		assertThat(cfg.removeFromSuccessors(), empty());

		// setting flag should be reflected
		cfg.removeFromSuperTask(true);
		assertThat(cfg.removeFromSuperTask(), is(true));
	}
}
