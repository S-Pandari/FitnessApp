package com.example.fitness;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelUnitTest {

    private Model testModel;

    public ModelUnitTest(){
        testModel = new Model("test_id", "test_name", "test_description", "test_url");
    }

    @Test
    public void modelProperlyCreated() {
        assertEquals("test_name",testModel.getName());
        assertEquals("test_description", testModel.getDesc());
        assertEquals("test_id", testModel.getId());
        assertEquals("test_url", testModel.getUrl());
    }

    @Test
    public void modelProperlyChanged(){
        testModel.setDesc("new_test_description");
        testModel.setName("new_test_name");
        testModel.setId("new_test_id");
        testModel.setUrl("new_test_url");

        assertEquals("new_test_name",testModel.getName());
        assertEquals("new_test_description", testModel.getDesc());
        assertEquals("new_test_id", testModel.getId());
        assertEquals("new_test_url", testModel.getUrl());
    }
}