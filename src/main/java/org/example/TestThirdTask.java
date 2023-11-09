package org.example;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class TestThirdTask {
    @Test
    public void createUser() {
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        String testEmail = "olenkatyut+test_" + uniqueId + "@gmail.com";

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

        given().baseUri("https://petstore.swagger.io/v2")
                .body(userJson)
                .header("Content-Type", "application/json")
                .when().post("/user")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void loginUser() {

        given().baseUri("https://petstore.swagger.io/v2")
                .queryParam("username", "otyut")
                .queryParam("password", "Qwerty123")
                .when().get("/user/login")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void createArrayOfUsers() {
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        String testEmail = "olenkatyut+test_" + uniqueId + "@gmail.com";

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

        given().baseUri("https://petstore.swagger.io/v2")
                .body(userJson)
                .header("Content-Type", "application/json")
                .when().post("/user/createWithArray")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract().response();
    }

    @Test
    public void logoutUser() {

        given().baseUri("https://petstore.swagger.io/v2")
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

        given().baseUri("https://petstore.swagger.io/v2")
                .body(petJson)
                .header("Content-Type", "application/json")
                .when().post("/pet")
                .then().assertThat().statusCode(200)
                .extract();
    }

    @Test
    public void uploadPetImage() {
        File file = new File("src/img/petImage.png");

        given().baseUri("https://petstore.swagger.io/v2")
                .multiPart("file", file,"multipart/form-data")
                .when().post("/pet/5/uploadImage")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void updatePet() {
        given().baseUri("https://petstore.swagger.io/v2")
                .contentType("application/x-www-form-urlencoded")
                .body("name=Holly&status=available")
                .when().post("/pet/6")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }

    @Test
    public void deletePet() {
        given().baseUri("https://petstore.swagger.io/v2?")
                .when().delete("/pet/5")
                .then().assertThat().statusCode(200)
                .body("code", equalTo(200))
                .extract();
    }



}
