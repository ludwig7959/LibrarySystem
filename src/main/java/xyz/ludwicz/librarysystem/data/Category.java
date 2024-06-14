package xyz.ludwicz.librarysystem.data;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "Category")
public class Category {

    public static Category ALL = new Category(0, "전체");
    public static Category ETC = new Category(-1, "기타");


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Integer categoryId;

    @Column(name = "name", nullable = false, unique = true, length = 16)
    private String name;

    @Formula("(SELECT COUNT(*) FROM Book b JOIN BookInfo bi ON b.bookInfoId = bi.bookInfoId WHERE bi.categoryId = categoryId)")
    private Long bookCount;

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBookCount() {
        return bookCount;
    }

    private Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Category() {

    }

    @Override
    public String toString() {
        return name;
    }
}