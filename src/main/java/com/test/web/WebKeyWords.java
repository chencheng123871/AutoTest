package com.test.web;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.test.common.AutoLog;

public class WebKeyWords {
	public WebDriver driver;

	/**
	 * 根据type的类型打开对应的浏览器
	 * 
	 * @param type
	 */
	public void openBrowser(String type) {
		switch (type) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			ChromeOptions option = new ChromeOptions();
			option.addArguments("--user-data-dir=D:\\chromedata");
			option.addArguments("--start-maximized");
			driver = new ChromeDriver(option);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			break;
		case "firefox":
			System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Firefox\\firefox.exe");
			System.setProperty("webdriver.gecko.driver", "Drivers/geckodriver.exe");

			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			break;
		default:
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			ChromeOptions option1 = new ChromeOptions();
			option1.addArguments("--user-data-dir=D:\\chromedata");
			option1.addArguments("--start-maximized");
			driver = new ChromeDriver(option1);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			break;
		}
	}

	/**
	 * 访问指定的url
	 * 
	 * @param url
	 */
	public void vistWeb(String url) {
		driver.get(url);
	}

	/**
	 * 根据xpath定位元素并点击
	 * 
	 * @param xpath
	 */
	public void click(String xpath) {
		WebElement ele = driver.findElement(By.xpath(xpath));
		ele.click();
	}

	public void inputContent(String xpath, String content) {
		WebElement ele = driver.findElement(By.xpath(xpath));
		ele.clear();
		ele.sendKeys(content);
		;

	}

	/**
	 * 鼠标悬停在某个元素上面
	 * 
	 * @param xpath
	 */
	public void hover(String xpath) {
		try {
			Actions act = new Actions(driver);
			// 记得在act对象中的方法编辑完之后，加上.perform()方法调用，让动作执行。
			act.moveToElement(driver.findElement(By.xpath(xpath))).perform();
			AutoLog.log.info("在" + xpath + "元素上悬停");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("悬停操作失败");
			AutoLog.log.error(e, e.fillInStackTrace());
		}
	}

	/**
	 * 通过浏览器的标题切换浏览器窗口的方法
	 * 
	 * @param exTitle 预期的浏览器窗口标题
	 */
	public void switchWindowByTitle(String exTitle) {
		try {
			Set<String> handles = driver.getWindowHandles();
			System.out.println(handles);
			String targetHandle = "";
			// 遍历所有的句柄，判断这个句柄对应的浏览器窗口标题是否是预期值
			for (String s : handles) {
				// 切换到各个窗口句柄，获取其标题，判断是否等于预期值
				if (driver.switchTo().window(s).getTitle().equals(exTitle)) {
					// 如果是，则说明找到了需要切换窗口的句柄
					targetHandle = s;
					break;
				}
			}
			driver.switchTo().window(targetHandle);
			AutoLog.log.info("切换浏览器到" + exTitle + "窗口");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 切换frame框架通过名字
	 * 
	 * @param framename
	 */
	public void switchIframe(String framename) {
		try {
			driver.switchTo().frame(framename);
			AutoLog.log.info("切换" + framename + "iframe成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("切换到" + framename + "iframe失败");
			AutoLog.log.error(e, e.fillInStackTrace());
		}
	}

	/**
	 * 通过frame的xpath切换框架中
	 * 
	 * @param xpath
	 */
	public void switchIframeAsele(String xpath) {
		try {
			WebElement frameElement = driver.findElement(By.xpath(xpath));
			driver.switchTo().frame(frameElement);
			AutoLog.log.info("切换" + xpath + "iframe成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("切换到" + xpath + "iframe失败");
			AutoLog.log.error(e, e.fillInStackTrace());
		}
	}

	/**
	 * 切回到默认的最外层框架中
	 */
	public void switchToRoot() {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("切换到网页最外层失败");
			AutoLog.log.error(e, e.fillInStackTrace());
		}
	}

}
