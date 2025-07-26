package com.tco.requests;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.ArrayList;

import com.tco.misc.Distances;
import com.tco.misc.JSONValidator;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestDistancesRequest {

    private DistancesRequest test;

    @BeforeEach
    public void init(TestInfo testInfo) throws Exception {
        String testName = testInfo.getTestMethod().get().getName();

        Place place1 = new Place();
        place1.put("latitude", "100");
        place1.put("longitude", "100");
        Place place2 = new Place();
        place2.put("latitude", "50");
        place2.put("longitude", "50");
        Places places = new Places();
        places.add(place1);
        places.add(place2);

        if (testName.equals("test3CosineDistances")) {
            Place place3 = new Place();
            place3.put("latitude", "100");
            place3.put("longitude", "100");
            places.add(place3);
        }

        Double earthRadius = 6378.0;
        test = new DistancesRequest(places, earthRadius);
    }

    private void test(String request, Type type, boolean valid) {
        try {
            JSONValidator.validate(request, type);
            assertTrue(valid);
        } catch (Exception e) {
            assertFalse(valid);
        }
    }

    @Test
    @DisplayName("zack722: DistancesRequest should fail schema validation")
    public void testDistancesRequestFail() {
        test("{}", DistancesRequest.class, false);
    }

    @Test
    @DisplayName("zack722: There should be no schema for the JSONValidator class")
    public void testMissingSchema() {
        test("", JSONValidator.class, false);
    }

    @Test
    @DisplayName("zack722: An invalid schema results in validate() failing")
    public void testInvalidSchema() {
        try (MockedStatic<SchemaLoader> mockedSchemaLoader = mockStatic(SchemaLoader.class)) {
            mockedSchemaLoader.when(() -> SchemaLoader.load(any(JSONObject.class)))
                    .thenThrow(SchemaException.class);

            test("{\"requestType\":\"distances\"}", DistancesRequest.class, false);
        }
    }

    @Test
    @DisplayName("leoo: DistancesRequest correctly creates list of 2 distances based on CosinesCalculator")
    public void test2CosinesDistances() throws Exception {
        Distances testDistances = new Distances(new ArrayList<>(Arrays.asList(5226L, 5226L)));
        setFormula(test, "cosines");
        test.calculateDistances();
        assertTrue(test.compareDistances(testDistances));
    }

    @Test
    @DisplayName("leoo: DistancesRequest correctly creates list of 3 distances based on CosinesCalculator")
    public void test3CosineDistances() throws Exception {
        Distances testDistances = new Distances(new ArrayList<>(Arrays.asList(5226L, 5226L, 0L)));
        setFormula(test, "cosines");
        test.calculateDistances();
        assertTrue(test.compareDistances(testDistances));
    }

    @Test
    @DisplayName("cougar: Testing build response to make sure it is not null")
    void testBuildResponseInitializedDistances() throws Exception {
        test.buildResponse();
        assertTrue(test.distancesNotNull());
    }

    // @Test
    // @DisplayName("zack722: Testing that a null formula defaults to
    // VincentyCalculator")
    // void testFormulaNullDefaultsToVincenty() throws Exception {
    // Places places = new Places();
    // places.add(new Place("40.748817", "-73.985428"));
    // places.add(new Place("34.052235", "-118.243683"));

    // DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
    // setFormula(distancesRequest, null);
    // distancesRequest.buildResponse();

    // assertFalse(distancesRequest.distancesNotNull());
    // }

    // @Test
    // @DisplayName("zack722: Testing that an empty formula string throws
    // IllegalArgumentException")
    // void testFormulaEmptyThrowsException() throws Exception {
    // Places places = new Places();
    // places.add(new Place("40.748817", "-73.985428"));
    // places.add(new Place("34.052235", "-118.243683"));

    // DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
    // setFormula(distancesRequest, "");

    // BadRequestException exception = assertThrows(BadRequestException.class,
    // distancesRequest::buildResponse);
    // assertEquals("Formula cannot be an empty string", exception.getMessage());
    // }

    // @Test
    // @DisplayName("zack722: Testing that an unsupported formula throws
    // IllegalArgumentException")
    // void testUnsupportedFormulaThrowsException() throws Exception {
    // Places places = new Places();
    // places.add(new Place("40.748817", "-73.985428"));
    // places.add(new Place("34.052235", "-118.243683"));

    // DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
    // setFormula(distancesRequest, "randomFormula");

    // BadRequestException exception = assertThrows(BadRequestException.class,
    // distancesRequest::buildResponse);
    // assertEquals("Unsupported formula: randomFormula", exception.getMessage());
    // }

    @Test
    @DisplayName("zack722: Testing that cosine formula is correctly applied")
    void testCosinesFormulaUsed() throws Exception {
        Places places = new Places();
        places.add(new Place("40.748817", "-73.985428"));
        places.add(new Place("34.052235", "-118.243683"));

        DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
        setFormula(distancesRequest, "cosines");
        distancesRequest.buildResponse();

        assertTrue(distancesRequest.distancesNotNull());
    }

    @Test
    @DisplayName("zack722: Testing that haversine formula is correctly applied")
    void testHaversineFormulaUsed() throws Exception {
        Places places = new Places();
        places.add(new Place("40.748817", "-73.985428"));
        places.add(new Place("34.052235", "-118.243683"));

        DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
        setFormula(distancesRequest, "haversine");
        distancesRequest.buildResponse();

        assertTrue(distancesRequest.distancesNotNull());
    }

    @Test
    @DisplayName("zack722: Testing that vincenty formula is correctly applied when explicitly requested")
    void testVincentyFormulaUsed() throws Exception {
        Places places = new Places();
        places.add(new Place("40.748817", "-73.985428"));
        places.add(new Place("34.052235", "-118.243683"));

        DistancesRequest distancesRequest = new DistancesRequest(places, 6371.0);
        setFormula(distancesRequest, "vincenty");
        distancesRequest.buildResponse();

        assertTrue(distancesRequest.distancesNotNull());
    }

    private void setFormula(DistancesRequest request, String formulaValue) throws Exception {
        Field formulaField = DistancesRequest.class.getDeclaredField("formula");
        formulaField.setAccessible(true);
        formulaField.set(request, formulaValue);
    }
}
