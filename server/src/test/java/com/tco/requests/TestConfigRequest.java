package com.tco.requests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConfigRequest {

    private ConfigRequest conf;

    @BeforeEach
    public void createConfigurationForTestCases() {
        conf = new ConfigRequest();
        conf.buildResponse();
    }

    @Test
    @DisplayName("base: Request type is \"config\"")
    public void testType() {
        String type = conf.getRequestType();
        assertEquals("config", type);
    }

    @Test
    @DisplayName("base: Features includes \"config\"")
    public void testFeatures() {
        assertTrue(conf.validFeature("config"));
    }

    @Test
    @DisplayName("leoo: Features includes \"distances\"")
    public void testFeaturesDist() {
        assertTrue(conf.validFeature("distances"));
    }

    @Test
    @DisplayName("leoo: Features includes \"tour\"")
    public void testFeaturesTour() {
        assertTrue(conf.validFeature("tour"));
    }

    @Test
    @DisplayName("duyalva: Features do not include \"find\"")
    public void testFeaturesFind() {
        assertTrue(conf.validFeature("find"));
    }

    @Test
    @DisplayName("leoo: Features includes \"near\"")
    public void testFeaturesNear() {
        assertTrue(conf.validFeature("near"));
    }

    @Test
    @DisplayName("duyalva: Features list is expected length")
    public void testFeaturesLength() {
        assertEquals(conf.getFeatures().size(), 5);
    }

    /*
     * @Test
     * 
     * @DisplayName("base: Team name is correct")
     * public void testServerName() {
     * String name = conf.getServerName();
     * assertEquals("Team Name", name);
     * }
     * 
     * @Test
     * 
     * @DisplayName("base: Team number is correct")
     * public void testTeamNumber() {
     * String teamNumber = conf.getTeamNumber();
     * assertEquals("t15", teamNumber);
     * }
     * 
     * @Test
     * 
     * @DisplayName("base: Mission statement is correct")
     * public void testMissionStatement() {
     * String missionStatement = conf.getMissionStatement();
     * assertEquals("Insert your team's mission statement here! Lorem ipsum odor amet, consectetuer adipiscing elit. Sociosqu nisi ut luctus dapibus platea justo justo. Diam ridiculus sem nisi consequat senectus sagittis tempus neque. Sem faucibus netus velit odio ridiculus porta. Sit vulputate sollicitudin penatibus dolor, velit eu molestie. Semper quis velit ridiculus bibendum elit. Vel sollicitudin eu quisque ligula felis eleifend, quis in curae. Metus convallis dis pellentesque posuere et sit suspendisse potenti. Lacinia dignissim duis vel urna dignissim pellentesque litora tempor. Netus vulputate commodo dolor aptent efficitur."
     * ,
     * missionStatement);
     * }
     * 
     * @Test
     * 
     * @DisplayName("base: People list is expected length")
     * public void testPeopleLength(){
     * assertEquals(conf.getPeople().size(), 5);
     * }
     */

}