package mb.booksy.web.model;

import java.util.Objects;

public class ItemDto {

    private Long id;
    private String itemName;
    private String itemImage;
    private Integer cartAmount;
    private Double cartPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return id.equals(itemDto.id) && itemName.equals(itemDto.itemName) && itemImage.equals(itemDto.itemImage) && cartAmount == itemDto.cartAmount && cartPrice == itemDto.cartPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName, itemImage, cartAmount, cartPrice);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setImage(String image) {
        this.itemImage = image;
    }

    public void setCartAmount(Integer cartAmount) {
        this.cartAmount = cartAmount;
    }

    public void setCartPrice(Double cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public Integer getCartAmount() {
        return cartAmount;
    }

    public Double getCartPrice() {
        return cartPrice;
    }
}
