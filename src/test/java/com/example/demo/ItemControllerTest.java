package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void testGetItemList() {
        // create a list with one demo item and make the repo return the demo item list
        List<Item> items = TestHelperMethods.createListWithOneDemoItem();
        Item demoItem = items.get(0);
        when(itemRepository.findAll()).thenReturn(items);
        // trigger controller function and check for ok-request
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // extract actual item list from response and compare to initial list
        List<Item> actualItems = response.getBody();
        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
        assertEquals(demoItem, actualItems.get(0));
    }

    @Test
    public void testGetItemById() {
        // create the demo item and make the repo return the demo item
        Item demoItem = TestHelperMethods.createDemoItem();
        when(itemRepository.findById(demoItem.getId())).thenReturn(java.util.Optional.of(demoItem));
        // trigger controller function and check for ok-request
        ResponseEntity<Item> response = itemController.getItemById(demoItem.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // extract actual demo item from response and compare to initial demo item
        Item actualItem = response.getBody();
        assertEquals(demoItem, actualItem);
        assertNotNull(actualItem);
        assertEquals(demoItem.getName(), actualItem.getName());
        assertEquals(demoItem.getId(), actualItem.getId());
        assertEquals(demoItem.getDescription(), actualItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {
        // create a list with one demo item and make the repo return the demo item list
        List<Item> itemList = TestHelperMethods.createListWithOneDemoItem();
        Item demoItem = itemList.get(0);
        when(itemRepository.findByName(demoItem.getName())).thenReturn(itemList);
        // trigger controller function and check for ok-request
        ResponseEntity<List<Item>> response = itemController.getItemsByName(demoItem.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        // extract actual demo item from response and compare to initial demo item
        Item actualItem = response.getBody().get(0);
        assertEquals(demoItem, actualItem);
        assertNotNull(actualItem);
        assertEquals(demoItem.getName(), actualItem.getName());
        assertEquals(demoItem.getId(), actualItem.getId());
        assertEquals(demoItem.getDescription(), actualItem.getDescription());
    }

}
