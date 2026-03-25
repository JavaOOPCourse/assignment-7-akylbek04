import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentRecordProcessor {

    private final List<Student> students = new ArrayList<>();
    private double averageScore;
    private Student highestStudent;

    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     * Reads student data from data/students.txt using try-with-resources.
     * Handles FileNotFoundException, IOException, NumberFormatException,
     * and InvalidScoreException.
     */
    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 2) {
                        System.out.println("Invalid data: " + line);
                        continue;
                    }
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());

                    if (score < 0 || score > 100) {
                        throw new InvalidScoreException("Score out of range (0-100): " + score);
                    }

                    students.add(new Student(name, score));
                    System.out.println(name + "," + score);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid data: " + line);
                } catch (InvalidScoreException e) {
                    System.out.println("Invalid data: " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     * Computes average score, finds highest score, and sorts students
     * by score in descending order.
     */
    public void processData() {
        if (students.isEmpty()) {
            System.out.println("No valid student records to process.");
            return;
        }

        int sum = 0;
        highestStudent = students.get(0);

        for (Student s : students) {
            sum += s.getScore();
            if (s.getScore() > highestStudent.getScore()) {
                highestStudent = s;
            }
        }

        averageScore = (double) sum / students.size();

        // Task 8: Sort students by score descending
        students.sort(Comparator.comparingInt(Student::getScore).reversed());
    }

    /**
     * Task 4 + Task 5 + Task 8
     * Writes the report to output/report.txt using try-with-resources.
     * Includes average, highest scorer, and the full sorted list.
     */
    public void writeFile() {
        if (students.isEmpty()) {
            System.out.println("No data to write.");
            return;
        }

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output/report.txt"))) {
            bw.write("Average: " + averageScore);
            bw.newLine();
            bw.write("Highest: " + highestStudent.getName() + " - " + highestStudent.getScore());
            bw.newLine();
            bw.newLine();
            bw.write("All Students (sorted by score, descending):");
            bw.newLine();
            for (Student s : students) {
                bw.write(s.getName() + " - " + s.getScore());
                bw.newLine();
            }
            System.out.println("Report written to output/report.txt");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

// Task 6: Custom Exception
class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }
}

// Student class
class Student {
    private final String name;
    private final int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return name + "," + score;
    }
}