package com.syncleus.ferma.ext.orientdb3;

import com.gentics.mesh.cli.BootstrapInitializer;
import com.gentics.mesh.core.data.HibMeshVersion;
import com.gentics.mesh.core.data.dao.PermissionRoots;
import com.gentics.mesh.core.db.TxData;
import com.gentics.mesh.etc.config.AuthenticationOptions;
import com.gentics.mesh.etc.config.CacheConfig;
import com.gentics.mesh.etc.config.ClusterOptions;
import com.gentics.mesh.etc.config.ContentConfig;
import com.gentics.mesh.etc.config.DebugInfoOptions;
import com.gentics.mesh.etc.config.GraphStorageOptions;
import com.gentics.mesh.etc.config.HttpServerConfig;
import com.gentics.mesh.etc.config.ImageManipulatorOptions;
import com.gentics.mesh.etc.config.MeshOptions;
import com.gentics.mesh.etc.config.MeshUploadOptions;
import com.gentics.mesh.etc.config.MonitoringConfig;
import com.gentics.mesh.etc.config.VertxOptions;
import com.gentics.mesh.etc.config.search.ElasticSearchOptions;

public class TxDataImpl implements TxData {

	private final BootstrapInitializer boot;
	private final MeshOptions options;
	private final PermissionRoots permissionRoots;

	public TxDataImpl(MeshOptions options, BootstrapInitializer boot, PermissionRoots permissionRoots) {
		this.options = options;
		this.boot = boot;
		this.permissionRoots = permissionRoots;
	}

	@Override
	public MeshOptions options() {
		return options;
	}

	public void overrideWithEnv() {
		options.overrideWithEnv();
	}

	public int hashCode() {
		return options.hashCode();
	}

	public boolean equals(Object obj) {
		return options.equals(obj);
	}

	public String getDefaultLanguage() {
		return options.getDefaultLanguage();
	}

	public MeshOptions setDefaultLanguage(String defaultLanguage) {
		return options.setDefaultLanguage(defaultLanguage);
	}

	public String getLanguagesFilePath() {
		return options.getLanguagesFilePath();
	}

	public MeshOptions setLanguagesFilePath(String languagesFilePath) {
		return options.setLanguagesFilePath(languagesFilePath);
	}

	public int getDefaultMaxDepth() {
		return options.getDefaultMaxDepth();
	}

	public MeshOptions setDefaultMaxDepth(int defaultMaxDepth) {
		return options.setDefaultMaxDepth(defaultMaxDepth);
	}

	public GraphStorageOptions getStorageOptions() {
		return options.getStorageOptions();
	}

	public MeshOptions setStorageOptions(GraphStorageOptions storageOptions) {
		return options.setStorageOptions(storageOptions);
	}

	public MeshUploadOptions getUploadOptions() {
		return options.getUploadOptions();
	}

	public MeshOptions setUploadOptions(MeshUploadOptions uploadOptions) {
		return options.setUploadOptions(uploadOptions);
	}

	public HttpServerConfig getHttpServerOptions() {
		return options.getHttpServerOptions();
	}

	public MeshOptions setHttpServerOptions(HttpServerConfig httpServerOptions) {
		return options.setHttpServerOptions(httpServerOptions);
	}

	public MonitoringConfig getMonitoringOptions() {
		return options.getMonitoringOptions();
	}

	public MeshOptions setMonitoringOptions(MonitoringConfig monitoringOptions) {
		return options.setMonitoringOptions(monitoringOptions);
	}

	public VertxOptions getVertxOptions() {
		return options.getVertxOptions();
	}

	public MeshOptions setVertxOptions(VertxOptions vertxOptions) {
		return options.setVertxOptions(vertxOptions);
	}

	public ClusterOptions getClusterOptions() {
		return options.getClusterOptions();
	}

	public MeshOptions setClusterOptions(ClusterOptions clusterOptions) {
		return options.setClusterOptions(clusterOptions);
	}

	public ElasticSearchOptions getSearchOptions() {
		return options.getSearchOptions();
	}

	public MeshOptions setSearchOptions(ElasticSearchOptions searchOptions) {
		return options.setSearchOptions(searchOptions);
	}

