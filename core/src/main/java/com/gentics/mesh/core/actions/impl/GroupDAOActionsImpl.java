package com.gentics.mesh.core.actions.impl;

import java.util.function.Predicate;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.gentics.mesh.context.BulkActionContext;
import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.action.GroupDAOActions;
import com.gentics.mesh.core.data.Group;
import com.gentics.mesh.core.data.dao.GroupDaoWrapper;
import com.gentics.mesh.core.data.page.Page;
import com.gentics.mesh.core.data.page.TransformablePage;
import com.gentics.mesh.core.data.relationship.GraphPermission;
import com.gentics.mesh.core.db.Tx;
import com.gentics.mesh.core.rest.group.GroupResponse;
import com.gentics.mesh.event.EventQueueBatch;
import com.gentics.mesh.parameter.PagingParameters;

@Singleton
public class GroupDAOActionsImpl implements GroupDAOActions {

	@Inject
	public GroupDAOActionsImpl() {
	}

	@Override
	public Group loadByUuid(Tx tx, InternalActionContext ac, String uuid, GraphPermission perm, boolean errorIfNotFound) {
		GroupDaoWrapper groupDao = tx.data().groupDao();
		if (perm == null) {
			return groupDao.findByUuid(uuid);
		} else {
			return groupDao.loadObjectByUuid(ac, uuid, perm, errorIfNotFound);
		}
	}

	@Override
	public Group loadByName(Tx tx, InternalActionContext ac, String name, GraphPermission perm, boolean errorIfNotFound) {
		GroupDaoWrapper groupDao = tx.data().groupDao();
		if (perm == null) {
			return groupDao.findByName(name);
		} else {
			throw new RuntimeException("Not supported");
		}
	}

	@Override
	public TransformablePage<? extends Group> loadAll(Tx tx, InternalActionContext ac, PagingParameters pagingInfo) {
		return tx.data().groupDao().findAll(ac, pagingInfo);
	}

	@Override
	public Page<? extends Group> loadAll(Tx tx, InternalActionContext ac, PagingParameters pagingInfo, Predicate<Group> extraFilter) {
		return tx.data().groupDao().findAll(ac, pagingInfo, extraFilter);
	}

	@Override
	public Group create(Tx tx, InternalActionContext ac, EventQueueBatch batch, String uuid) {
		return tx.data().groupDao().create(ac, batch, uuid);
	}

	@Override
	public void delete(Tx tx, Group group, BulkActionContext bac) {
		tx.data().groupDao().delete(group, bac);
	}

	@Override
	public boolean update(Tx tx, Group group, InternalActionContext ac, EventQueueBatch batch) {
		return tx.data().groupDao().update(group, ac, batch);
	}

	@Override
	public GroupResponse transformToRestSync(Tx tx, Group group, InternalActionContext ac, int level, String... languageTags) {
		return tx.data().groupDao().transformToRestSync(group, ac, level, languageTags);
	}

	@Override
	public String getAPIPath(Tx tx, InternalActionContext ac, Group group) {
		return group.getAPIPath(ac);
	}

	@Override
	public String getETag(Tx tx, InternalActionContext ac, Group group) {
		return group.getETag(ac);
	}
}