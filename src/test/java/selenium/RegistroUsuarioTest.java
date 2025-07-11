package selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import es.laboticademar.webstore.WebstoreApplication;

@SpringBootTest(
    classes = WebstoreApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistroUsuarioTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    public void setupTest() {
        this.baseUrl = "http://localhost:" + port + "/register";

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*");
        opts.addArguments("--user-data-dir=/tmp/selenium-profile-" + UUID.randomUUID());
        driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().setSize(new Dimension(1920, 1080));  // o .maximize()
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(baseUrl);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    

    @Test
    public void testCamposObligatoriosPaso1() {
        // No rellenamos nada y pulsamos Siguiente
        driver.findElement(By.id("next-btn")).click();

        WebElement warning = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("warning-msg")));
        assertTrue(warning.getText().contains("Se deben rellenar los campos obligatorios"));

        // El primer campo obligatorio debe llevar borde rojo
        WebElement nombre = driver.findElement(By.id("nombre"));
        String borderClass = nombre.getAttribute("class");
        assertTrue(borderClass.contains("border-red-500"));
    }

   @Test
   public void testEmailInvalidoPaso1() {
       // Rellenar campos obligatorios de paso 1
       driver.findElement(By.id("nombre")).sendKeys("Ana");
       driver.findElement(By.id("apellido1")).sendKeys("López");
       driver.findElement(By.id("correo")).sendKeys("ana.lopez@dominio-no-valido.com");
       driver.findElement(By.id("passwd")).sendKeys("Abcdef1!");
       driver.findElement(By.id("confirm_passwd")).sendKeys("Abcdef1!");

       // **Marcar los términos y la política de privacidad (requeridos)**
       driver.findElement(By.id("aceptaTerminos")).click();
       driver.findElement(By.id("aceptaPrivacidad")).click();

       // Pulsar Siguiente para que se ejecute la validación de email
       driver.findElement(By.id("next-btn")).click();

       // Esperar mensaje de “correo no válido”
       WebElement warning = wait.until(
           ExpectedConditions.visibilityOfElementLocated(By.id("warning-msg")));
       assertTrue(warning.getText().contains("Dirección de correo no válida"));

       // Verificar que el campo correo tenga la clase de borde rojo
       WebElement correo = driver.findElement(By.id("correo"));
       assertTrue(correo.getAttribute("class").contains("border-red-500"));
   }


    @Test
    public void testPasswordRequisitosPaso1() {
        // Paso 1: rellenar datos básicos
        driver.findElement(By.id("nombre")).sendKeys("Luis");
        driver.findElement(By.id("apellido1")).sendKeys("Martín");
        driver.findElement(By.id("correo")).sendKeys("luis.martin@gmail.com");
        // contraseña demasiado corta y sin mayúsculas/números
        driver.findElement(By.id("passwd")).sendKeys("abc");
        driver.findElement(By.id("confirm_passwd")).sendKeys("abc");

        // **Marcar los términos y la política de privacidad (requeridos)**
        driver.findElement(By.id("aceptaTerminos")).click();
        driver.findElement(By.id("aceptaPrivacidad")).click();

        // Pulsar Siguiente para que salte la validación de passwords
        driver.findElement(By.id("next-btn")).click();

        // Esperar mensaje de error de contraseñas
        WebElement warning = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("warning-msg")));
        assertTrue(warning.getText().contains("Las contraseñas deben cumplir los requisitos"));

        // Verificar que al menos un requisito está marcado con “❌”
        List<WebElement> reqs = driver.findElements(By.cssSelector("#pwd-requirements li"));
        boolean algunoRojo = reqs.stream()
            .anyMatch(li -> li.getText().startsWith("❌"));
        assertTrue(algunoRojo);
    }

}
