package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SamplePage {
    private WebDriver driver;

    // Определение локаторов
    private By headerLocator = By.xpath("//h2[text()='LambdaTest Sample App']");
    private By itemsRemainingLocator = By.xpath("//span[@class='ng-binding' and contains(text(), 'remaining')]");
    private By todoItemsLocator = By.xpath("//li//span");
    private By todoCheckBoxesLocator = By.xpath("//input[@type='checkbox']");
    private By newTodoInputLocator = By.xpath("//input[@placeholder='Want to add more']");
    private By addButtonLocator = By.xpath("//input[@value='add']");

    public SamplePage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для взаимодействия с элементами страницы
    public boolean isHeaderPresent() {
        return driver.findElement(headerLocator).isDisplayed();
    }

    public String getItemsRemainingText() {
        return driver.findElement(itemsRemainingLocator).getText();
    }

    public int getItemsCount() {
        return driver.findElements(todoItemsLocator).size();
    }

    public boolean isItemStrikethrough(int i) {
        WebElement item = driver.findElements(todoItemsLocator).get(i - 1);
        return item.getAttribute("class").contains("done-true");
    }

    public void toggleItem(int i) {
        WebElement item = driver.findElements(todoCheckBoxesLocator).get(i - 1);
        item.click();
    }

    public void addNewItem(String itemText) {
        driver.findElement(newTodoInputLocator).sendKeys(itemText);
        driver.findElement(addButtonLocator).click();
    }

}
