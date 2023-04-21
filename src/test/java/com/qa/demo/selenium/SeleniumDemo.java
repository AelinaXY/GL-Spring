package com.qa.demo.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SeleniumDemo {

	private WebDriver driver;

	@BeforeEach
	void init() {
		// ChromeOptions options = new ChromeOptions();
		// options.addArguments("--remote-allow-origins=*");
		// creates new window
		this.driver = new FirefoxDriver();
		// maximises it
		this.driver.manage().window().maximize();
	}

	@Test
	void turtleTest() {
		// redirects browser to this address
		this.driver.get("https://www.bbc.co.uk/search");

		WebElement search = this.driver.findElement(By.cssSelector("#search-input"));
		search.sendKeys("turtle");
		search.sendKeys(Keys.ENTER);

		WebElement result = this.driver.findElement(By.cssSelector(
				"#main-content > div.ssrcss-1v7bxtk-StyledContainer.enjd40x0 > div > div > ul > li:nth-child(1) > div > div > div.ssrcss-tq7xfh-PromoContent.e1f5wbog8 > div.ssrcss-1f3bvyz-Stack.e1y4nx260 > a > span"));

		Assertions.assertEquals("The Turtle Dove Pilgrimage", result.getText());
	}

	@AfterEach
	void tearDown() {
		this.driver.close();
	}

}
