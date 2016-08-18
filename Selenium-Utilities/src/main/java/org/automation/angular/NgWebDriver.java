package org.automation.angular;


import org.automation.angular.scripts.WaitForAngular;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.List;
import java.util.Set;

/**
 * Angular WebDriver Implementation.
 * Only difference is adding wait for angular JS to run, no extra
 *
 * TODO =>  Shantonu, adding all javascript capability to enrich this driver... example form ajax& js examples
 */
public final class NgWebDriver implements WebDriver, WrapsDriver {
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;
    private final String root;
    private NgModule[] mockModules;
    public boolean IgnoreSynchronization;
    public int MaxIterations;

    /**
     * Ctor.
     * @param drv Parent web driver.
     */
    public NgWebDriver(final WebDriver drv) {
        this(drv, "body");
    }

    /**
     * Ctor.
     * @param drv Parend Web Driver.
     * @param rootElement Root Element.
     */
    public NgWebDriver(
        final WebDriver drv,
        final String rootElement
    ) {
        if (!(drv instanceof JavascriptExecutor)) {
            throw new WebDriverException(
                "The WebDriver instance must implement the " +
                    "JavaScriptExecutor interface."
            );
        }

        this.driver = drv;
        this.jsExecutor = (JavascriptExecutor) drv;
        this.root = rootElement;
    }
    public NgWebDriver(WebDriver driver, boolean ignoreSync)
    {
        this(driver);
        this.IgnoreSynchronization = ignoreSync;
    }


    @Override
    public WebDriver getWrappedDriver() {
        return this.driver;
    }

    @Override
    public void close() {
        this.driver.close();
    }

    @Override
    public NgWebElement findElement(final By by) {
        this.waitForAngular();
        return new NgWebElement(this, this.driver.findElement(by));
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return this.driver.findElements(by);
    }

    @Override
    public void get(final String arg0) {
        this.waitForAngular();
        this.driver.get(arg0);
    }

    @Override
    public String getCurrentUrl() {
        this.waitForAngular();
        return this.driver.getCurrentUrl();
    }

    @Override
    public String getPageSource() {
        this.waitForAngular();
        return this.driver.getPageSource();
    }

    @Override
    public String getTitle() {
        this.waitForAngular();
        return this.driver.getTitle();
    }

    @Override
    public String getWindowHandle() {
        this.waitForAngular();
        return this.driver.getWindowHandle();
    }

    @Override
    public Set<String> getWindowHandles() {
        this.waitForAngular();
        return this.driver.getWindowHandles();
    }

    @Override
    public Options manage() {
        return this.driver.manage();
    }

    @Override
    public Navigation navigate() {
        return new NgNavigation(this.driver.navigate());
    }

    @Override
    public void quit() {
        this.driver.quit();

    }

    @Override
    public TargetLocator switchTo() {
        return this.driver.switchTo();
    }

    public void waitForAngular() {
        this.jsExecutor.executeAsyncScript(
            new WaitForAngular().content(),
            this.root
        );
    }
}
