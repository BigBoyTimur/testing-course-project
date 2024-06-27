package pages;

import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Objects;

public class YandexMarketPage {
    private WebDriver driver;

    private By catalogButtonLocator = By.xpath("//button[@class='_30-fz button-focus-ring Hkr1q _1pHod _2rdh3 _3rbM-']");
    private By allForGamingLinkLocator = By.xpath("//a[@class='_3yHCR' and .//span[text()='Все для гейминга']]");
    private By gamingConsolesLinkLocator = By.xpath("//a[@class='_2re3U ltlqD _2TBT0' and text() = 'Игровые приставки']");
    private By xboxTitleLocator = By.xpath("//h1[@class='_3lpeU _6tyDq _1ea6I _2Imo_' and contains(text(), 'Игровые приставки Xbox')]");
    private By productTitleLocator = By.xpath("//h3[(@class='G_TNq _2SUA6 _33utW _13aK2 h29Q2 _1A5yJ' or @class='G_TNq _2SUA6 _33utW _13aK2 _2-NSe _1A5yJ' or @class='G_TNq _2SUA6 _33utW _13aK2 _2a1rW _1A5yJ') and not(ancestor::div[@class='_1yYhp _1MOwX _1bCJz'])]");
    private By productPriceLocator = By.xpath("//span[@class='_1ArMm' and not(ancestor::div[@class='_1yYhp _1MOwX _1bCJz'])]");
    private By favouritesAddedMessageLocator = By.xpath("//*[text()='Товар добавлен в избранное. Нажмите, чтобы перейти к списку.' or text()='Товар удалён из избранного']");
    private By addToFavouritesLocator = By.xpath("//*[@title='Добавить в избранное']");
    private By favouritesLinkLocator = By.xpath("//a[@class='EQlfk _2h0Ng' and contains(., 'Избранное')]");
    private By productTitleInFavouritesLocator = By.xpath("//h3[@class='G_TNq _2SUA6 _33utW _13aK2 _2a1rW _1A5yJ']");
    private By deleteFromFavouritesLocator = By.xpath("//*[@title='Удалить из избранного']");
    private By deleteMessageLocator = By.xpath("//*[contains(text(), 'удалён из избранного')]");
    private By cartIsEmptyLocator = By.xpath("//*[text()='Войдите в аккаунт']");

    public YandexMarketPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectXboxCatalog() throws InterruptedException {
        WebElement button = driver.findElement(catalogButtonLocator);
        button.click();

        Thread.sleep(2000);

        Actions action = new Actions(driver);
        WebElement allForGamingLink = driver.findElement(allForGamingLinkLocator);
        action.moveToElement(allForGamingLink).perform();

        WebElement gamingConsolesLink = driver.findElements(gamingConsolesLinkLocator).get(1);
        gamingConsolesLink.click();
    }

    public boolean isXboxTitleDisplayed() {
        WebElement element = driver.findElement(xboxTitleLocator);
        return element.isDisplayed();
    }

    public List<WebElement> getProductsTitles() {
        return driver.findElements(productTitleLocator);
    }
    public List<WebElement> getProductsPrices() {
        return driver.findElements(productPriceLocator);
    }

    //Добавляет элемент в избранное и возвращает кнопку добавления
    public WebElement addToFavourite(int i) {
        WebElement button = driver.findElements(addToFavouritesLocator).get(i);
        button.click();
        return button;
    }

    public boolean isFavouritesAddedMessageLocatorDisplayed() {
        return driver.findElement(favouritesAddedMessageLocator).isDisplayed();
    }

    public void clickFavouritesLink () {
        driver.findElement(favouritesLinkLocator).click();
    }

    public boolean isFirstProductMatches(String title, String price) {
        return Objects.equals(driver.findElement(productTitleInFavouritesLocator).getText(), title) && Objects.equals(driver.findElement(productPriceLocator).getText(), price);
    }

    public boolean deleteFromFavourites() throws InterruptedException {
        driver.findElement(deleteFromFavouritesLocator).click();
        Thread.sleep(1500);
        return driver.findElement(deleteMessageLocator).isDisplayed();
    }

    public boolean refreshPage() {
        driver.navigate().refresh();
        return driver.findElement(cartIsEmptyLocator).isDisplayed();
    }
}
