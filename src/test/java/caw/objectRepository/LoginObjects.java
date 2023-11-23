package caw.objectRepository;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginObjects {
	WebDriver driver;
	
	public LoginObjects(WebDriver driver1) {
		driver = driver1;
		PageFactory.initElements(driver,this);
	}
	
	@FindBy(xpath="//h1[text()='Dynamic HTML TABLE Tag']")
	private WebElement actualHeading;

	@FindBy(xpath="//summary[text()='Table Data']")
	private WebElement tableData;
	
	@FindBy(id="jsondata")
	private WebElement jsonData;
	
	@FindBy(id="refreshtable")
	private WebElement refreshtable;
	
	@FindBy(xpath="//table[@id='dynamictable']/tr/td[2]")
	private List<WebElement> webTable;
	
	public WebElement getActualHeading() {
		return actualHeading;
	}

	public WebElement getTableData() {
		return tableData;
	}

	public WebElement getJsonData() {
		return jsonData;
	}

	public WebElement getRefreshtable() {
		return refreshtable;
	}

	public List<WebElement> getWebTable() {
		return webTable;
	}
}
