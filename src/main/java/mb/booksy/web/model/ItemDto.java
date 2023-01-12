package mb.booksy.web.model;

import java.util.Objects;

public class ItemDto {

    private Long id;
    private String itemName;
    private String itemImage;
    private Integer cartAmount;
    private Double cartPrice;
    private String currency;
    private String producerName;
    private String bookType;
    private String genre;
    private Double price;
    private Integer availability;
    private String itemDescription;
    private Integer numberInOrderReturn;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Integer getNumberInOrderReturn() {
        return numberInOrderReturn;
    }

    public void setNumberInOrderReturn(Integer numberInOrderReturn) {
        this.numberInOrderReturn = numberInOrderReturn;
    }
}
