import CustomListeners.TestNGListeners;
import Utils.DataReader.JsonReader;
import Utils.DataReader.PropertyReader;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Epic("PetStore API")
@Feature("Store Operations")
@Listeners(TestNGListeners.class)
public class StoreTest
{

    JsonReader testData;
    RequestSpecification request;
    Response response;
    @BeforeClass
    public void preCondition()
    {
        RestAssured.baseURI = PropertyReader.getProperty("baseURI");
        testData = new JsonReader("store-data");
    }
    @BeforeMethod
    public void setup()
    {
        request = RestAssured.given();
    }


    @Test(priority = 0)
    @Story("Place Order")
    @Description("Create a new order for a pet while user is logged in")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyCreatingOrderForPetWhileLoggedIn()
    {
       request = RestAssured.given()
               .basePath("/store/order")
               .body(testData.getJsonData("allOrderBody"))
               .header("Content-Type","application/json");
       response = request.post();
       response.prettyPrint();
       response.then().statusCode(200);
    }

    @Test(priority = 0)
    @Story("Place Order")
    @Description("Attempt to create an order while user is logged out")
    @Severity(SeverityLevel.NORMAL)
    public void verifyCreatingOrderForPetWhileLoggedOut()
    {
        request = RestAssured.given()
                .basePath("/store/order")
                .body(testData.getJsonData("allOrderBody"))
                .header("Content-Type","application/json");
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(400);
    }

    @Test(priority = 1)
    @Story("Get Order")
    @Description("Find purchase order by valid ID range")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFindingOrderByValidOrderIDRange()
    {
        request = RestAssured.given()
                .basePath("/store/order/" + testData.getJsonData("orderBody.id"));
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 1)
    @Story("Get Order")
    @Description("Verify error handling when fetching order with invalid ID range")
    @Severity(SeverityLevel.NORMAL)
    public void verifyFindingOrderByInvalidOrderIDRange()
    {
        request = RestAssured.given()
                .basePath("/store/order/15");
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(400);
    }

    @Test(priority = 1)
    @Story("Get Inventory")
    @Description("Returns pet inventories by status")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFindingInventoryByStatus()
    {
        request = RestAssured.given()
                .basePath("/store/inventory");
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 2)
    @Story("Delete Order")
    @Description("Delete purchase order by valid ID")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyDeletingOrderByValidOrderID()
    {
        request = RestAssured.given()
                .basePath("/store/order/" + testData.getJsonData("orderBody.id"));
        response = request.delete();
        response.then().statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("orderBody.id")));
    }

    @Test(priority = 2)
    @Story("Delete Order")
    @Description("Verify error handling when deleting order with invalid ID type")
    @Severity(SeverityLevel.NORMAL)
    public void verifyDeletingOrderByInvalidOrderID()
    {
        request = RestAssured.given()
                .basePath("/store/order/String");
        response = request.delete();
        response.prettyPrint();
        response.then().statusCode(400);
    }

    @Test(priority = 2)
    @Story("Delete Order")
    @Description("Verify error handling when deleting a non-existent order ID")
    @Severity(SeverityLevel.NORMAL)
    public void verifyDeletingOrderByNotExistOrderID()
    {
        request = RestAssured.given()
                .basePath("/store/order/7");
        response = request.delete();
        response.prettyPrint();
        response.then().statusCode(404);
    }
}
