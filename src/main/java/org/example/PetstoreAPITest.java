package org.example;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class PetstoreAPITest {
    private final String petsStoreUrl = "https://petstore.swagger.io/v2";
    @Test
    public void createUser() {
        String testEmail = Utils.generateRandomEmail();

        String userJson = "{"
                + "\"id\": 0,"
                + "\"username\": \"otyut\","
                + "\"firstName\": \"Olena\","
                + "\"lastName\": \"Zhmkv\","
                + "\"email\": \"" + testEmail + "\","
                + "\"password\": \"Qwerty123\","
                + "\"phone\": \"000000000\","
                + "\"userStatus\": 0"
                + "}";

        given().baseUri(petsStoreUrl)
                .body(userJson)
                .header("Content-Type", "application/json")
                .when().post("/user")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void loginUser() {

        given().baseUri(petsStoreUrl)
                .queryParam("username", "otyut")
                .queryParam("password", "Qwerty123")
                .when().get("/user/login")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void createArrayOfUsers() {
        String testEmail = Utils.generateRandomEmail();

        String userJson = "["
                + "{"
                + "\"id\": 0,"
                + "\"username\": \"otyut\","
                + "\"firstName\": \"Olena\","
                + "\"lastName\": \"Zhmkv\","
                + "\"email\": \"" + testEmail + "\","
                + "\"password\": \"Qwerty123\","
                + "\"phone\": \"000000000\","
                + "\"userStatus\": 0"
                + "}"
                + "]";

        given().baseUri(petsStoreUrl)
                .body(userJson)
                .header("Content-Type", "application/json")
                .when().post("/user/createWithArray")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract().response();
    }

    @Test
    public void logoutUser() {

        given().baseUri(petsStoreUrl)
                .when().get("/user/logout")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void addPet() {

        String petJson = "{"
                + "\"id\": 0,"
                + "\"category\": {"
                + "  \"id\": 0,"
                + "  \"name\": \"pug\""
                + "},"
                + "\"name\": \"nolly\","
                + "\"photoUrls\": ["
                + "  \"photoUrlExample\""
                + "],"
                + "\"tags\": ["
                + "  {"
                + "    \"id\": 0,"
                + "    \"name\": \"tagName\""
                + "  }"
                + "],"
                + "\"status\": \"available\""
                + "}";

        given().baseUri(petsStoreUrl)
                .body(petJson)
                .header("Content-Type", "application/json")
                .when().post("/pet")
                .then().assertThat().statusCode(200)
                .extract();
    }

    @Test
    public void uploadPetImage() {
        File file = new File("src/img/petImage.png");

        given().baseUri(petsStoreUrl)
                .multiPart("file", file,"multipart/form-data")
                .when().post("/pet/5/uploadImage")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void updatePet() {
        given().baseUri(petsStoreUrl)
                .contentType("application/x-www-form-urlencoded")
                .body("name=Holly&status=available")
                .when().post("/pet/6")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void deletePet() {
        given().baseUri(petsStoreUrl)
                .when().delete("/pet/5")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }
}
