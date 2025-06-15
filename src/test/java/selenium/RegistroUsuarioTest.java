package selenium;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistroUsuarioTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:8080/register";

    @BeforeEach
    public void setupTest() {
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
    public void testRegistroExitoso() {
        // --- Paso 1 ---
        driver.findElement(By.id("nombre")).sendKeys("Juan");
        driver.findElement(By.id("apellido1")).sendKeys("Pérez");
        driver.findElement(By.id("apellido2")).sendKeys("Gómez");
        driver.findElement(By.id("correo")).sendKeys("juan.perez@gmail.com");
        driver.findElement(By.id("passwd")).sendKeys("Abcdef1!");
        driver.findElement(By.id("confirm_passwd")).sendKeys("Abcdef1!");

        // **Nuevo**: marcar checkboxes requeridos
        driver.findElement(By.id("aceptaTerminos")).click();
        driver.findElement(By.id("aceptaPrivacidad")).click();

        // pulsar siguiente y esperar al paso 2
        driver.findElement(By.id("next-btn")).click();
        wait.until(ExpectedConditions.textToBe(By.id("step-label"), "Paso 2 de 3"));

        // --- Paso 2 ---
        driver.findElement(By.id("fechaNac")).sendKeys("1990-05-15");
        new Select(driver.findElement(By.id("genero"))).selectByValue("1");
        driver.findElement(By.id("telefono")).sendKeys("600123456");
        driver.findElement(By.id("calle")).sendKeys("Calle Falsa");
        driver.findElement(By.id("numero")).sendKeys("123");
        driver.findElement(By.id("localidad")).sendKeys("Madrid");
        driver.findElement(By.id("codigoPostal")).sendKeys("28001");
        driver.findElement(By.id("provincia")).sendKeys("Madrid");
        driver.findElement(By.id("pais")).sendKeys("España");

        driver.findElement(By.id("next-btn")).click();
        wait.until(ExpectedConditions.textToBe(By.id("step-label"), "Paso 3 de 3"));

            // --- Paso 3 ---
        List<WebElement> intereses = driver.findElements(By.cssSelector(".select-interes"));

        for (int idx : new int[]{0, 2}) {
            WebElement interes = intereses.get(idx);

            wait.until(ExpectedConditions.elementToBeClickable(interes));

            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", interes);

            new Actions(driver).moveToElement(interes).click().perform();
        }

        // por último pulsar “Terminar” y comprobar redirección…
        driver.findElement(By.id("next-btn")).click();
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/"));
        assertEquals("http://localhost:8080/", driver.getCurrentUrl());
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
