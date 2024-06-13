package xyz.ludwicz.librarysystem.data;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "Publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisherId")
    private Integer publisherId;

    @Column(name = "name", nullable = false, unique = true, length = 32)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Formula("(SELECT COUNT(*) FROM Book b JOIN BookInfo bi ON b.bookInfoId = bi.bookInfoId WHERE bi.publisherId = publisherId)")
    private Long bookCount;

    public Integer getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBookCount() {
        return bookCount;
    }
}