	public AuthenticationOptions getAuthenticationOptions() {
		return options.getAuthenticationOptions();
	}

	public MeshOptions setAuthenticationOptions(AuthenticationOptions authenticationOptions) {
		return options.setAuthenticationOptions(authenticationOptions);
	}

	public String getTempDirectory() {
		return options.getTempDirectory();
	}

	public MeshOptions setTempDirectory(String tempDirectory) {
		return options.setTempDirectory(tempDirectory);
	}

	public String toString() {
		return options.toString();
	}

	public String getPluginDirectory() {
		return options.getPluginDirectory();
	}

	public MeshOptions setPluginDirectory(String pluginDirectory) {
		return options.setPluginDirectory(pluginDirectory);
	}

	public ImageManipulatorOptions getImageOptions() {
		return options.getImageOptions();
	}

	public MeshOptions setImageOptions(ImageManipulatorOptions imageOptions) {
		return options.setImageOptions(imageOptions);
	}

	public ContentConfig getContentOptions() {
		return options.getContentOptions();
	}

	public MeshOptions setContentOptions(ContentConfig contentOptions) {
		return options.setContentOptions(contentOptions);
	}

	public DebugInfoOptions getDebugInfoOptions() {
		return options.getDebugInfoOptions();
	}

	public MeshOptions setDebugInfoOptions(DebugInfoOptions debugInfoOptions) {
		return options.setDebugInfoOptions(debugInfoOptions);
	}

	public CacheConfig getCacheConfig() {
		return options.getCacheConfig();
	}

	public MeshOptions setCacheConfig(CacheConfig cacheConfig) {
		return options.setCacheConfig(cacheConfig);
	}

	public boolean isUpdateCheckEnabled() {
		return options.isUpdateCheckEnabled();
	}

	public MeshOptions setUpdateCheck(boolean updateCheck) {
		return options.setUpdateCheck(updateCheck);
	}

	public String getNodeName() {
		return options.getNodeName();
	}

	public MeshOptions setNodeName(String nodeName) {
		return options.setNodeName(nodeName);
	}

	public boolean isInitClusterMode() {
		return options.isInitClusterMode();
	}

	public MeshOptions setInitCluster(boolean isInitCluster) {
		return options.setInitCluster(isInitCluster);
	}

	public String getLockPath() {
		return options.getLockPath();
	}

	public MeshOptions setLockPath(String lockPath) {
		return options.setLockPath(lockPath);
	}

	public String getAdminPassword() {
		return options.getAdminPassword();
	}

	public MeshOptions setAdminPassword(String adminPassword) {
		return options.setAdminPassword(adminPassword);
	}

	public String getInitialAdminPassword() {
		return options.getInitialAdminPassword();
	}

	public MeshOptions setInitialAdminPassword(String initialAdminPassword) {
		return options.setInitialAdminPassword(initialAdminPassword);
	}

	public boolean isForceInitialAdminPasswordReset() {
		return options.isForceInitialAdminPasswordReset();
	}

	public MeshOptions setForceInitialAdminPasswordReset(boolean forceInitialAdminPasswordReset) {
		return options.setForceInitialAdminPasswordReset(forceInitialAdminPasswordReset);
	}

	public int getPluginTimeout() {
		return options.getPluginTimeout();
	}

	public MeshOptions setPluginTimeout(int pluginTimeout) {
		return options.setPluginTimeout(pluginTimeout);
	}

	public boolean isStartInReadOnly() {
		return options.isStartInReadOnly();
	}

	public MeshOptions setStartInReadOnly(boolean startInReadOnly) {
		return options.setStartInReadOnly(startInReadOnly);
	}

	public int getVersionPurgeMaxBatchSize() {
		return options.getVersionPurgeMaxBatchSize();
	}

	public MeshOptions setVersionPurgeMaxBatchSize(int versionPurgeMaxBatchSize) {
		return options.setVersionPurgeMaxBatchSize(versionPurgeMaxBatchSize);
	}

	public void validate() {
		options.validate();
	}

	public void validate(MeshOptions options) {
		options.validate(options);
	}

	@Override
	public HibMeshVersion meshVersion() {
		return boot.meshRoot();
	}

	@Override
	public PermissionRoots permissionRoots() {
		return permissionRoots;
	}

}
