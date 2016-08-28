package org.automation.selenium.validation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by shantonu on 8/28/16.
 */
public class ValidateProcessBar extends ValidateWebElement {
    public ValidateProcessBar(WebElement webElement) {
        super(webElement);
    }

    public ValidateProcessBar(By by) {
        super(by);
    }
}
