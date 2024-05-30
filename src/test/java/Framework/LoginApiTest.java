package Framework;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import POJO.AddProductResponse;
import POJO.CreateProductRequest;
import POJO.LoginRequest;
import POJO.LoginResponse;
import POJO.OrderDetails;

public class LoginApiTest {

	public static void main(String[] args) {
		RequestSpecification req=new RequestSpecBuilder ().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		LoginRequest lp=new LoginRequest();
		lp.setUserEmail("choudharyabantika348@gmail.com");
		lp.setUserPassword("Aban2001@");
		RequestSpecification reqRes=given().log().all().spec(req).body(lp);
		
		LoginResponse response=reqRes.post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
		
		String token=response.getToken();
		System.out.println("token "+token);
		String userId=response.getUserId();
		System.out.println("userId "+userId);
		
		//ADD Product
		RequestSpecification reqAddProduct=new RequestSpecBuilder ().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization",token).build();
		
		RequestSpecification addProduct=given().log().all().spec(reqAddProduct)
		.param("productName","Quartz Penadant")
		.param("productAddedBy",userId)
		.param("productCategory","fashion")
		.param("productSubCategory","jewellry")
		.param("productPrice","1120")
		.param("productDescription","Giva Exclusive")
		.param("productFor", "Women")
		.multiPart("productImage",new File("C:\\Users\\2303514\\Downloads\\jewellry.jpg"));
		
		AddProductResponse responseAdd=addProduct.post("/api/ecom/product/add-product").then().log().all().extract().response().as(AddProductResponse.class);
		String productId=responseAdd.getProductId();
		System.out.println("productId "+productId);
		System.out.println("message"+responseAdd.getMessage());
		
		//Create order
		RequestSpecification reqOrderProduct=new RequestSpecBuilder ().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).
				addHeader("Authorization",token).build();
		
		OrderDetails orderDetail=new OrderDetails();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		List<OrderDetails> mylist=new ArrayList<OrderDetails>();
		mylist.add(orderDetail);
		CreateProductRequest corder=new CreateProductRequest();
		corder.setOrders(mylist);
//		System.out.println("list Added successfully");
		
		RequestSpecification orderProduct=given().spec(reqOrderProduct).body(corder);
		
		String orderResponse=orderProduct.post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		System.out.println(orderResponse);
		
		//Delete the product
		RequestSpecification reqdelProduct=new RequestSpecBuilder ().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization",token).build();
		RequestSpecification delProduct=given().relaxedHTTPSValidation().spec(reqdelProduct);
		String msg=delProduct.delete("/api/ecom/product/delete-product/"+productId).then().log().all().extract().response().asString();
		System.out.println(msg);
		

	}

}
