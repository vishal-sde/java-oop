package SimpleProjects.ExceptionAndIO;


public class Student {
    private String name;
    private int mark1;
    private int mark2;
    private int mark3;

    public Student(String name, int mark1, int mark2, int mark3) {
        this.name = name;
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }

    public String getName() {
        return name;
    }

    public double calcAverage() {
        return (mark1 + mark2 + mark3) / 3.0;
    }

    public String getStatus() {
        return calcAverage() >= 40 ? "Passed" : "Failed";
    }
}
