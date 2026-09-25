package com.orangehrm.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.net.URI;

public final class OrangeHrmEmployeeApiVerifier {
    private OrangeHrmEmployeeApiVerifier() {
    }

    public static void assertEmployee(WebDriver driver, String baseUrl, int empNumber,
                                      String employeeId, String firstName, String lastName) {
        Assert.assertTrue(empNumber > 0, "empNumber must be a positive internal record number");
        Response response = get(driver, baseUrl, empNumber);
        Assert.assertEquals(response.statusCode(), 200, "Employee GET must succeed: " + response.asString());
        Assert.assertTrue(response.contentType().toLowerCase().contains("application/json"),
                "Employee GET must return JSON");
        Assert.assertEquals(response.jsonPath().getInt("data.empNumber"), empNumber);
        Assert.assertEquals(response.jsonPath().getString("data.employeeId"), employeeId);
        Assert.assertEquals(response.jsonPath().getString("data.firstName"), firstName);
        Assert.assertEquals(response.jsonPath().getString("data.lastName"), lastName);
    }

    public static void assertDeleted(WebDriver driver, String baseUrl, int empNumber) {
        Response response = get(driver, baseUrl, empNumber);
        Assert.assertEquals(response.statusCode(), 404,
                "Deleted employee must no longer be retrievable: " + response.asString());
    }

    private static Response get(WebDriver driver, String baseUrl, int empNumber) {
        URI origin = URI.create(baseUrl);
        URI current = URI.create(driver.getCurrentUrl());
        Assert.assertEquals(current.getScheme(), origin.getScheme(), "Browser and API scheme differ");
        Assert.assertEquals(current.getAuthority(), origin.getAuthority(), "Browser and API host differ");

        io.restassured.specification.RequestSpecification request = RestAssured.given()
                .baseUri(baseUrl.replaceAll("/+$", ""))
                .accept("application/json")
                .redirects().follow(false);
        for (Cookie cookie : driver.manage().getCookies()) {
            request.cookie(cookie.getName(), cookie.getValue());
        }
        return request.get("/web/index.php/api/v2/pim/employees/{empNumber}", empNumber);
    }
}
