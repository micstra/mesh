package com.gentics.mesh.search;

import static com.gentics.mesh.util.VerticleHelper.fail;
import static io.vertx.core.http.HttpMethod.POST;

import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.mesh.core.AbstractCoreApiVerticle;
import com.gentics.mesh.core.data.GenericVertex;
import com.gentics.mesh.core.data.root.RootVertex;
import com.gentics.mesh.core.rest.common.AbstractListResponse;
import com.gentics.mesh.core.rest.common.RestModel;
import com.gentics.mesh.core.rest.group.GroupListResponse;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.project.ProjectListResponse;
import com.gentics.mesh.core.rest.role.RoleListResponse;
import com.gentics.mesh.core.rest.schema.MicroschemaListResponse;
import com.gentics.mesh.core.rest.schema.SchemaListResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyListResponse;
import com.gentics.mesh.core.rest.tag.TagListResponse;
import com.gentics.mesh.core.rest.user.UserListResponse;
import com.gentics.mesh.graphdb.Trx;
import com.gentics.mesh.graphdb.spi.Database;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Route;

@Component
@Scope("singleton")
@SpringVerticle
public class SearchVerticle extends AbstractCoreApiVerticle {

	private static final Logger log = LoggerFactory.getLogger(SearchVerticle.class);

	@Autowired
	private SearchHandler searchHandler;

	public SearchVerticle() {
		super("search");
	}

	@Override
	public void registerEndPoints() throws Exception {
		route("/*").handler(springConfiguration.authHandler());
		addSearchEndpoints();
	}

	private void addSearchEndpoints() {
		try (Trx tx = db.trx()) {
			addSearch("users", boot.userRoot(), UserListResponse.class);
			addSearch("groups", boot.groupRoot(), GroupListResponse.class);
			addSearch("role", boot.roleRoot(), RoleListResponse.class);
			addSearch("nodes", boot.nodeRoot(), NodeListResponse.class);
			addSearch("tags", boot.tagRoot(), TagListResponse.class);
			addSearch("tagFamilies", boot.tagFamilyRoot(), TagFamilyListResponse.class);
			addSearch("projects", boot.projectRoot(), ProjectListResponse.class);
			addSearch("schemas", boot.schemaContainerRoot(), SchemaListResponse.class);
			addSearch("microschemas", boot.microschemaContainerRoot(), MicroschemaListResponse.class);
		}
	}

	private <T extends GenericVertex<TR>, TR extends RestModel, RL extends AbstractListResponse<TR>> void addSearch(String typeName,
			RootVertex<T> root, Class<RL> classOfRL) {
		Route postRoute = route("/" + typeName).method(POST).consumes(APPLICATION_JSON).produces(APPLICATION_JSON);
		postRoute.handler(rc -> {
			try {
				searchHandler.handleSearch(rc, root, classOfRL);
			} catch (Exception e) {
				fail(rc, "search_error_query");
			}
		});
	}

}
