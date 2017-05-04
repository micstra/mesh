package com.gentics.mesh.search;

import static com.gentics.mesh.test.TestDataProvider.PROJECT_NAME;
import static com.gentics.mesh.test.TestSize.FULL;
import static com.gentics.mesh.test.context.MeshTestHelper.call;
import static com.gentics.mesh.test.context.MeshTestHelper.getSimpleQuery;
import static com.gentics.mesh.util.MeshAssert.failingLatch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.gentics.mesh.FieldUtil;
import com.gentics.mesh.core.rest.micronode.MicronodeResponse;
import com.gentics.mesh.core.rest.microschema.impl.MicroschemaCreateRequest;
import com.gentics.mesh.core.rest.microschema.impl.MicroschemaResponse;
import com.gentics.mesh.core.rest.node.NodeCreateRequest;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.core.rest.node.NodeUpdateRequest;
import com.gentics.mesh.core.rest.node.VersionReference;
import com.gentics.mesh.core.rest.release.ReleaseCreateRequest;
import com.gentics.mesh.core.rest.schema.MicroschemaReference;
import com.gentics.mesh.core.rest.schema.SchemaReference;
import com.gentics.mesh.core.rest.schema.impl.SchemaModelImpl;
import com.gentics.mesh.core.rest.schema.impl.SchemaUpdateRequest;
import com.gentics.mesh.core.rest.user.NodeReference;
import com.gentics.mesh.graphdb.NoTx;
import com.gentics.mesh.json.JsonUtil;
import com.gentics.mesh.parameter.impl.VersioningParametersImpl;
import com.gentics.mesh.test.context.MeshTestSetting;
import com.gentics.mesh.test.performance.TestUtils;

@MeshTestSetting(useElasticsearch = true, testSize = FULL, startServer = true)
public class NodeSearchEndpointGTest extends AbstractNodeSearchEndpointTest {

	@Test
	public void testSearchDraftNodes() throws Exception {
		try (NoTx noTx = db().noTx()) {
			recreateIndices();
		}

		String oldContent = "supersonic";
		String newContent = "urschnell";
		String uuid = db().noTx(() -> content("concorde").getUuid());
		NodeResponse concorde = call(() -> client().findNodeByUuid(PROJECT_NAME, uuid, new VersioningParametersImpl().draft()));

		NodeListResponse response = call(
				() -> client().searchNodes(PROJECT_NAME, getSimpleQuery(oldContent), new VersioningParametersImpl().draft()));
		assertThat(response.getData()).as("Search result").usingElementComparatorOnFields("uuid").containsOnly(concorde);

		response = call(() -> client().searchNodes(PROJECT_NAME, getSimpleQuery(newContent), new VersioningParametersImpl().draft()));
		assertThat(response.getData()).as("Search result").isEmpty();

		// change draft version of content
		NodeUpdateRequest update = new NodeUpdateRequest();
		update.setLanguage("en");
		update.getFields().put("content", FieldUtil.createHtmlField(newContent));
		update.setVersion(new VersionReference().setNumber("1.0"));
		call(() -> client().updateNode(PROJECT_NAME, concorde.getUuid(), update));

		response = call(() -> client().searchNodes(PROJECT_NAME, getSimpleQuery(oldContent), new VersioningParametersImpl().draft()));
		assertThat(response.getData()).as("Search result").isEmpty();

		response = call(() -> client().searchNodes(PROJECT_NAME, getSimpleQuery(newContent), new VersioningParametersImpl().draft()));
		assertThat(response.getData()).as("Search result").usingElementComparatorOnFields("uuid").containsOnly(concorde);
	}

