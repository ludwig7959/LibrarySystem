package xyz.ludwicz.librarysystem.data;

import javax.persistence.*;

@Entity
@Table(name = "Member")
public class Member {

    @Id
    @Column(name = "memberId", length = 16, nullable = false)
    private String memberId;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "phone", length = 16, nullable = false, unique = true)
    private String phone;

    @Column(name = "type", length = 10, nullable = false)
    private String type;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}