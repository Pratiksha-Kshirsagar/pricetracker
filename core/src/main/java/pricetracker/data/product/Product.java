package pricetracker.data.product;

import pricetracker.data.manufacturer.Manufacturer;
import pricetracker.data.productproperties.ProductProperties;
import pricetracker.data.producttype.ProductType;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Year;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String model;

    @Convert(converter = YearFieldConverter.class)
    private Year year;

    /**
     * Describes a url to the image for current product.
     */
    private String image;
    @Version
    private Integer version;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id", nullable = false)
    private ProductProperties productProperties;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductProperties getProductProperties() {
        return productProperties;
    }

    public void setProductProperties(ProductProperties productProperties) {
        this.productProperties = productProperties;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", image='" + image + '\'' +
                ", version='" + version + '\'' +
                ", productProperties=" + productProperties +
                ", productType=" + productType +
                ", manufacturer=" + manufacturer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(model, product.model) &&
                Objects.equals(year, product.year) &&
                Objects.equals(image, product.image) &&
                Objects.equals(version, product.version) &&
                Objects.equals(productProperties, product.productProperties) &&
                Objects.equals(productType, product.productType) &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, model, year, image, version, productProperties, productType, manufacturer);
    }
}
