module de.ruu.app.jeeeraaah.client.fx
{
	exports de.ruu.app.jeeeraaah.client.fx;
	exports de.ruu.app.jeeeraaah.client.fx.dash;
	exports de.ruu.app.jeeeraaah.client.fx.task;
	exports de.ruu.app.jeeeraaah.client.fx.task.edit;
	exports de.ruu.app.jeeeraaah.client.fx.task.gantt;
	exports de.ruu.app.jeeeraaah.client.fx.task.view;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.directneighbours;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.super_sub_or_predecessor;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.super_sub_or_successor;
	exports de.ruu.app.jeeeraaah.client.fx.task.view.list.directneighbours;
	exports de.ruu.app.jeeeraaah.client.fx.task.selector;
	exports de.ruu.app.jeeeraaah.client.fx.taskgroup;
	exports de.ruu.app.jeeeraaah.client.fx.taskgroup.edit;
	exports de.ruu.app.jeeeraaah.client.fx.taskgroup.selector;
  exports de.ruu.app.jeeeraaah.client.fx.test;

	opens de.ruu.app.jeeeraaah.client.fx;
	opens de.ruu.app.jeeeraaah.client.fx.dash;
	opens de.ruu.app.jeeeraaah.client.fx.task;
	opens de.ruu.app.jeeeraaah.client.fx.task.edit;
	opens de.ruu.app.jeeeraaah.client.fx.task.gantt;
	opens de.ruu.app.jeeeraaah.client.fx.task.view;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.directneighbours;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.list.directneighbours;
	opens de.ruu.app.jeeeraaah.client.fx.task.selector;
	opens de.ruu.app.jeeeraaah.client.fx.taskgroup;
	opens de.ruu.app.jeeeraaah.client.fx.taskgroup.edit;
	opens de.ruu.app.jeeeraaah.client.fx.taskgroup.selector;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.super_sub_or_predecessor;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add;
	opens de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.super_sub_or_successor;

	requires de.ruu.lib.cdi.common;
	requires de.ruu.lib.cdi.se;
  requires de.ruu.lib.fx.comp;
	requires de.ruu.lib.fx.core;
	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;
	requires de.ruu.lib.util;

	requires de.ruu.app.jeeeraaah.client.rs;
	requires de.ruu.app.jeeeraaah.common;

	requires jakarta.activation;
	requires javafx.fxml;
	requires javafx.controls;
	requires org.mapstruct;

	requires static lombok;
	requires org.slf4j;
	requires jakarta.validation;
	requires javafx.graphics;
	requires jakarta.inject;
	requires jakarta.ws.rs;
}