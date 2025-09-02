package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.ActionAdd.Context;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.super_sub_or_successor.Configurator;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.super_sub_or_successor.ConfiguratorService.ActionAddToSuperSubOrSuccessorConfigurationResult;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import jakarta.enterprise.inject.spi.CDI;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;
import lombok.NonNull;

import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * <code>ActionAddToSuperSubOrSuccessor</code> is responsible for adding a task as a new predecessor task to either
 * the selected predecessor or the selected super/sub task.
 * <p>
 * It creates a dialog for the user to choose the receiver of the predecessor task, and upon confirmation, it creates a
 * new assignment between the receiver and its predecessor in the database.
 */
class ActionAddToSuperSubOrSuccessor
{
	private final Context context;

	private final Configurator configurator = CDI.current().select(Configurator.class).get();

	ActionAddToSuperSubOrSuccessor(@NonNull Context context) { this.context = context; }

	void execute()
	{
		Parent configuratorLocalRoot = configurator.localRoot();// ensure that the configurator is initialized
		configurator.service().context(context);

		Dialog<ActionAddToSuperSubOrSuccessorConfigurationResult> dialog = new Dialog<>();
		dialog.setTitle("choose target task (super/sub or successor for new successor relation");
		dialog.setResultConverter(this::dialogResultConverter);
		dialog.setResizable(true);

		DialogPane pane = dialog.getDialogPane();
		pane.setContent(configuratorLocalRoot);
		pane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		pane.getButtonTypes().addAll(CANCEL, OK);

		dialog.setOnShown(e -> pane.getScene().getWindow().sizeToScene()); // make sure window resizes correctly
		Optional<ActionAddToSuperSubOrSuccessorConfigurationResult> optional = dialog.showAndWait();

		optional.ifPresent
		(
				configurationResult ->
				{
					context.taskServiceClient().addSuccessor
					(
							configurationResult.targetTask               ().toDTO(new ReferenceCycleTracking()),
							configurationResult.chosenSuccessorTaskBean().toDTO(new ReferenceCycleTracking())
					);
					TreeItem<TaskBean> newItemInSuccessorTree = new TreeItem<>(configurationResult.chosenSuccessorTaskBean());
					context.treeItemSelectedSuccessorTask().getChildren().add(newItemInSuccessorTree);
					populateTreeForSuccessorsOf(newItemInSuccessorTree);
					// add the chosen predecessor task to the predecessor tasks of the selected super/sub task
					context
							.treeItemSelectedSuperSubTask().getValue().addPredecessor(configurationResult.chosenSuccessorTaskBean());
				}
		);
	}

	private void populateTreeForSuccessorsOf(TreeItem<TaskBean> newItemInSuccessorTree)
	{
		TaskBean predecessorTask = newItemInSuccessorTree.getValue();

		predecessorTask.predecessors().ifPresent
				(
						predecessors ->
								predecessors.forEach
										(
												predecessor ->
												{
													TreeItem<TaskBean> predecessorOfNewItemInSuccessorTree = new TreeItem<>(predecessor);
													newItemInSuccessorTree.getChildren().add(predecessorOfNewItemInSuccessorTree);
													populateTreeForSuccessorsOf(predecessorOfNewItemInSuccessorTree);
												}
										)
				);
	}

	private ActionAddToSuperSubOrSuccessorConfigurationResult dialogResultConverter(ButtonType btn)
	{
		if (btn.getButtonData() == OK_DONE)
				if (configurator.service().result().isPresent())
						return new ActionAddToSuperSubOrSuccessorConfigurationResult
						(
								configurator.service().result().get().chosenSuccessorTaskBean(),
								configurator.service().result().get().targetTask()
						);
		return null;
	}
}