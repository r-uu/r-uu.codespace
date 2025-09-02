package de.ruu.lib.jpa.core.mapstruct.demo.tree;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TestMapstructWithLombokForBinaryTree
{
	@Test void createBinTreeWithSingleNodeForDTOs()
	{
		NodeDTO root = new NodeDTO("root");

		assertThat(root.children().isEmpty(), is(true));
	}
}