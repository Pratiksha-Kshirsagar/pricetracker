package pricetracker.dto.store;

import java.io.Serializable;
import java.util.Objects;

public class StoreDto implements Serializable {
    private Long id;

    private String name;

    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "StoreDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDto storeDto = (StoreDto) o;
        return Objects.equals(id, storeDto.id) &&
                Objects.equals(name, storeDto.name) &&
                Objects.equals(url, storeDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url);
    }
}
