package org.automation.selenium.validation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by shantonu on 8/28/16.
 */
public class ValidateAccordion extends ValidateWebElement{
    public ValidateAccordion(WebElement webElement) {
        super(webElement);
    }

    public ValidateAccordion(By by) {
        super(by);
    }
}
