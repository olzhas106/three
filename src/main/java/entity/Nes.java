package entity;


import javax.persistence.*;

@Entity
@Table(name = "nested_sets")
public class Nes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "\"left\"")
    private Integer left;
    @Column(name = "\"right\"")
    private Integer right;
    private Integer level;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getLeft() {
        return left;
    }

    public Integer getRight() {
        return right;
    }

    public Integer getLevel() {
        return level;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}

