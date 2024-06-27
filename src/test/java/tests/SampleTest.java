package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.SamplePage;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


public class SampleTest {
    private WebDriver driver;
    private SamplePage samplePage;

    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);

    @BeforeEach
    public void setup() {
        logger.info("Начало выполнения теста");
        // Инициализация WebDriver
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver.exe");
        driver = new ChromeDriver();
        logger.info("Открытие страницы");
        driver.get("https://lambdatest.github.io/sample-todo-app/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        samplePage = new SamplePage(driver);
    }

    @Test
    @DisplayName("Sample app test")
    public void testSampleTodo() throws FileNotFoundException {
        try {
            checkIsHeaderPresent();

            checkItemsRemainingText("5 of 5 remaining");

            checkItemIsNotStrikethrough(1);
            toggleItem(1);
            checkItemIsStrikethrough(1);

            int itemsCount = samplePage.getItemsCount();
            for (int i = 2; i < 1 + itemsCount; i++) {
                checkItemIsNotStrikethrough(i);
                toggleItem(i);
                checkItemIsStrikethrough(i);
            }

            addNewItem("Новый элемент");
            checkItemIsNotStrikethrough(samplePage.getItemsCount());
            checkItemsRemainingText("1 of 6 remaining");

            toggleItem(samplePage.getItemsCount());
            checkItemIsStrikethrough(samplePage.getItemsCount());
            checkItemsRemainingText("0 of 6 remaining");
        } catch (AssertionError e) {
            takeScreenshot("testSampleTodo");
            throw e;
        }
    }

    @AfterEach
    public void tearDown() {
        logger.info("Конец выполнения теста");
        // Закрытие браузера
        driver.quit();
    }

    @Step("Проверка наличия заголовка на странице")
    private void checkIsHeaderPresent() {
        logger.info("Проверка наличия заголовка на странице");
        assertTrue(samplePage.isHeaderPresent(), "Заголовок не найден");
    }

    @Step("Проверка текста с оставшимися элементами")
    private void checkItemsRemainingText(String expectedRemainingText) {
        logger.info("Проверка текста с оставшимися элементами");
        assertEquals(expectedRemainingText, samplePage.getItemsRemainingText());
    }

    @Step("Проверка того, что элемент списка не зачеркнут")
    private void checkItemIsNotStrikethrough(int i) {
        logger.info("Проверка того, что элемент списка не зачеркнут");
        assertFalse(samplePage.isItemStrikethrough(i), "элемент списка " + i +" зачеркнут");
    }

    @Step("Изменение состояния элемента списка")
    private void toggleItem(int i) {
        logger.info("Изменение состояния элемента списка");
        samplePage.toggleItem(i);
        checkItemIsStrikethrough(i);
    }

    @Step("Проверка того, что элемент списка зачеркнут")
    private void checkItemIsStrikethrough(int i) {
        logger.info("Проверка того, что элемент списка зачеркнут");
        assertTrue(samplePage.isItemStrikethrough(i), "элемент списка " + i +" не зачеркнут");
    }

    @Step("Добавление нового элемента")
    private void addNewItem(String newItemText) {
        logger.info("Добавление нового элемента");
        samplePage.addNewItem(newItemText);
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
