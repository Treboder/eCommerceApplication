package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestHelperMethods {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }

            declaredField.set(target, toInject);
            if(wasPrivate){
                declaredField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static Long USER_ID = 1L;
    public static String USER_NAME = "user";
    public static String PASSWORD = "password";
    public static String PASSWORD_HASHED = "hash";
    public static String DEMO_ITEM_NAME = "Demo Item";
    public static String DEMO_ITEM_DESC = "Item for Demonstration";
    public static BigDecimal DEMO_ITEM_PRICE = new BigDecimal(1.99);

    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
        user.setPassword(PASSWORD);
        return user;
    }

    public static User createUserWithCartIncludingOneDemoItem() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_NAME);
        user.setPassword(PASSWORD);
        user.setCart(createCartWithOneDemoItemForUser(user));
        return user;
    }

    public static Cart createCartWithOneDemoItemForUser(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = createListWithOneDemoItem();
        cart.setItems(items);
        cart.setTotal(DEMO_ITEM_PRICE);
        cart.setUser(user);
        return cart;
    }

    public static List<Item> createListWithOneDemoItem(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(createDemoItem());
        return itemList;
    }

    public static Item createDemoItem() {
        Item item = new Item();
        item.setId(0L);
        item.setName(DEMO_ITEM_NAME);
        item.setDescription(DEMO_ITEM_DESC);
        item.setPrice(DEMO_ITEM_PRICE);
        return item;
    }

    public static UserOrder createDemoOrder(){
        Cart cart = createCartWithOneDemoItemForUser(createUser());
        UserOrder order = new UserOrder();
        order.setItems(cart.getItems());
        order.setTotal(cart.getTotal());
        order.setUser(createUser());
        order.setId(0L);
        return order;
    }

}
