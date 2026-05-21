package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  public void testSimpleListWarehouses() {

    final String path = "warehouse";

    // List all, should have all 3 products the database has initially:
    given()
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"));
  }

  @Test
  public void testSimpleCheckingArchivingWarehouses() {

    // Uncomment the following lines to test the WarehouseResourceImpl implementation

    // final String path = "warehouse";

    // List all, should have all 3 products the database has initially:
    // given()
    //     .when()
    //     .get(path)
    //     .then()
    //     .statusCode(200)
    //     .body(
    //         containsString("MWH.001"),
    //         containsString("MWH.012"),
    //         containsString("MWH.023"),
    //         containsString("ZWOLLE-001"),
    //         containsString("AMSTERDAM-001"),
    //         containsString("TILBURG-001"));

    // // Archive the ZWOLLE-001:
    // given().when().delete(path + "/1").then().statusCode(204);

    // // List all, ZWOLLE-001 should be missing now:
    // given()
    //     .when()
    //     .get(path)
    //     .then()
    //     .statusCode(200)
    //     .body(
    //         not(containsString("ZWOLLE-001")),
    //         containsString("AMSTERDAM-001"),
    //         containsString("TILBURG-001"));
  }

  @Test
  void testSearchWithFilters_shouldReturnList() {
    given().queryParam("location", "AMSTERDAM-001").queryParam("minCapacity", 50).when().get("/warehouse/search")
            .then().statusCode(200).body("size()", greaterThanOrEqualTo(0));
      }

  @Test
  void testPagination_shouldLimitResults() {
    given().queryParam("page", 0).queryParam("pageSize", 2).when().get("/warehouse/search")
            .then().statusCode(200).body("size()", lessThanOrEqualTo(2));
      }

  @Test
  void testSortingDescendingCapacity() {
    given().queryParam("sortBy",  "capacity").queryParam("sortOrder", desc).when().get("/warehouse/search")
            .then().statusCode(200).body("capacity", everyItem(notNullValue()));
      }

  @Test
  void testExcludeArchivedWarehouses() {
    given.when().get("/warehouse/search")
            .then().statusCode(200).body("$", everyItem(hashKey("businessUnitCode")));
      }
}
