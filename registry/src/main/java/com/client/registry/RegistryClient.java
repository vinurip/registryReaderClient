package com.client.registry;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;


/**
 * Simple client that can be used to connect to the repository.
 *
 * @author Vinuri
 */
public class RegistryClient {

	private static ConfigurationContext configContext = null;

	private static final String CARBON_HOME = "/Users/vinurip/wso2/wso2greg-4.6.0";
	private static final String axis2Conf = CARBON_HOME + "/repository/conf/axis2/axis2_client.xml";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String serverURL = "https://localhost:9443/services/";


	/**
	 * Simple main method that uses the repository to execute a certain
	 * operation on the repository
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Registry registry = initialize();

		String regString = "/Test/";
	//	String resourcePath = "/Test/appserversumedhaorg1-3";
		// String
		// resourcePath="/_system/governance/Test/appserversumedhaorg1-3";

		// CartridgeSubscription cartridgeSubscription =
		// deserialize(deserializeFilePath, regString);
		// cartridgeSubscription =
		// changeResourceUrl(cartridgeSubscription,
		// "https://s2giq.clou.wso2.com/git/Development/as/119.git");
		// updateRegistry(cartridgeSubscription, registry, resourcePath);
		// CartridgeSubscription cartridgeSubscription2 =
		// deserialize(deserializeFilePath, regString);
		// Map<String, CartridgeSubscription> cartridgeSubscriptions =
		// getResourcePath(registry,
		// regString);
		// for (Map.Entry<String, CartridgeSubscription> entry :
		// cartridgeSubscriptions.entrySet()) {
		// System.out.println(entry.getKey() + " aaa -- " + entry.getValue());
		// }

	//	getResourcePath(registry, regString);
	//	deserialize(deserializeFilePath,"/Test/active/1");
		
		RegistryReader reader= new RegistryReader();
		reader.changeRegsitryData(registry, regString);
		

	}

	

	private static WSRegistryServiceClient initialize() throws Exception {
		System.setProperty("javax.net.ssl.trustStore", CARBON_HOME +
		                                               "/repository/resources/security/wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("carbon.repo.write.mode", "true");
		configContext =
		                ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Conf);
		return new WSRegistryServiceClient(serverURL, username, password, configContext);

	}

	
	

}