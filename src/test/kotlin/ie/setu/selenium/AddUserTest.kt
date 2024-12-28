import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

class AddUserTest {
    private var driver: WebDriver? = null
    private var vars: Map<String, Any>? = null
    var js: JavascriptExecutor? = null

    @Before
    fun setUp() {
        driver = ChromeDriver()
        js = driver as JavascriptExecutor
        vars = HashMap()
    }

    @After
    fun tearDown() {
        driver!!.quit()
    }

    @Test
    fun addUser() {
        driver!!["http://localhost:7001/"]
        driver!!.manage().window().size = Dimension(1552, 832)
        driver!!.findElement(By.linkText("Health Tracker")).click()
        driver!!.findElement(By.linkText("More Details...")).click()
        driver!!.findElement(By.cssSelector(".fa-plus")).click()
        driver!!.findElement(By.name("name")).click()
        driver!!.findElement(By.name("name")).sendKeys("Abraham")
        driver!!.findElement(By.name("email")).sendKeys("John")
        driver!!.findElement(By.name("password")).click()
        driver!!.findElement(By.name("password")).sendKeys("AJ123")
        driver!!.findElement(By.cssSelector(".card-body > .btn")).click()
        driver!!.findElement(By.linkText("Abraham (John)")).click()
        driver!!.findElement(By.linkText("Health Tracker")).click()
    }
}