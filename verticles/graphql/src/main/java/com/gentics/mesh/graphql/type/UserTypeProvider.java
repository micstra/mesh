package com.gentics.mesh.graphql.type;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.data.User;
import com.gentics.mesh.core.data.node.Node;
import com.gentics.mesh.core.data.relationship.GraphPermission;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLObjectType.Builder;
import graphql.schema.GraphQLTypeReference;

@Singleton
public class UserTypeProvider extends AbstractTypeProvider {

	@Inject
	public InterfaceTypeProvider interfaceTypeProvider;

	@Inject
	public UserTypeProvider() {
	}

	public GraphQLObjectType getUserType() {
		Builder root = newObject();
		root.name("User");
		root.description("User description");
		interfaceTypeProvider.addCommonFields(root);

		// .username
		root.field(newFieldDefinition().name("username")
				.description("The username of the user")
				.type(GraphQLString)
				.dataFetcher((env) -> {
					User user = env.getSource();
					return user.getUsername();
				}));

		// .firstname
		root.field(newFieldDefinition().name("firstname")
				.description("The firstname of the user")
				.type(GraphQLString)
				.dataFetcher((env) -> {
					User user = env.getSource();
					return user.getFirstname();
				}));

		// .lastname
		root.field(newFieldDefinition().name("lastname")
				.description("The lastname of the user")
				.type(GraphQLString)
				.dataFetcher((env -> {
					User user = env.getSource();
					return user.getLastname();
				})));

		// .emailAddress
		root.field(newFieldDefinition().name("emailAddress")
				.description("The email of the user")
				.type(GraphQLString)
				.dataFetcher((env) -> {
					User user = env.getSource();
					return user.getEmailAddress();
				}));

		// .groups
		root.field(newPagingFieldWithFetcher("groups", "Groups to which the user belongs.", (env) -> {
			User user = env.getSource();
			InternalActionContext ac = env.getContext();
			return user.getGroups(ac.getUser(), getPagingInfo(env));
		}, "Group"));

		// .nodeReference
		root.field(newFieldDefinition().name("nodeReference")
				.description("User node reference")
				.type(new GraphQLTypeReference("Node"))
				.dataFetcher((env) -> {
					User user = env.getSource();
					InternalActionContext ac = env.getContext();
					Node node = user.getReferencedNode();
					if (node != null) {
						if (ac.getUser()
								.hasPermission(node, GraphPermission.READ_PERM)
								|| ac.getUser()
										.hasPermission(node, GraphPermission.READ_PUBLISHED_PERM)) {
							return node;
						}
					}
					return null;
				}));

		return root.build();
	}
}