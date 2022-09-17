package homework;

@Table(title = "students")
public class Students {
    @Column
    int id;
    @Column
    String name;
    @Column
    int course;

    public Students(int id, String name, int course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCourse() {
        return course;
    }
}
