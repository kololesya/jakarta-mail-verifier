package com.solvd.laba.mail.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected abstract String getPageUrl();

    public void open() {
        driver.get(getPageUrl());
    }

    public WebDriver getDriver() {
        return driver;
    }
}

