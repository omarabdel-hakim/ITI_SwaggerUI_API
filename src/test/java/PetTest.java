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

import java.io.File;

@Listeners(TestNGListeners.class)
@Epic("PetStore API")
@Feature("Pet Operations")
public class PetTest
{
    JsonReader testData;
    RequestSpecification request;
    Response response;
    @BeforeClass
    public void preCondition()
    {
        RestAssured.baseURI = PropertyReader.getProperty("baseURI");
        testData = new JsonReader("pet-data");
    }
    @BeforeMethod
    public void setup()
    {
        request = RestAssured.given();
    }

    @Test(priority = 0)
    @Story("Create Pet")
    @Description("Verify adding a new pet to the store with valid status")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAddingNewPetToStoreWithValidStatus()
    {
        request.basePath("/pet").body(testData.getJsonData("allPetBody"))
                .header("Content-Type","application/json");
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 0)
    @Story("Create Pet")
    @Description("Verify API behavior when adding a pet with invalid status/body")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAddingNewPetToStoreWithInvalidStatus()
    {
        request.basePath("/pet").body( testData.getJsonData("allInvalidPetBody"))
                .header("Content-Type","application/json");
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(405);
    }

    @Test(priority = 1)
    @Story("Find Pet")
    @Description("Find pet by an existing ID")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFindingPetsByExistID()
    {
        request.basePath("/pet/" + testData.getJsonData("petBody.id"));
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 1)
    @Story("Find Pet")
    @Description("Verify 404 error when searching for a non-existent pet ID")
    @Severity(SeverityLevel.NORMAL)
    public void verifyFindingPetsByNotExistID()
    {
        request.basePath("/pet/" + testData.getJsonData("notExistID"));
        response = request.get();
        response.then().statusCode(404);
    }

    @Test(priority = 1)
    @Story("Find Pet")
    @Description("Verify error handling for invalid ID format")
    @Severity(SeverityLevel.MINOR)
    public void verifyFindingPetsByInvalidID()
    {
        request.basePath("/pet/" + testData.getJsonData("invalidID"));
        response = request.post();
        response.then().statusCode(400);
    }

    @Test(priority = 2)
    @Story("Find Pet")
    @Description("Find pets by valid status (available)")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyFindingPetsByValidStatus()
    {
        request.basePath("/pet/findByStatus")
                .queryParam("available");       //available, pending, sold
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 2)
    @Story("Find Pet")
    @Description("Verify behavior when searching by invalid status")
    @Severity(SeverityLevel.NORMAL)
    public void verifyFindingPetsByInvalidStatus()
    {
        request.basePath("/pet/findByStatus")
                .queryParam("coming");       //available, pending, sold
        response = request.get();
        response.prettyPrint();
        response.then().statusCode(400);
    }

    @Test(priority = 3)
    @Story("Pet Image Upload")
    @Description("Upload an image for an existing pet")
    @Severity(SeverityLevel.NORMAL)
    public void verifyUploadingImage()
    {
    request.basePath("/pet/"+testData.getJsonData("petBody.id")+"/uploadImage")
            .multiPart("file", new File("src/test/resources/test-data/dog.jpg"));
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 4)
    @Story("Update Pet")
    @Description("Update pet details using form parameters with valid ID")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUpdatingPetInStoreWithValidID()
    {
        request.basePath("/pet/"+testData.getJsonData("petBody.id"))
                .formParam("name","NewPetName")
                .formParam("status","pending");
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("petBody.id")));
    }

    @Test(priority = 4)
    @Story("Update Pet")
    @Description("Verify error when updating pet with invalid ID format")
    @Severity(SeverityLevel.NORMAL)
    public void verifyUpdatingPetInStoreWithInvalidID()
    {
        request.basePath("/pet/" + testData.getJsonData("invalidID"))
                .formParam("name","NewPetName")
                .formParam("status","pending");
        response = request.post();
        response.prettyPrint();
        response.then().statusCode(400)
                .body("message", Matchers.equalTo("Invalid ID supplied"));
    }

    @Test(priority = 4)
    @Story("Update Pet")
    @Description("Update entire pet object via PUT request")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyUpdatingWithExistPetID()
    {
        request.basePath("/pet")
                .header("Content-Type","application/json")
                .body(testData.getJsonData("allUpdatedPetBody"));
        response = request.put();
        response.prettyPrint();
        response.then().statusCode(200)
                .body("name", Matchers.equalTo("Rockey"));
    }

    @Test(priority = 4)
    @Story("Update Pet")
    @Description("Verify error handling when updating with invalid pet structure")
    @Severity(SeverityLevel.NORMAL)
    public void verifyUpdatingWithInvalidPetID()
    {
        request.basePath("/pet")
                .header("Content-Type","application/json")
                .body(testData.getJsonData("allInvalidUpdatedPetBody"));
        response = request.put();
        response.prettyPrint();
        response.then().statusCode(400)
                .body("message", Matchers.equalTo("bad input"));
    }

    @Test(priority = 5)
    @Story("Delete Pet")
    @Description("Delete an existing pet by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyDeletingPetWithValidID()
    {
        request.basePath("/pet/" + testData.getJsonData("petBody.id"))
                .header("api_key","string") ;
        response = request.delete();
        response.then().statusCode(200)
                .body("message", Matchers.equalTo(testData.getJsonData("petBody.id")));
    }

    @Test(priority = 6)
    @Story("Delete Pet")
    @Description("Verify behavior when trying to delete a non-existent pet")
    @Severity(SeverityLevel.NORMAL)
    public void verifyDeletingPetWithNotExistID()
    {
        request.basePath("/pet/" + testData.getJsonData("petBody.id"))
                .header("api_key","string") ;
        response = request.delete();
        response.then().statusCode(404);
    }

    @Test(priority = 6)
    @Story("Delete Pet")
    @Description("Verify error handling when deleting with invalid ID format")
    @Severity(SeverityLevel.MINOR)
    public void verifyDeletingPetWithInvalidID()
    {
        request.basePath("/pet/" + testData.getJsonData("invalidID"))
                .header("api_key","string") ;
        response = request.delete();
        response.prettyPrint();
        response.then().statusCode(400)
                .body("message", Matchers.equalTo("bad input"));
    }
}
