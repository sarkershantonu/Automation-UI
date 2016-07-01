package org.browser.manage.remote;

import org.browser.manage.BrowserFactory;
import org.browser.manage.CapabilityFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.util.concurrent.BlockingDeque;

/**
 * Created by shantonu on 7/1/16.
 */
public class RemoteWDConnector extends Thread{
    private BlockingDeque<RemoteWebDriver> drivers;
    private String url;

    public RemoteWDConnector(String url, final BlockingDeque<RemoteWebDriver> allRemotedrivers){
        this.drivers=allRemotedrivers;
        this.url=url;
    }
    @Override
    public void run(){

        try {
            RemoteWebDriver driver =  BrowserFactory.getDefaultRemoteDriver();
            drivers.put(driver);
        } catch (InterruptedException e) {
            this.interrupt();
        }catch (Exception e){
            e.printStackTrace();
            this.hold();
        }
    }

    public final void hold(){
        try {
            this.sleep(2000);//default waut
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}