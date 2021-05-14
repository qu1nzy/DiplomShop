package by.tms.Diplom.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Objects;


@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "items")
    public class Item {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "item_id")
        private long id;

        @OneToOne(cascade = CascadeType.ALL)
        private Category category;

        private String manufacturer;
        private String description;
        private String model;
        private String serialNumber;
        private String price;
        private String countryManufacture;
        @ManyToOne
        private Order order;
        private ItemStatus itemStatus;
        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private Image image;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(category, item.category) && Objects.equals(model, item.model);
        }

        @Override
        public int hashCode() {
            return Objects.hash(category, model);
        }
    }
