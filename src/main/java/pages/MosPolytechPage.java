package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MosPolytechPage {
    private WebDriver driver;

    private By scheduleLinkLocator = By.xpath("//a[@title='Расписание']");
    private By watchOnSiteLinkLocator = By.xpath("//a[.//span[text()='Смотрите на сайте']]");
    private By groupInputLocator = By.xpath("//input[@placeholder='группа ...']");
    private By groupsLocator = By.xpath("//div[contains(@class, 'found-groups')]//div[contains(@class, 'group')]");
    private By daysOfCurrentWeek = By.xpath("//*[contains(@class, 'schedule-week')][.//div[contains(@class, 'schedule-day_today')]]/div[contains(@class, 'schedule-day')]");

    public MosPolytechPage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickScheduleLink() {
        WebElement link = driver.findElement(scheduleLinkLocator);
        link.click();
    }

    public void clickWatchOnSiteLocatorLink() {
        List<WebElement> links = driver.findElements(watchOnSiteLinkLocator);
        links.get(1).click();
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[1]);
    }

    public void enterGroupNum(String groupNum) {
        WebElement input = driver.findElement(groupInputLocator);
        input.sendKeys(groupNum);
    }

    public List<WebElement> getGroupResult() {
        return driver.findElements(groupsLocator);
    }

    public void clickOnGroup() {
        driver.findElements(groupsLocator).getFirst().click();
    }

    public int getDayOfWeek() {
        List<WebElement> daysOfWeek = driver.findElements(daysOfCurrentWeek);
        for (int i = 0; i < 6; i++) {
            if (daysOfWeek.get(i).getAttribute("class").contains("schedule-day_today")) {
                return i + 1;
            }
        }
        return -1;
    }
}
