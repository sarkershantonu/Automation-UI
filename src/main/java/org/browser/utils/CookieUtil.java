package org.browser.utils;

import automation.utils.UtilBase;

import lombok.Getter;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shantonu on 6/2/16.
 */
public class CookieUtil extends UtilBase{
    private @Getter
    Set<Cookie> browserCookies = null;
    public CookieUtil(WebDriver aDriver) {
        super(aDriver);
        init();
    }

    private  void init(){
        browserCookies = driver.manage().getCookies();
    }
    public void clearCookie(){
        driver.manage().deleteAllCookies();
        init();
    }
    public void add(Cookie cookie){
        driver.manage().addCookie(cookie);
    }

    public void delete(Cookie cookie){
        driver.manage().deleteCookie(cookie);
    }
    public void delete(String cookieName){
        driver.manage().deleteCookieNamed(cookieName);
    }
    public Cookie getCookie(String name){
        return driver.manage().getCookieNamed(name);
    }
}
