package com.gentics.mesh.core.data.dao.impl;

import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.gentics.madl.traversal.RawTraversalResult;
import com.gentics.mesh.context.BulkActionContext;
import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.data.MeshVertex;
import com.gentics.mesh.core.data.Project;
import com.gentics.mesh.core.data.Role;
import com.gentics.mesh.core.data.TagFamily;
import com.gentics.mesh.core.data.User;
import com.gentics.mesh.core.data.dao.TagFamilyDaoWrapper;
import com.gentics.mesh.core.data.page.TransformablePage;
import com.gentics.mesh.core.data.relationship.GraphPermission;
import com.gentics.mesh.core.data.root.TagFamilyRoot;
import com.gentics.mesh.core.rest.common.PermissionInfo;
import com.gentics.mesh.core.rest.tag.TagFamilyResponse;
import com.gentics.mesh.etc.config.MeshOptions;
import com.gentics.mesh.event.EventQueueBatch;
import com.gentics.mesh.graphdb.spi.Database;
import com.gentics.mesh.madl.frame.EdgeFrame;
import com.gentics.mesh.madl.frame.ElementFrame;
import com.gentics.mesh.madl.frame.VertexFrame;
import com.gentics.mesh.madl.tp3.mock.GraphTraversal;
import com.gentics.mesh.madl.traversal.TraversalResult;
import com.gentics.mesh.parameter.PagingParameters;
import com.google.gson.JsonObject;
import com.syncleus.ferma.ClassInitializer;
import com.syncleus.ferma.FramedGraph;
import com.syncleus.ferma.TEdge;
import com.syncleus.ferma.traversals.EdgeTraversal;
import com.syncleus.ferma.traversals.VertexTraversal;
import com.tinkerpop.blueprints.Vertex;

import io.reactivex.Single;
import io.vertx.core.Vertx;

// TODO there is no tag family root since the tag itself is the root. 
public class TagFamilyDaoWrapperImpl implements TagFamilyDaoWrapper {

	private final TagFamilyRoot delegate;

	public TagFamilyDaoWrapperImpl(TagFamilyRoot delegate) {
		this.delegate = delegate;
	}

	public Object id() {
		return delegate.id();
	}

	public PermissionInfo getRolePermissions(InternalActionContext ac, String roleUuid) {
		return delegate.getRolePermissions(ac, roleUuid);
	}

	public void setUuid(String uuid) {
		delegate.setUuid(uuid);
	}

	public String getAPIPath(TagFamily element, InternalActionContext ac) {
		return delegate.getAPIPath(element, ac);
	}

	public void setUniqueLinkOutTo(VertexFrame vertex, String... labels) {
		delegate.setUniqueLinkOutTo(vertex, labels);
	}

	public TagFamily create(String name, User user) {
		return delegate.create(name, user);
	}

	public TraversalResult<? extends Role> getRolesWithPerm(GraphPermission perm) {
		return delegate.getRolesWithPerm(perm);
	}

	public String getUuid() {
		return delegate.getUuid();
	}

	public Single<TagFamilyResponse> transformToRest(TagFamily element, InternalActionContext ac, int level, String... languageTags) {
		return delegate.transformToRest(element, ac, level, languageTags);
	}

	public Vertex getVertex() {
		return delegate.getVertex();
	}

	public String getElementVersion() {
		return delegate.getElementVersion();
	}

	public void setUniqueLinkInTo(VertexFrame vertex, String... labels) {
		delegate.setUniqueLinkInTo(vertex, labels);
	}

	public <T> T property(String name) {
		return delegate.property(name);
	}

	public TagFamily create(String name, User user, String uuid) {
		return delegate.create(name, user, uuid);
	}

	public void delete(BulkActionContext bac) {
		delegate.delete(bac);
	}

	public Vertex getElement() {
		return delegate.getElement();
	}

	public void setSingleLinkOutTo(VertexFrame vertex, String... labels) {
		delegate.setSingleLinkOutTo(vertex, labels);
	}

	public Object getId() {
		return delegate.getId();
	}

	public <T> T addFramedEdge(String label, com.syncleus.ferma.VertexFrame inVertex, ClassInitializer<T> initializer) {
		return delegate.addFramedEdge(label, inVertex, initializer);
	}

	public void setSingleLinkInTo(VertexFrame vertex, String... labels) {
		delegate.setSingleLinkInTo(vertex, labels);
	}

	public Set<String> getPropertyKeys() {
		return delegate.getPropertyKeys();
	}

