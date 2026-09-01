package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classroom_gen")
    @SequenceGenerator(name = "classroom_gen", sequenceName = "classroom_id_seq", allocationSize = 30)
    @Column(name = "classroom_id")
    private Integer id;

    @Column(name = "classroom_name", unique = true)
    private String name;
    
    @OneToMany(mappedBy = "classroom")
    private List<Lesson> lessons = new ArrayList<>();

    public Classroom() {
    }

    public Classroom(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
    
    

}