	/**
	 * Test creating a microschema and adding it to the content schema. Assert that the search endpoint works as expected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMicronodeMigrationSearch() throws Exception {
		try (NoTx noTx = db().noTx()) {
			recreateIndices();
		}
		String contentUuid = db().noTx(() -> content().getUuid());
		String folderUuid = db().noTx(() -> folder("2015").getUuid());
		String schemaUuid = db().noTx(() -> schemaContainer("content").getUuid());
		SchemaUpdateRequest schemaUpdate = db()
				.noTx(() -> JsonUtil.readValue(schemaContainer("content").getLatestVersion().getJson(), SchemaUpdateRequest.class));

		// 1. Create the microschema
		MicroschemaCreateRequest microschemaRequest = new MicroschemaCreateRequest();
		microschemaRequest.setName("TestMicroschema");
		microschemaRequest.addField(FieldUtil.createStringFieldSchema("text"));
		microschemaRequest.addField(FieldUtil.createNodeFieldSchema("nodeRef").setAllowedSchemas("content"));
		MicroschemaResponse microschemaResponse = call(() -> client().createMicroschema(microschemaRequest));

		// Assign the microschema to the project
		call(() -> client().assignMicroschemaToProject(PROJECT_NAME, microschemaResponse.getUuid()));

		// 2. Add micronode field to content schema
		CountDownLatch latch = TestUtils.latchForMigrationCompleted(client());
		schemaUpdate.addField(FieldUtil.createMicronodeFieldSchema("micro").setAllowedMicroSchemas("TestMicroschema"));
		call(() -> client().updateSchema(schemaUuid, schemaUpdate));

		// 3. Search directly for existing content
		NodeListResponse response = call(
				() -> client().searchNodes(PROJECT_NAME, getSimpleQuery("supersonic"), new VersioningParametersImpl().draft()));
		// This is currently failing because the index handler uses the new index and not the old one to find the node. We need to check whether this behavioqr
		// is desired.
		assertThat(response.getData()).as("Search result").isEmpty();

		// 4. Wait until the migration is complete and search again
		failingLatch(latch);
		NodeListResponse response2 = call(
				() -> client().searchNodes(PROJECT_NAME, getSimpleQuery("supersonic"), new VersioningParametersImpl().draft()));
		assertThat(response2.getData()).as("Search result").isNotEmpty();

		// Finally lets create a new node that has a micronode
		NodeCreateRequest nodeCreateRequest = new NodeCreateRequest();
		nodeCreateRequest.setLanguage("en");
		nodeCreateRequest.setParentNode(new NodeReference().setUuid(folderUuid));
		nodeCreateRequest.setSchema(new SchemaReference().setName("content"));
		nodeCreateRequest.getFields().put("title", FieldUtil.createStringField("someTitle"));
		nodeCreateRequest.getFields().put("name", FieldUtil.createStringField("someName"));
		MicronodeResponse micronodeField = new MicronodeResponse();
		micronodeField.setMicroschema(new MicroschemaReference().setName("TestMicroschema"));
		micronodeField.getFields().put("text", FieldUtil.createStringField("someText"));
		micronodeField.getFields().put("nodeRef", FieldUtil.createNodeField(contentUuid));
		nodeCreateRequest.getFields().put("micro", micronodeField);
		NodeResponse nodeResponse = call(() -> client().createNode(PROJECT_NAME, nodeCreateRequest));
		assertEquals("someText", nodeResponse.getFields().getMicronodeField("micro").getFields().getStringField("text").getString());
	}

	@Test
	public void testSearchPublishedInRelease() throws Exception {
		try (NoTx noTx = db().noTx()) {
			recreateIndices();
		}

		String uuid = db().noTx(() -> content("concorde").getUuid());
		NodeResponse concorde = call(() -> client().findNodeByUuid(PROJECT_NAME, uuid, new VersioningParametersImpl().draft()));
		call(() -> client().publishNode(PROJECT_NAME, uuid));

		CountDownLatch latch = TestUtils.latchForMigrationCompleted(client());
		ReleaseCreateRequest createRelease = new ReleaseCreateRequest();
		createRelease.setName("newrelease");
		call(() -> client().createRelease(PROJECT_NAME, createRelease));
		failingLatch(latch);

		NodeListResponse response = call(() -> client().searchNodes(PROJECT_NAME, getSimpleQuery("supersonic")));
		assertThat(response.getData()).as("Search result").isEmpty();

		response = call(() -> client().searchNodes(PROJECT_NAME, getSimpleQuery("supersonic"),
				new VersioningParametersImpl().setRelease(db().noTx(() -> project().getInitialRelease().getName()))));
		assertThat(response.getData()).as("Search result").usingElementComparatorOnFields("uuid").containsOnly(concorde);
	}

	@Test
	public void testSearchTagFamilies() throws Exception {
		try (NoTx noTx = db().noTx()) {
			recreateIndices();
		}

		String query = getESQuery("tagFamilySearch.es");

		NodeListResponse response = call(() -> client().searchNodes(PROJECT_NAME, query));
		assertThat(response.getData()).isNotEmpty();

		for (NodeResponse node : response.getData()) {
			long count = node.getTags().stream().filter(tag -> tag.getName().equals("red")).count();
			assertThat(count).as("The node should have the tag 'red'.").isGreaterThanOrEqualTo(1);
		}
	}

}
