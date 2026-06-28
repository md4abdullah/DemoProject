import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		//create address
		String response = given().log().all().queryParam("key","qaclick123").headers("Content-Type", "application/json")
		.body(Payload.addPlace()).when().post("/maps/api/place/add/json")
		        .then().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server","Apache/2.4.52 (Ubuntu)")
		        .extract().response().asString();
		
		JsonPath js = new JsonPath(response); //parsing the json
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		//update
		String newAddress = "70 Summer walk, USA";
		 given().log().all().queryParam("key","qaclick123").headers("Content-Type", "application/json")
		 .body("{\r\n"
		 		+ "\"place_id\":\""+placeId+"\",\r\n"
		 		+ "\"address\":\""+newAddress+"\",\r\n"
		 		+ "\"key\":\"qaclick123\"\r\n"
		 		+ "}\r\n"
		 		+ "").when().put("/maps/api/place/update/json")
		 .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));
		 
		 //get address
		String responseAdd =  given().log().all().queryParam("place_id",placeId).queryParam("key","qaclick123").headers("Content-Type", "application/json")
		 .when().get("/maps/api/place/get/json")
		 .then().log().all().statusCode(200).extract().response().asString();
		
		System.out.println(responseAdd);
		
		JsonPath js1 = new JsonPath(responseAdd);
		String actualAddress = js1.getString("address");
		Assert.assertEquals(newAddress, actualAddress);		

	}

}
