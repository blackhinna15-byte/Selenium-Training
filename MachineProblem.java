package com.bootcamp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class MachineProblem {

    @Test
    public void fullSequentialTest() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // 1. Navigate to homepage and assert title
            driver.get("https://the-internet.herokuapp.com/");
            String actualTitle = driver.getTitle();
            String expectedTitle = "The Internet";
            Assert.assertEquals(actualTitle, expectedTitle);
            Thread.sleep(2000);

            // 2. Perform Login and Logout
            driver.findElement(By.linkText("Form Authentication")).click();

            WebElement usernameInput = driver.findElement(By.id("username"));
            usernameInput.sendKeys("tomsmith");

            Thread.sleep(2000);

            WebElement passwordInput = driver.findElement(By.id("password"));
            passwordInput.sendKeys("SuperSecretPassword!");
            Thread.sleep(2000);

            driver.findElement(By.cssSelector("button.radius")).click();
            Thread.sleep(2000);

            WebElement successMessage = driver.findElement(By.id("flash"));
            Assert.assertTrue(successMessage.getText().contains("You logged into a secure area!"));

            Thread.sleep(2000);

            driver.findElement(By.cssSelector("a.button.secondary.radius")).click();

            Thread.sleep(2000);

            WebElement logoutMessage = driver.findElement(By.id("flash"));
            Assert.assertTrue(logoutMessage.getText().contains("You logged out of the secure area!"));

            Thread.sleep(2000);
            
            // Go back to the main page to ensure clean state for next test
            driver.get("https://the-internet.herokuapp.com/");

            Thread.sleep(2000);

            // 3. Perform Dropdown Selection
            // Explicitly navigate to the dropdown page URL to ensure correct page loading
            driver.get("https://the-internet.herokuapp.com/dropdown");
            Thread.sleep(2000); // Add a small wait for the page to load
            WebElement dropdown = driver.findElement(By.id("dropdown"));
            Select select = new Select(dropdown);
            select.selectByValue("2");
            Thread.sleep(2000);
            
            WebElement selectedOption = select.getFirstSelectedOption();
            Assert.assertEquals(selectedOption.getText(), "Option 2");

            Thread.sleep(2000);

            driver.navigate().back(); // Go back to the homepage
            Thread.sleep(2000);

            // 4. Perform checkbox selection
            driver.get("https://the-internet.herokuapp.com/checkboxes");

            Thread.sleep(2000);
            
            WebElement checkbox1 = driver.findElement(By.xpath("//input[@type='checkbox'][1]"));
            if (!checkbox1.isSelected()) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox1);
            }
            
            WebElement checkbox2 = driver.findElement(By.xpath("//input[@type='checkbox'][2]"));
            if (!checkbox2.isSelected()) {
                 ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox2);
            }
            
            Assert.assertTrue(checkbox1.isSelected(), "Checkbox 1 is not checked!");
            Assert.assertTrue(checkbox2.isSelected(), "Checkbox 2 is not checked!");
            
            Thread.sleep(2000);
            driver.get("https://the-internet.herokuapp.com/");

            Thread.sleep(2000);

            // 5. Handle JavaScript Confirm Alert
            driver.get("https://the-internet.herokuapp.com/javascript_alerts");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//button[.='Click for JS Confirm']")).click();
            Thread.sleep(2000);
            
            // Switch to the alert and accept it
            driver.switchTo().alert().accept();
            
            // Assert the result message
            WebElement result = driver.findElement(By.id("result"));
            Assert.assertEquals(result.getText(), "You clicked: Ok");
            
            Thread.sleep(2000);

            // 6. Handle JavaScript Alert
            driver.findElement(By.xpath("//button[.='Click for JS Alert']")).click();
            Thread.sleep(2000);
            
            // Switch to the alert and accept it
            driver.switchTo().alert().accept();
            
            // Assert the result message
            WebElement resultJsAlert = driver.findElement(By.id("result"));
            Assert.assertEquals(resultJsAlert.getText(), "You successfully clicked an alert");
            
            Thread.sleep(2000);
            driver.get("https://the-internet.herokuapp.com/");
            Thread.sleep(2000);

            // 7. Patience Test explicit wait
            driver.findElement(By.linkText("Dynamic Loading")).click();
            Thread.sleep(1000);
            driver.findElement(By.partialLinkText("Example 1")).click();
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("#start button")).click();
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement finish = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
            
            Assert.assertTrue(finish.getText().contains("Hello World!"));

            Thread.sleep(1000);


        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}