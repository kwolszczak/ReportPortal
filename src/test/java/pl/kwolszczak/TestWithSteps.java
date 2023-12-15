package pl.kwolszczak;

import com.epam.reportportal.annotations.Step;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kwolszczak.page.OrderingSimulator;

import java.util.List;

@ExtendWith(ReportPortalExtension.class)
class TestWithSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWithSteps.class);

   @Test
   void orderProductsTest() {

       final Integer productCount = 5;
       final Double price = 3.0;
       final Double totalPrice = price * productCount;

       navigateToMainPage();
       login();
       navigateToProductsPage();
       addProductToCart(productCount);
       pay(totalPrice);
       logout();
   }

    @Step
    public void navigateToMainPage() {
        LOGGER.info("Main page displayed");
    }

    @Step
    public void login() {
        OrderingSimulator.logIn();
        LOGGER.info("User logged in");
    }

    @Step
    public void navigateToProductsPage() {
        List<String> products = OrderingSimulator.getProducts();
        LOGGER.info("Products page opened");
    }

    @Step("Add {count} products to the cart")
    public void addProductToCart(Integer count) {
        String product = clickOnProduct();
        selectProductsCount(count);
        clickCartButton(product, count);
    }

    @Step
    private String clickOnProduct() {
        LOGGER.info("Product click event");
        return OrderingSimulator.chooseProduct();
    }

    @Step("{method} with {count} products")
    private void selectProductsCount(Integer count) {
        LOGGER.info(count + " products selected");
    }

    @Step("{productCount} products added")
    private void clickCartButton(String product, Integer productCount) {
        OrderingSimulator.addProduct(product, productCount);
        LOGGER.info(productCount + " products added to the cart");
        Assertions.assertEquals(5, productCount.intValue());
    }

    @Step("Payment step with price = {totalPrice}")
    public void pay(Double totalPrice) {
        OrderingSimulator.doPayment(totalPrice);
        LOGGER.info("Successful payment");
    }

    @Step
    public void logout() {
        OrderingSimulator.logOut();
        LOGGER.info("User logged out");
    }
}