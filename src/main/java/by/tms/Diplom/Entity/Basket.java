package by.tms.Diplom.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Basket {

    private List<Item> itemList = new ArrayList<>();

    public void addItem(Item item) {
        itemList.add(item);
        log.info("basket, addItem - success");
    }
}