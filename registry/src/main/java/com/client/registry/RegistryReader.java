package com.client.registry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.apache.stratos.cloud.controller.stub.pojo.CartridgeInfo;
import org.apache.stratos.manager.dao.Cluster;
import org.apache.stratos.manager.repository.Repository;
import org.apache.stratos.manager.subscription.CartridgeSubscription;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class RegistryReader {
	private  final String deserializeFilePath = "/Users/vinurip/Vinu/h/";
	private Deserializer deserializer= new Deserializer();
	
	public  void changeRegsitryData(Registry registry, String resourcePath) {
		Object resourceObj = null;

		try {
			resourceObj = registry.get(resourcePath);
			if (resourceObj instanceof org.wso2.carbon.registry.core.Collection) {
				resourceObj = registry.get(resourcePath).getContent();
			}

		} catch (RegistryException e) {
			// TODO Auto-generated catch block
			System.out.println("Error while getting the registry data - ");
			e.printStackTrace();
		}
		if (resourceObj == null) {
			System.out.println("Cannot Find the resource Object");
		} else if (resourceObj instanceof String[]) {
			// get the paths for all SubscriptionContext instances
			String[] subscriptionResourcePaths = (String[]) resourceObj;
			// traverse the paths recursively
			for (String subscriptionResourcePath : subscriptionResourcePaths) {
				changeRegsitryData(registry, subscriptionResourcePath);
			}

		} else {
			System.out.println(" resource path " + resourcePath);
			String path = ((Resource) resourceObj).getParentPath();
			deserializer.downloadDeserializableFile(path);
			
			CartridgeSubscription deserilizedCartridgeSubscription =(CartridgeSubscription) deserializer.deserializeFromFile(deserializeFilePath);
			deserilizedCartridgeSubscription =
			                                   changeClusterDomain(deserilizedCartridgeSubscription);
			deserilizedCartridgeSubscription =
			                                   changeClusterHostName(deserilizedCartridgeSubscription);
			deserilizedCartridgeSubscription =
			                                   changeCartridgeInfo(deserilizedCartridgeSubscription);
			deserilizedCartridgeSubscription =
			                                   changeRepositoryUrl(deserilizedCartridgeSubscription);
			System.out.println("after change -" + deserilizedCartridgeSubscription);
			updateRegistry(deserilizedCartridgeSubscription, registry, resourcePath);
		}

	}
	
	public  CartridgeSubscription changeRepositoryUrl(CartridgeSubscription cartridgeSubscription) {
		Repository repository = cartridgeSubscription.getRepository();
		repository.setUrl(repository.getUrl().replace("s2git.cloud.wso2.com",
		                                              "s2git.cloudstaging.wso2.com"));
		cartridgeSubscription.setRepository(repository);
		return cartridgeSubscription;

	}

	public  CartridgeSubscription changeClusterDomain(CartridgeSubscription cartridgeSubscription) {
		Cluster cluster = cartridgeSubscription.getCluster();
		cluster.setClusterDomain(cluster.getClusterDomain()
		                                .replace("appserver.appserver.dev.cloud.",
		                                         "appserver.appserver.dev.clouds"));
		cartridgeSubscription.setCluster(cluster);
		return cartridgeSubscription;
	}

	public  CartridgeSubscription changeClusterHostName(CartridgeSubscription cartridgeSubscription) {
		Cluster cluster = cartridgeSubscription.getCluster();
		cluster.setHostName(cluster.getHostName().replace("appserver.dev.cloud.wso2.com",
		                                                  "appserver.dev.cloudstaging.wso2.com"));
		cartridgeSubscription.setCluster(cluster);
		return cartridgeSubscription;
	}

	public  CartridgeSubscription changeCartridgeInfo(CartridgeSubscription cartridgeSubscription) {
		CartridgeInfo cartridgeInfo = cartridgeSubscription.getCartridgeInfo();
		cartridgeInfo.setHostName(cartridgeInfo.getHostName()
		                                       .replace("appserver.dev.cloud.wso2.com",
		                                                "appserver.dev.cloudstaging.wso2.com"));
		cartridgeSubscription.setCartridgeInfo(cartridgeInfo);
		return cartridgeSubscription;
	}



	private  void updateRegistry(CartridgeSubscription cartridgeSubscription,
	                                   Registry registry, String resourcePath) {
		Resource resource;

		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

			objectOutputStream.writeObject(cartridgeSubscription);

			objectOutputStream.flush();
			objectOutputStream.close();

			InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			resource = registry.newResource();
			resource.setContent(inputStream);
			registry.put(resourcePath, resource);
			System.out.println("Succesfuly added the resources");
		} catch (Exception ex) {
			System.out.println("Error while updating the registry - ");
			ex.printStackTrace();
		}

	}
}
