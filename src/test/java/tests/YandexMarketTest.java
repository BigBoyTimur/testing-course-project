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
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.YandexMarketPage;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YandexMarketTest {
    private WebDriver driver;
    private YandexMarketPage yandexMarketPage;
    private String firstProductTitle;
    private String firstProductPrice;


    private static final Logger logger = LoggerFactory.getLogger(YandexMarketTest.class);

    @BeforeEach
    public void setup() {
        logger.info("Начало выполнения теста");
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-geolocation");
        driver = new ChromeDriver(options);
        logger.info("Открытие страницы яндекс маркета");
        driver.get("https://market.yandex.ru/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
        yandexMarketPage = new YandexMarketPage(driver);
    }

    @Test
    @DisplayName("YandexMarket Test")
    public void testYandexMarket() throws InterruptedException, FileNotFoundException {
        try {
            clickCatalogButton();

            logProducts();

            saveFirstProductInfo();

            addFirstToFavourite();

            goToFavourites();

            removeFistFromFavourite();

            refreshThePage();
        } catch (AssertionError e) {
            takeScreenshot("yandexMarketTest");
            throw e;
        }
    }

    @AfterEach
    public void tearDown() {
        logger.info("Завершение теста");
        // Закрытие браузера
//        driver.quit();
    }

    @Step("Клик на меню “Каталог”, выбор категории: Все для гейминга -> Xbox -> Игровые приставки")
    private void clickCatalogButton() throws InterruptedException {
        logger.info("Клик на меню “Каталог”, выбор категории: Все для гейминга -> Xbox -> Игровые приставки");
        yandexMarketPage.selectXboxCatalog();
        assertTrue(yandexMarketPage.isXboxTitleDisplayed(), "не та станица");
    }

    @Step("Вывод в лог первых 5 товаров")
    private void logProducts() {
        logger.info("Вывод в лог первых 5 товаров");
        List<WebElement> productsTitles = yandexMarketPage.getProductsTitles();
        List<WebElement> productsPrices = yandexMarketPage.getProductsPrices();
        logger.info("{}: {}", productsTitles.get(0).getText(), productsPrices.get(0).getText());
        logger.info("{}: {}", productsTitles.get(1).getText(), productsPrices.get(1).getText());
        logger.info("{}: {}", productsTitles.get(2).getText(), productsPrices.get(2).getText());
        logger.info("{}: {}", productsTitles.get(3).getText(), productsPrices.get(3).getText());
        logger.info("{}: {}", productsTitles.get(4).getText(), productsPrices.get(4).getText());
    }

    @Step("Сохранение название и цены первого товара")
    private void saveFirstProductInfo() {
        logger.info("Сохранение название и цены первого товара");
        this.firstProductTitle = yandexMarketPage.getProductsTitles().getFirst().getText();
        this.firstProductPrice = yandexMarketPage.getProductsPrices().getFirst().getText();
    }

    @Step("Добавление первого товара в избранное")
    private void addFirstToFavourite() throws InterruptedException {
        logger.info("Добавление первого товара в избранное");
        WebElement button = yandexMarketPage.addToFavourite(0);
        assertEquals(button.getAttribute("title"), "Удалить из избранного");
        Thread.sleep(1500);
        assertTrue(yandexMarketPage.isFavouritesAddedMessageLocatorDisplayed(), "Сообщение о том, что товар добавлен в корзину не отображено");
//        removeFistFromFavourite();
    }

    @Step("Переход на страницу избранное")
    private void goToFavourites() {
        logger.info("Переход на страницу избранное");
        yandexMarketPage.clickFavouritesLink();
        assertTrue(yandexMarketPage.isFirstProductMatches(firstProductTitle, firstProductPrice), "неправильно сохранен или добавлен товар");
    }

    @Step("Удаление первого товара из избранного")
    private void removeFistFromFavourite() throws InterruptedException {
        logger.info("Удаление первого товара из избранного");
        assertTrue(yandexMarketPage.deleteFromFavourites(), "Сообщение о том, что товар удален не отображено");
    }

    @Step("Обновление страницы")
    private void refreshThePage() throws InterruptedException {
        logger.info("Обновление страницы");
        assertTrue(yandexMarketPage.refreshPage(), "Корзина не пуста");
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
