package com.gentics.mesh.core.data.dao.impl;

import static com.gentics.mesh.core.data.relationship.GraphPermission.CREATE_PERM;
import static com.gentics.mesh.core.rest.error.Errors.conflict;
import static com.gentics.mesh.core.rest.error.Errors.error;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.gentics.mesh.cli.BootstrapInitializer;
import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.data.MeshVertex;
import com.gentics.mesh.core.data.Project;
import com.gentics.mesh.core.data.TagFamily;
import com.gentics.mesh.core.data.dao.AbstractDaoWrapper;
import com.gentics.mesh.core.data.dao.TagFamilyDaoWrapper;
import com.gentics.mesh.core.data.dao.UserDaoWrapper;
import com.gentics.mesh.core.data.generic.PermissionProperties;
import com.gentics.mesh.core.data.impl.TagFamilyWrapper;
import com.gentics.mesh.core.data.root.TagFamilyRoot;
import com.gentics.mesh.core.data.user.MeshAuthUser;
import com.gentics.mesh.core.rest.common.GenericRestResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyCreateRequest;
import com.gentics.mesh.core.rest.tag.TagFamilyResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyUpdateRequest;
import com.gentics.mesh.event.EventQueueBatch;
import com.gentics.mesh.madl.traversal.TraversalResult;
import com.gentics.mesh.parameter.GenericParameters;
import com.gentics.mesh.parameter.value.FieldsSet;

import dagger.Lazy;

// TODO there is no tag family root since the tag itself is the root. 
public class TagFamilyDaoWrapperImpl extends AbstractDaoWrapper implements TagFamilyDaoWrapper {

	@Inject
	public TagFamilyDaoWrapperImpl(Lazy<BootstrapInitializer> boot, Lazy<PermissionProperties> permissions) {
		super(boot, permissions);
	}

	@Override
	public TraversalResult<? extends TagFamily> findAllGlobal() {
		return boot.get().tagFamilyRoot().findAll();
	}

	@Override
	public long computeGlobalCount() {
		return boot.get().tagFamilyRoot().computeCount();
	}

	@Override
	public TagFamilyWrapper findByName(Project project, String name) {
		TagFamilyRoot root = project.getTagFamilyRoot();
		TagFamily tagFamily = root.findByName(name);
		return TagFamilyWrapper.wrap(tagFamily);
	}

	@Override
	public TagFamilyWrapper findByUuid(Project project, String uuid) {
		TagFamilyRoot root = project.getTagFamilyRoot();
		TagFamily tagFamily = root.findByUuid(uuid);
		return TagFamilyWrapper.wrap(tagFamily);
	}

	@Override
	public TagFamily findByUuidGlobal(String uuid) {
		TagFamilyRoot globalTagFamilyRoot = boot.get().tagFamilyRoot();
		return globalTagFamilyRoot.findByUuid(uuid);
	}

	@Override
	public TagFamilyResponse transformToRestSync(TagFamily tagFamily, InternalActionContext ac, int level, String... languageTags) {
		GenericParameters generic = ac.getGenericParameters();
		FieldsSet fields = generic.getFields();

		TagFamilyResponse restTagFamily = new TagFamilyResponse();
		if (fields.has("uuid")) {
			restTagFamily.setUuid(tagFamily.getUuid());

			// Performance shortcut to return now and ignore the other checks
			if (fields.size() == 1) {
				return restTagFamily;
			}
		}

		if (fields.has("name")) {
			restTagFamily.setName(tagFamily.getName());
		}

		tagFamily.fillCommonRestFields(ac, fields, restTagFamily);

		if (fields.has("perms")) {
			setRolePermissions(tagFamily, ac, restTagFamily);
		}

		return restTagFamily;

	}

	public void setRolePermissions(MeshVertex vertex, InternalActionContext ac, GenericRestResponse model) {
		model.setRolePerms(permissions.get().getRolePermissions(vertex, ac, ac.getRolePermissionParameters().getRoleUuid()));
	}

	@Override
	public boolean update(TagFamily tagFamily, InternalActionContext ac, EventQueueBatch batch) {
		TagFamilyUpdateRequest requestModel = ac.fromJson(TagFamilyUpdateRequest.class);
		Project project = ac.getProject();
		String newName = requestModel.getName();

		if (isEmpty(newName)) {
			throw error(BAD_REQUEST, "tagfamily_name_not_set");
		}

		TagFamily tagFamilyWithSameName = project.getTagFamilyRoot().findByName(newName);
		if (tagFamilyWithSameName != null && !tagFamilyWithSameName.getUuid().equals(tagFamily.getUuid())) {
			throw conflict(tagFamilyWithSameName.getUuid(), newName, "tagfamily_conflicting_name", newName);
		}
		if (!tagFamily.getName().equals(newName)) {
			tagFamily.setName(newName);
			batch.add(tagFamily.onUpdated());
			return true;
		}
		return false;
	}

	@Override
	public TagFamily create(Project project, InternalActionContext ac, EventQueueBatch batch, String uuid) {
		MeshAuthUser requestUser = ac.getUser();
		UserDaoWrapper userDao = boot.get().userDao();
		TagFamilyCreateRequest requestModel = ac.fromJson(TagFamilyCreateRequest.class);

		String name = requestModel.getName();
		if (StringUtils.isEmpty(name)) {
			throw error(BAD_REQUEST, "tagfamily_name_not_set");
		}
		TagFamilyRoot projectTagFamilyRoot = project.getTagFamilyRoot();

		// Check whether the name is already in-use.
		TagFamily conflictingTagFamily = projectTagFamilyRoot.findByName(name);
		if (conflictingTagFamily != null) {
			throw conflict(conflictingTagFamily.getUuid(), name, "tagfamily_conflicting_name", name);
		}

		if (!userDao.hasPermission(requestUser, projectTagFamilyRoot, CREATE_PERM)) {
			throw error(FORBIDDEN, "error_missing_perm", projectTagFamilyRoot.getUuid(), CREATE_PERM.getRestPerm().getName());
		}
		TagFamily tagFamily = projectTagFamilyRoot.create(name, requestUser, uuid);
		projectTagFamilyRoot.addTagFamily(tagFamily);
		userDao.inheritRolePermissions(requestUser, projectTagFamilyRoot, tagFamily);

		batch.add(tagFamily.onCreated());
		return tagFamily;
	}

	@Override
	public TraversalResult<? extends TagFamily> findAll(Project project) {
		return project.getTagFamilyRoot().findAll();
	}

}