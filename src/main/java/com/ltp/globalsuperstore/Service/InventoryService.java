package com.ltp.globalsuperstore.Service;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.Item;
import com.ltp.globalsuperstore.Repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public Item getItem(int index) {
        return inventoryRepository.getItem(index);
    }

    public List<Item> getItems() {
        return inventoryRepository.getItems();
    }

    public void addItem(Item item) {
        inventoryRepository.addItem(item);
    }

    public void updateItem(Item item, int index) {
        inventoryRepository.updateItem(item, index);
    }

    public Item getItemById(String id) {
        int index = getIndexOfExistingItem(id);
        return index == Constants.NOT_FOUND ? new Item() : getItem(index);
    }

    public int getIndexOfExistingItem(String id) {
        for (int i = 0; i < getItems().size(); i++) {
            if (getItems().get(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }

    public String submitItem(Item item) {
        int index = getIndexOfExistingItem(item.getId());
        String status = Constants.SUCCESS_STATUS;

        if (index == Constants.NOT_FOUND) {
            addItem(item);
        } else if (within5Days(item.getDate(), getItems().get(index).getDate())) {
            updateItem(item, index);
        } else {
            status = Constants.FAILED_STATUS;
        }

        return status;
    }

}
