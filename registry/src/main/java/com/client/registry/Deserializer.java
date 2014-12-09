package com.client.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.utils.RegistryClientUtils;

public class Deserializer {

	private final  String FilePath = "/Users/vinurip/Vinu/h/"; 
	
	public  void downloadDeserializableFile(String registryPath) {
		RemoteRegistry remote_registry;
		try {
			remote_registry =
			                  new RemoteRegistry(new URL("https://10.100.5.174:9443/registry"),
			                                     "admin", "admin");
			File file = new File(FilePath);
			RegistryClientUtils.exportFromRegistry(file, registryPath, remote_registry);

		} catch (RegistryException e) {
			System.out.println("Error while geting the  remote registry  -");
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			System.out.println("Error while geting the registry url -");
			e1.printStackTrace();
		}

	}

	public  Object deserializeFromFile(String filePath) {
		Object object  = null;
		try {
			File[] files = new File(filePath).listFiles();
			ObjectInputStream objectInputStream;
			for (File file : files) {
				FileInputStream inputFileStream = new FileInputStream(file);
				if (!file.getName().startsWith(".") && !file.isHidden()) {
					objectInputStream = new ObjectInputStream(inputFileStream);
					object= objectInputStream.readObject();
					objectInputStream.close();
					inputFileStream.close();
					file.delete();
				}

			}

		} catch (IOException e) {
			System.out.println("Error while reading the file content - ");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error while deserializing the file content -");
	        e.printStackTrace();
        }
		return object;

	}
}
