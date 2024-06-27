package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.MosPolytechPage;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MosPolytechTest {
    private WebDriver driver;
    private MosPolytechPage mosPolytechPage;

    private static final Logger logger = LoggerFactory.getLogger(MosPolytechTest.class);

    @BeforeEach
    public void setup() {
        logger.info("Начало выполнения теста");
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver.exe");
        driver = new ChromeDriver();
        logger.info("Открытие страницы мосполитеха");
        driver.get("https://mospolytech.ru/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        mosPolytechPage = new MosPolytechPage(driver);
    }

    @Test
    @DisplayName("MosPolytech test")
    public void testMosPolytech() throws FileNotFoundException {
        try {
        clickSchedule();

        clickWatchOnSiteLocatorLink();

        enterGroupNum("221-361");

        clickOnFoundedGroup();
        } catch (AssertionError e) {
            takeScreenshot("MosPolytechTest");
            throw e;
        }
    }

    @AfterEach
    public void tearDown() {
        logger.info("Завершение теста");
        // Закрытие браузера
        driver.quit();
    }

    @Step("Клик на ссылку расписание")
    private void clickSchedule() {
        logger.info("Клик на ссылку расписание");
        mosPolytechPage.clickScheduleLink();
    }

    @Step("Клик на ссылку 'смотреть на сайте'")
    private void clickWatchOnSiteLocatorLink() {
        logger.info("Клик на ссылку 'смотреть на сайте'");
        mosPolytechPage.clickWatchOnSiteLocatorLink();
        assertEquals(2, driver.getWindowHandles().size());
    }

    @Step("Ввод номера группы")
    private void enterGroupNum(String groupNum) {
        logger.info("Ввод номера группы: {}", groupNum);
        mosPolytechPage.enterGroupNum(groupNum);
        List<WebElement> groups = mosPolytechPage.getGroupResult();
        assertTrue(groups.size() == 1 && Objects.equals(groups.getFirst().getText(), groupNum), "Результаты поиска некорректны");
    }

    @Step("Клик на найденную группу в результатх поиска")
    private void clickOnFoundedGroup() {
        logger.info("Клик на найденную группу в результатах поиска");
        mosPolytechPage.clickOnGroup();
        assertEquals(this.getDayOfWeek(), mosPolytechPage.getDayOfWeek());
    }

    private int getDayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    private void takeScreenshot(String testCaseName) throws FileNotFoundException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        String screenshotFileName = testCaseName + "_" + timestamp + ".png";

        // Сделать скриншот
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);

        // Сохранить скриншот в каталог для Allure отчета
        Allure.addAttachment(screenshotFileName, new FileInputStream(screenshot));

        // Сохранить скриншот в папку с проектом
        File projectScreenshotFile = new File("src/test/resources/screenshots/" + screenshotFileName);
        try (FileOutputStream fos = new FileOutputStream(projectScreenshotFile)) {
            Files.copy(screenshot.toPath(), fos);
        } catch (IOException e) {
            logger.error("Не удалось сохранить скриншот в файл: {}", e.getMessage());
        }
    }
}
