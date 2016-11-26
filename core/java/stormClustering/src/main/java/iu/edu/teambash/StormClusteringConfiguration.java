package iu.edu.teambash;

import io.dropwizard.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StormClusteringConfiguration extends Configuration {
    // TODO: implement service configuration

    public void init() throws ServletException {
        String ip = null;
        try {
            java.net.URL getMyIP = new java.net.URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(getMyIP.openStream()));
            ip = in.readLine();
            //ip = "127.0.0.1";
            System.out.println("Host IP is: ---------------------> " + ip);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String microServiceURL = "http://"+ip+":7777/StormClustering";
        String msName = "stormClustering";
        int port = 7777;

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ip+":2181", new RetryNTimes(5, 1000));
        curatorFramework.start();

        try{
            ServiceInstance serviceInstance = ServiceInstance.builder().uriSpec(new UriSpec(microServiceURL)).address(ip).port(port).name(msName).build();
            ServiceDiscoveryBuilder.builder(Void.class).basePath("services").client(curatorFramework).thisInstance(serviceInstance).build().start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

