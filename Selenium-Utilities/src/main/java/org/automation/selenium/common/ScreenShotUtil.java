package org.automation.selenium.common;

import org.apache.commons.io.FileUtils;
import org.automation.utils.common.PropertyUtil;
import org.automation.selenium.UtilBase;

import org.automation.selenium.browser.Browser;
import org.automation.selenium.javascripts.JavaScriptUtil;
import org.automation.selenium.page.PageUtil;
import org.automation.utils.config.ConfigHelper;
import org.automation.utils.io.FileUtilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shantonu on 4/19/16.
 */
public class ScreenShotUtil extends UtilBase {
    public ScreenShotUtil(WebDriver aDriver) {
        super(aDriver);
    }
    public String takeScreenShot(String name, boolean isError){
        StringBuilder fileNama = getFileName(name,isError);
        File screenShot = new File(fileNama.toString());
        screenShot.getParentFile().mkdir();
        new PageUtil(this.driver).waitForPageLoad();
        if(!new PageUtil(this.driver).isPageLoaded()){
            File srcFile = (File) ((TakesScreenshot) Browser.getInstance()).getScreenshotAs(OutputType.FILE);
            try{
                FileUtils.copyFile(srcFile,screenShot);
            } catch (IOException e) {
                //todo default exception managemebt
            }
        }else
        {
            screenShotByJS(screenShot);
        }

        return saveScreenShot(screenShot, isError);


    }

    /***
     * TOdo saving to dile
     * @param screenShot
     * @param isError
     * @return
     */
    private String saveScreenShot(File screenShot, boolean isError) {
        return null;
    }

    /**
     * This actually get links for image working with Jenkins , preapered for windows environment
     * todo => make generic conversion to linux systems , file seperator portion only
     * @param screen
     * @return
     */
    private String getScreenShotLink(File screen){
        String absScreen  = screen.getAbsolutePath();
        String url = PropertyUtil.getSysProperty("BUILD_URL");
        if(url!=null){
            return "<a href=\'"
                    +url
                    +"artifact/"
                    +absScreen.substring(absScreen.indexOf("target"),absScreen.length()).replaceAll("\\\\", "/")+"\' target=\'_blank\'>"
                    +"screenshot"
                    +"</a>";
        }else {
            return "<a href=\'" + absScreen + "\' target=\'_blank\' >" + "screenshot" + "</a>";
        }
    }
    private void writeScreenshotToFile(byte[] content, File file){

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(content);
            out.close();

        } catch (FileNotFoundException e) {
           //todo , default behavior
        } catch (IOException e) {
            // todo default behavior
        }

    }

    /**
     * This is fully based on JS library used by project.. 100% project specific. use different library if you need to
     * @param screenShot
     * Todo => hafl done, need to complete
     */

    private void screenShotByJS(File screenShot){
        String screenshotjs;
        String base64js_getImage;
        String base64_imageContant;

        try{
        screenshotjs = new JavaScriptUtil(this.driver).readJsLibrary("html2canvas.min.js");
        /**The main JS => Change this if it does not works
         * function injectHtml2Canvas()
         {
         {
         %s
         }
         }
         var script = document.createElement('script');
         script.appendChild(document.createTextNode('('+ injectHtml2Canvas +')();'));
         (document.body || document.head || document.documentElement).appendChild(script);
         */
        base64js_getImage = String.format("function injectHtml2Canvas()\n                "
                +"{{\n%s\n}}\n \n                "
                +"var script = document.createElement(\'script\');\n                "
                +"script.appendChild(document.createTextNode(\'(\'+ injectHtml2Canvas +\')();\'));\n                "
                +"(document.body || document.head || document.documentElement).appendChild(script);", new Object[]{screenshotjs});
        base64_imageContant =  "return window.html2canvas != undefined;";

        boolean i = ((Boolean) new JavaScriptUtil(driver).getJsExecutor().executeScript(base64_imageContant,new Object[0])).booleanValue();
        if(!i){
            new JavaScriptUtil(driver).getJsExecutor().executeScript(base64js_getImage, new Object[0]);
        }
        }catch (Exception e){
            //todo default exception => error injecting library for screenshots
        }

    }
    private StringBuilder getFileName(String testname, boolean isError){
        SimpleDateFormat date = ConfigHelper.dateFormat;
        SimpleDateFormat time  = ConfigHelper.timeFormat;
        StringBuilder file = new StringBuilder();
        Date screenDate = new Date();
        String path = PropertyUtil.getSysProperty("screenshot.folder");
        if(path!=null){
            file.append(path).append(PropertyUtil.getSysProperty("file.separator"));
        }
        file.append(PropertyUtil.getSysProperty("user.dir"));
        file.append(PropertyUtil.getSysProperty("file.separator"))
                .append("target")
                .append(PropertyUtil.getSysProperty("file.separator"))
                .append(date.format(screenDate));
        if(isError){
            file.append(PropertyUtil.getSysProperty("file.separator")).append("error_");
        }else
        {
            file.append(PropertyUtil.getSysProperty("file.separator")).append("screenshot_");
        }
        file.append(testname+"_").append(time.format(screenDate));
        file = FileUtilities.trimLimit(file);
        file.append(ConfigHelper.screenshotType);
        return file;
    }


}