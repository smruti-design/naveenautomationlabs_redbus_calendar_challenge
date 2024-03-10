import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RebBusCalenderWeekendHolidays {
	
	static WebDriver driver;

	public static void main(String[] args) {

		// disable notification
		ChromeOptions option = new ChromeOptions();
		option.addArguments("--disable-notifications");

		// webdriver instantiate
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\resources\\chromedriver.exe");
		driver = new ChromeDriver(option);

		// hit the URL & maximise the window
		driver.get("https://www.redbus.in/");
		driver.manage().window().maximize();

		// click on the calendar icon to view the calendar
		WebElement calendarIcon = driver.findElement(By.cssSelector(".sc-cSHVUG.NyvQv.icon.icon-datev2"));
		calendarIcon.click();

		// wait until the calendar appears, as its ajax (asynchronous), the calendar gets loaded without any modification of url
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sc-jzJRlG.dPBSOp")));

		// enter the month & year to view holidays and weekends
		viewHolidayAndWeekends("May 2024");

		driver.quit();

	}

	public static void viewHolidayAndWeekends(String data) {

		String[] date = data.split(" ");
		String expectedMonth = date[0];
		String expectedYear = date[1];

		System.out.println(expectedMonth + " " + expectedYear);

		// extract current month & year
		String dateText = driver
				.findElement(
						By.cssSelector("div[class='DayNavigator__CalendarHeader-qj8jdz-1 fxvMrr'] div:nth-child(2)"))
				.getText();
		String[] s = dateText.split("\n");
		String[] d = s[0].split(" ");
		String currentMonth = d[0];
		String currentYear = d[1];

		while (!(currentYear.equals(expectedYear) && currentMonth.equals(expectedMonth))) {
			// click on the next icon
			driver.findElement(
					By.cssSelector("div[class='DayNavigator__CalendarHeader-qj8jdz-1 fxvMrr']>div:nth-last-child(1)"))
					.click();
			dateText = driver
					.findElement(By
							.cssSelector("div[class='DayNavigator__CalendarHeader-qj8jdz-1 fxvMrr'] div:nth-child(2)"))
					.getText();
			s = dateText.split("\n");
			d = s[0].split(" ");
			currentMonth = d[0];
			currentYear = d[1];

		}

		// get the number of holidays
		try {
			WebElement holiday = driver.findElement(By.cssSelector(".holiday_count"));
			System.out.println(holiday.getText());
		} catch (NoSuchElementException e) {
			System.out.println("0 Holidays");
			e.printStackTrace();
		}

		// List down all the weekends name (sat & sun) from the current date(excluding)
		List<WebElement> list = driver.findElements(By.cssSelector(".DayTiles__CalendarDaysSpan-sc-1xum02u-1.bwoYtA"));
		String[] array = new String[list.size()];
		int index = 0;
		for (WebElement web : list) {
			array[index++] = web.getText();
		}
		StringBuilder stringBuilder = new StringBuilder();

		// Append opening bracket
		stringBuilder.append("[");

		// Append each element of the array
		for (int i = 0; i < array.length; i++) {
			stringBuilder.append(array[i]);

			// Append comma and space if not the last element
			if (i < array.length - 1) {
				stringBuilder.append(", ");
			}
		}

		// Append closing bracket
		stringBuilder.append("]");

		// Return the formatted string
		System.out.println(stringBuilder.toString());

	}

}