	public void addToStringSetProperty(String propertyKey, String value) {
		delegate.addToStringSetProperty(propertyKey, value);
	}

	public VertexTraversal<?, ?, ?> out(String... labels) {
		return delegate.out(labels);
	}

	public void remove() {
		delegate.remove();
	}

	public void delete() {
		delegate.delete();
	}

	public TagFamilyResponse transformToRestSync(TagFamily element, InternalActionContext ac, int level, String... languageTags) {
		return delegate.transformToRestSync(element, ac, level, languageTags);
	}

	public <T extends ElementFrame> TraversalResult<? extends T> out(String label, Class<T> clazz) {
		return delegate.out(label, clazz);
	}

	public FramedGraph getGraph() {
		return delegate.getGraph();
	}

	public <R> void property(String key, R value) {
		delegate.property(key, value);
	}

	public void applyPermissions(EventQueueBatch batch, Role role, boolean recursive, Set<GraphPermission> permissionsToGrant,
		Set<GraphPermission> permissionsToRevoke) {
		delegate.applyPermissions(batch, role, recursive, permissionsToGrant, permissionsToRevoke);
	}

	public void removeTagFamily(TagFamily tagFamily) {
		delegate.removeTagFamily(tagFamily);
	}

	public <T extends EdgeFrame> TraversalResult<? extends T> outE(String label, Class<T> clazz) {
		return delegate.outE(label, clazz);
	}

	public <T> T getProperty(String name) {
		return delegate.getProperty(name);
	}

	public <T extends ElementFrame> TraversalResult<? extends T> in(String label, Class<T> clazz) {
		return delegate.in(label, clazz);
	}

	public <T> T addFramedEdge(String label, com.syncleus.ferma.VertexFrame inVertex, Class<T> kind) {
		return delegate.addFramedEdge(label, inVertex, kind);
	}

	public void addTagFamily(TagFamily tagFamily) {
		delegate.addTagFamily(tagFamily);
	}

	public void removeProperty(String key) {
		delegate.removeProperty(key);
	}

	public <T extends EdgeFrame> TraversalResult<? extends T> inE(String label, Class<T> clazz) {
		return delegate.inE(label, clazz);
	}

	public Project getProject() {
		return delegate.getProject();
	}

	public <T extends RawTraversalResult<?>> T traverse(Function<GraphTraversal<Vertex, Vertex>, GraphTraversal<?, ?>> traverser) {
		return delegate.traverse(traverser);
	}

	public <T> T getProperty(String name, Class<T> type) {
		return delegate.getProperty(name, type);
	}

	public Database db() {
		return delegate.db();
	}

	public String getETag(TagFamily element, InternalActionContext ac) {
		return delegate.getETag(element, ac);
	}

	public Vertx vertx() {
		return delegate.vertx();
	}

	public boolean hasPublishPermissions() {
		return delegate.hasPublishPermissions();
	}

	public MeshOptions options() {
		return delegate.options();
	}

	public <T> T addFramedEdgeExplicit(String label, com.syncleus.ferma.VertexFrame inVertex, ClassInitializer<T> initializer) {
		return delegate.addFramedEdgeExplicit(label, inVertex, initializer);
	}

	public void setCachedUuid(String uuid) {
		delegate.setCachedUuid(uuid);
	}

	public void setProperty(String name, Object value) {
		delegate.setProperty(name, value);
	}

	public TraversalResult<? extends TagFamily> findAll() {
		return delegate.findAll();
	}

	public Class<?> getTypeResolution() {
		return delegate.getTypeResolution();
	}

	public void setTypeResolution(Class<?> type) {
		delegate.setTypeResolution(type);
	}

	public Stream<? extends TagFamily> findAllStream(InternalActionContext ac, GraphPermission permission) {
		return delegate.findAllStream(ac, permission);
	}

	public <T> T addFramedEdgeExplicit(String label, com.syncleus.ferma.VertexFrame inVertex, Class<T> kind) {
		return delegate.addFramedEdgeExplicit(label, inVertex, kind);
	}

	public void removeTypeResolution() {
		delegate.removeTypeResolution();
	}

	public VertexTraversal<?, ?, ?> v() {
		return delegate.v();
	}

	public EdgeTraversal<?, ?, ?> e() {
		return delegate.e();
	}

	public EdgeTraversal<?, ?, ?> e(Object... ids) {
		return delegate.e(ids);
	}

	public TEdge addFramedEdge(String label, com.syncleus.ferma.VertexFrame inVertex) {
		return delegate.addFramedEdge(label, inVertex);
	}

