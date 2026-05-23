package SimpleProjects.ExceptionAndIO;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StudentRecordSystem {
    public static void validateMark(int mark) throws InvalidMarkException {
        if (mark < 0 || mark > 100) {
            throw new InvalidMarkException("Invalid mark: " + mark);
        }
    }

    public static void main(String[] args) {
        HashMap<String, Student> studentMap = new HashMap<>();

        // Read phase
        try (BufferedReader reader = new BufferedReader(new FileReader("src/SimpleProjects/ExceptionAndIO/studentsList.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");

                    if (data.length != 4) {
                        System.out.println("Invalid line format: " + line);
                        continue;
                    }

                    String name = data[0].trim();
                    int mark1 = Integer.parseInt(data[1].trim());
                    int mark2 = Integer.parseInt(data[2].trim());
                    int mark3 = Integer.parseInt(data[3].trim());

                    validateMark(mark1);
                    validateMark(mark2);
                    validateMark(mark3);

                    Student student = new Student(name, mark1, mark2, mark3);
                    studentMap.put(name, student);
                    System.out.println("Loaded: " + name);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in line: " + line);
                } catch (InvalidMarkException e) {
                    System.out.println("Validation error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Write phase
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/SimpleProjects/ExceptionAndIO/student_output.csv"))) {
            for (Map.Entry<String, Student> entry : studentMap.entrySet()) {
                Student student = entry.getValue();
                writer.write(
                        student.getName() + "," +
                                String.format("%.2f", student.calcAverage()) + "," +
                                student.getStatus()
                );
                writer.newLine();
            }
            System.out.println("Successfully written to student_output.csv");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
