package com.stuffer.stuffers.communicator;

import com.stuffer.stuffers.models.all_restaurant.RestaurantItems;

public interface RestaurantListener {
    public void onIncreaseItemRequest(int pos, RestaurantItems.Result obj, Integer id);

    public void onDeCreaseRequest(int pos,Integer id);
}