	public <T> T getGraphAttribute(String key) {
		return delegate.getGraphAttribute(key);
	}

	public TraversalResult<? extends TagFamily> findAllDynamic() {
		return delegate.findAllDynamic();
	}

	public VertexTraversal<?, ?, ?> in(String... labels) {
		return delegate.in(labels);
	}

	public EdgeTraversal<?, ?, ?> outE(String... labels) {
		return delegate.outE(labels);
	}

	public EdgeTraversal<?, ?, ?> inE(String... labels) {
		return delegate.inE(labels);
	}

	public void linkOut(com.syncleus.ferma.VertexFrame vertex, String... labels) {
		delegate.linkOut(vertex, labels);
	}

	public TransformablePage<? extends TagFamily> findAll(InternalActionContext ac, PagingParameters pagingInfo) {
		return delegate.findAll(ac, pagingInfo);
	}

	public void linkIn(com.syncleus.ferma.VertexFrame vertex, String... labels) {
		delegate.linkIn(vertex, labels);
	}

	public TransformablePage<? extends TagFamily> findAll(InternalActionContext ac, PagingParameters pagingInfo, Predicate<TagFamily> extraFilter) {
		return delegate.findAll(ac, pagingInfo, extraFilter);
	}

	public void unlinkOut(com.syncleus.ferma.VertexFrame vertex, String... labels) {
		delegate.unlinkOut(vertex, labels);
	}

	public void unlinkIn(com.syncleus.ferma.VertexFrame vertex, String... labels) {
		delegate.unlinkIn(vertex, labels);
	}

	public TransformablePage<? extends TagFamily> findAllNoPerm(InternalActionContext ac, PagingParameters pagingInfo) {
		return delegate.findAllNoPerm(ac, pagingInfo);
	}

	public void setLinkOut(com.syncleus.ferma.VertexFrame vertex, String... labels) {
		delegate.setLinkOut(vertex, labels);
	}

	public VertexTraversal<?, ?, ?> traversal() {
		return delegate.traversal();
	}

	public TagFamily findByName(String name) {
		return delegate.findByName(name);
	}

	public JsonObject toJson() {
		return delegate.toJson();
	}

	public <T> T reframe(Class<T> kind) {
		return delegate.reframe(kind);
	}

	public TagFamily findByName(InternalActionContext ac, String name, GraphPermission perm) {
		return delegate.findByName(ac, name, perm);
	}

	public <T> T reframeExplicit(Class<T> kind) {
		return delegate.reframeExplicit(kind);
	}

	public TagFamily findByUuid(String uuid) {
		return delegate.findByUuid(uuid);
	}

	public TagFamily loadObjectByUuid(InternalActionContext ac, String uuid, GraphPermission perm) {
		return delegate.loadObjectByUuid(ac, uuid, perm);
	}

	public TagFamily loadObjectByUuid(InternalActionContext ac, String uuid, GraphPermission perm, boolean errorIfNotFound) {
		return delegate.loadObjectByUuid(ac, uuid, perm, errorIfNotFound);
	}

	public TagFamily loadObjectByUuidNoPerm(String uuid, boolean errorIfNotFound) {
		return delegate.loadObjectByUuidNoPerm(uuid, errorIfNotFound);
	}

	public MeshVertex resolveToElement(Stack<String> stack) {
		return delegate.resolveToElement(stack);
	}

	public TagFamily create(InternalActionContext ac, EventQueueBatch batch) {
		return delegate.create(ac, batch);
	}

	public TagFamily create(InternalActionContext ac, EventQueueBatch batch, String uuid) {
		return delegate.create(ac, batch, uuid);
	}

	public void addItem(TagFamily item) {
		delegate.addItem(item);
	}

	public void removeItem(TagFamily item) {
		delegate.removeItem(item);
	}

	public String getRootLabel() {
		return delegate.getRootLabel();
	}

	public Class<? extends TagFamily> getPersistanceClass() {
		return delegate.getPersistanceClass();
	}

	public long computeCount() {
		return delegate.computeCount();
	}

	public PermissionInfo getRolePermissions(MeshVertex vertex, InternalActionContext ac, String roleUuid) {
		return delegate.getRolePermissions(vertex, ac, roleUuid);
	}

	public TraversalResult<? extends Role> getRolesWithPerm(MeshVertex vertex, GraphPermission perm) {
		return delegate.getRolesWithPerm(vertex, perm);
	}

	public void delete(TagFamily element, BulkActionContext bac) {
		delegate.delete(element, bac);
	}

}
