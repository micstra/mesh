package com.gentics.mesh.dagger;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import com.gentics.mesh.Mesh;
import com.gentics.mesh.dagger.module.CommonModule;
import com.gentics.mesh.dagger.module.OrientDBModule;
import com.gentics.mesh.etc.config.MeshOptions;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Central dagger mesh component which will expose dependencies.
 */
@Singleton
@Component(modules = { CommonModule.class, OrientDBModule.class })
public interface OrientDBMeshComponent extends MeshComponent {

	@Component.Builder
	interface Builder extends MeshComponent.Builder {
		@BindsInstance
		Builder configuration(MeshOptions options);

		@BindsInstance
		Builder mesh(Mesh mesh);

		@BindsInstance
		Builder searchProviderType(@Nullable SearchProviderType type);

		OrientDBMeshComponent build();
	}
}
