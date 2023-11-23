package caw.cawAssignment;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import caw.objectRepository.LoginObjects;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Login {

	WebDriver driver;
	String expectedHeading = "Dynamic HTML TABLE Tag";
	Properties props = new Properties();
	String browser, url, jsonFileLocation, errorMsg;
	LoginObjects loginObj;
	JavascriptExecutor jse;

	@BeforeMethod
	public void setUp() throws Exception {
		ScreenRecorderUtil.startRecord("setUp");
		FileInputStream fileInputStream = new FileInputStream(".\\src\\test\\java\\resources\\data.properties");
		props.load(fileInputStream);
		
		browser = props.getProperty("browser");
		url = props.getProperty("url");
		jsonFileLocation = props.getProperty("jsonFileLocation");
		errorMsg = props.getProperty("errorMsg");
		
		//Open browser
		if(browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("Internet Explorer")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		}
		else if(browser.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		else {
			System.out.println("Not a valid browser");
		}
		
		jse = (JavascriptExecutor)driver;
		
		loginObj = new LoginObjects(driver);
		//maximize screen
		driver.manage().window().maximize();
		
		// 1- land on the given url
		driver.get(url);
	}

	@Test
	public void loginToWebsite() throws IOException, ParseException, InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		String actualHeading = loginObj.getActualHeading().getText();
		Assert.assertEquals(actualHeading, expectedHeading);

		// 2- Click on Table Data button
		loginObj.getTableData().click();
		
		WebElement jsonData = loginObj.getJsonData();
		
		Actions actions = new Actions(driver);
		actions.click(jsonData).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
		actions.keyDown(Keys.DELETE).keyUp(Keys.DELETE).perform();
		Thread.sleep(5000);
		
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(jsonFileLocation);
		Object obj = jsonParser.parse(reader);
		JSONArray arraydata = (JSONArray) obj;
		String stringData = arraydata.toJSONString();

		// 3 - Insert the json data
		loginObj.getJsonData().sendKeys(stringData);
		jse.executeScript("window.scrollBy(0,500)");
		// click on Refresh Table button
		Thread.sleep(3000);
		loginObj.getRefreshtable().click();

		//4 - The entered data will be populated in the table.
		List<WebElement> names = loginObj.getWebTable();
		
		int actualCountOfNames = names.size();
		
		ArrayList<String> arrList = new ArrayList<String>();
		for(int i=0; i<names.size(); i++)
		{
			arrList.add(names.get(i).getText());
		}
		
		int countOfMacthingNames = 0;
		
		for(String listEle : arrList) {
			if(stringData.contains(listEle)) {
				countOfMacthingNames = countOfMacthingNames+1;
			}
		}
		//5 - Both data should match
		Assert.assertEquals(countOfMacthingNames, actualCountOfNames, errorMsg);
		
		Thread.sleep(5000);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
		ScreenRecorderUtil.stopRecord();
	}
}
