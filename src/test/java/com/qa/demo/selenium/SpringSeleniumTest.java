package com.qa.demo.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "classpath:cat-schema.sql",
        "classpath:cat-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class SpringSeleniumTest {

    private WebDriver driver;

    @LocalServerPort
    private int port;

    private WebDriverWait wait;

    @BeforeEach
    void init() {
        this.driver = new FirefoxDriver();
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @Test
    void testTitle() {
        this.driver.get("http://localhost:" + port);
        WebElement title = this.driver.findElement(By.cssSelector("body > header > h1"));

        assertEquals("CATS", title.getText());
    }

    @Test
    void testGetAll() throws InterruptedException {
        this.driver.get("http://localhost:" + port);
        WebElement card = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));

        assertTrue(card.getText().contains("Mr Bigglesworth"));
    }

    @Test
    void testCreate() throws InterruptedException {
        this.driver.get("http://localhost:" + port);
        WebElement nameBox = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#catForm > input:nth-child(2)")));
        nameBox.sendKeys("turtle");

        WebElement numberBox = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#catForm > input:nth-child(4)")));
        numberBox.sendKeys("34");

        WebElement submitBox = this.wait.until(
                ExpectedConditions
                        .elementToBeClickable(By.cssSelector("#catForm > div:nth-child(7) > button:nth-child(2)")));

        submitBox.click();

        WebElement card = this.wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.col:nth-child(2) > div:nth-child(1) > div:nth-child(1) > p:nth-child(1)")));

        assertTrue(card.getText().contains("turtle"));
    }

    @Test
    void testUpdate() throws InterruptedException {
        this.driver.get("http://localhost:" + port);
        WebElement updateButton = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(
                        "div.col:nth-child(1) > div:nth-child(1) > div:nth-child(1) > button:nth-child(5)")));
        updateButton.click();

        WebElement nameBox = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#updateForm > input:nth-child(2)")));
        nameBox.sendKeys(Keys.BACK_SPACE);

        WebElement submitButton = this.wait.until(
                ExpectedConditions
                        .elementToBeClickable(By.cssSelector("#updateForm > div:nth-child(7) > button:nth-child(2)")));
        submitButton.click();

        WebElement card = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#output > div > div")));

        assertTrue(card.getText().contains("Mr Biggleswort"));
    }

    @Test
    void testDelete() throws InterruptedException {
        this.driver.get("http://localhost:" + port);
        WebElement deleteButton = this.wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(
                        "div.col:nth-child(1) > div:nth-child(1) > div:nth-child(1) > button:nth-child(6)")));
        deleteButton.click();

        List<WebElement> card = this.driver.findElements(By.className("card"));

        assertEquals(0, card.size());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe /T");
        this.driver.quit();
    }
}
