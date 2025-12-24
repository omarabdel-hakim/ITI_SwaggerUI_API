import CustomListeners.TestNGListeners;
import Utils.DataReader.JsonReader;
import Utils.DataReader.PropertyReader;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Epic("PetStore API")
@Feature("User Operations")
@Listeners(TestNGListeners.class)
public class UserTest
{
    JsonReader testData;
    RequestSpecification request;
    Response response;

    @BeforeClass
    public void preCondition()
    {
        RestAssured.baseURI = PropertyReader.getProperty("baseURI");
        testData = new JsonReader("user-data");
    }
    @BeforeMethod
    public void setup()
    {
        request = RestAssured.given();
    }

    @Test(priority = 0)
    @Story("Create User")
    @Description("Create a new user with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyCreatingUser()
    {
        request.basePath("/user")
                .header("Content-Type","application/json")
                .body(testData.getJsonData("allRequestBody"));
        response = request.post();
        response.prettyPrint();
        response.then()
                .statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("requestBody.id")));
    }

    @Test(priority = 0)
    @Story("Create User")
    @Description("Create a list of users with given array")
    @Severity(SeverityLevel.NORMAL)
    public void verifyCreatingListOfUserWithArray()
    {
        request.basePath("/user/createWithArray")
                .header("Content-Type","application/json")
                .body(testData.getJsonData("arrayOfUser"));
        response = request.post();
        response.then()
                .statusCode(200)
                .body("message", Matchers.equalTo("ok"));
    }

    @Test(priority = 1)
    @Story("Authentication")
    @Description("Log user into the system")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoggingUserIn()
    {
        request.basePath("/user/login")
                .queryParam(testData.getJsonData("requestBody.username"))
                .queryParam(testData.getJsonData("requestBody.password"));
        response = request.get();
        response.then()
                .statusCode(200);
        Assert.assertTrue(response.jsonPath().getString("message").contains("logged in"));

    }

    @Test(priority = 4)
    @Story("Authentication")
    @Description("Log user out of the current session")
    @Severity(SeverityLevel.NORMAL)
    public void verifyLoggingUserOut()
    {
        request.basePath("/user/logout");
        Response response = request.get();
        response.then()
                .statusCode(200)
                .body("message", Matchers.equalTo("ok"));
    }

    @Test(priority = 2)
    @Story("Get User")
    @Description("Find user details by username")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFindingUserByUserName()
    {
        request.basePath("/user/"+ testData.getJsonData("requestBody.username"));
        response = request.get();
        response.prettyPrint();
        response.then()
                .statusCode(200)
                .body("id", Matchers.equalTo(Integer.parseInt(testData.getJsonData("requestBody.id") )));
    }

    @Test(priority = 3)
    @Story("Update User")
    @Description("Update existing user information")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUpdatingUser()
    {
        request.basePath("/user/"+ testData.getJsonData("requestBody.username"))
                .body(testData.getJsonData("allUpdatedRequestBody"))
                .header("Content-Type", "application/json");
        response = request.put();
        response.then()
                .statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("requestBody.id")));
    }

    @Test(priority = 5)
    @Story("Delete User")
    @Description("Delete user by username")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyDeletingUser()
    {
        request.basePath("/user/"+ testData.getJsonData("requestBody.username"));
        response = request.delete();
        response.then()
                .statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("requestBody.username")));
    }
}