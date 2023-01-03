package com.restful.booker.crudtests;

import com.restful.booker.steps.BookingsSteps;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;

@RunWith(SerenityRunner.class)
public class BookingsCRUDTest extends TestBase {

    public static String username = "admin";
    public static String password = "password123";
    public static String firstname = "Koms"+ TestUtils.getRandomValue();
    public static String lastname = "Vac"+TestUtils.getRandomValue();
    public static Integer totalprice = 129;
    public static Boolean depositpaid = true;
    public static String additionalneeds = "Breakfast";
    public static int bookingID;
    public static String token;

    @Steps
    BookingsSteps bookingsSteps;

    @Title("This will Auth user")
    @Test
    public void test001() {
        ValidatableResponse response = bookingsSteps.authorizeUser(username, password);
        response.log().all().statusCode(200);
        HashMap<?,?> tokenMap= response.log().all().extract().path("");
        Assert.assertThat(tokenMap,hasKey("token"));
        System.out.println(token);
    }

    @Title("This test will create a new Booking")
    @Test
    public void test002() {
        HashMap<Object, Object> bookingsDatesMap = new HashMap<>();
        bookingsDatesMap.put("checkin", "2015-12-01");
        bookingsDatesMap.put("checkout", "2015-12-05");
        ValidatableResponse response = bookingsSteps.createBooking(firstname, lastname,totalprice,depositpaid,bookingsDatesMap,additionalneeds);
        response.log().all().statusCode(200);
        bookingID= response.log().all().extract().path("bookingid");
        HashMap<?,?>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }

    @Title("This test will Update the booking")
    @Test
    public void test003() {
        HashMap<Object, Object> bookingsDatesMap = new HashMap<>();
        bookingsDatesMap.put("checkin", "2015-12-01");
        bookingsDatesMap.put("checkout", "2015-12-05");
        firstname = firstname+"_updated";
        ValidatableResponse response = bookingsSteps.updateBooking(bookingID,firstname, lastname,totalprice,depositpaid,bookingsDatesMap,additionalneeds);
        response.log().all().statusCode(200);
        HashMap<?,?>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }

    @Title("This test will Deleted the booking")
    @Test
    public void test004() {

        ValidatableResponse response = bookingsSteps.deleteBooking(bookingID);
        response.log().all().statusCode(201);
        ValidatableResponse response1 = bookingsSteps.getBookingByID(bookingID);
        response1.log().all().statusCode(404);

    }
}
