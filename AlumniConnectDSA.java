import java.util.*;

class Alumni {

    int id;
    String name;
    int graduationYear;
    String organization;
    String jobRole;
    String skills;
    String interests;
    int experience;

    Alumni(int id, String name, int graduationYear,
           String organization, String jobRole,
           String skills, String interests, int experience) {

        this.id = id;
        this.name = name;
        this.graduationYear = graduationYear;
        this.organization = organization;
        this.jobRole = jobRole;
        this.skills = skills;
        this.interests = interests;
        this.experience = experience;
    }

    void display() {

        System.out.println("--------------------------------------------");
        System.out.println("Alumni ID       : " + id);
        System.out.println("Name            : " + name);
        System.out.println("Graduation Year : " + graduationYear);
        System.out.println("Organization    : " + organization);
        System.out.println("Job Role        : " + jobRole);
        System.out.println("Skills          : " + skills);
        System.out.println("Interests       : " + interests);
        System.out.println("Experience      : " + experience + " years");
        System.out.println("--------------------------------------------");
    }
}

public class AlumniConnectDSA {

    // =====================================================
    // Z-FUNCTION
    // =====================================================

    static int[] calculateZ(String s) {

        int n = s.length();

        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] = Math.min(right - i + 1,
                                z[i - left]);
            }

            while (i + z[i] < n &&
                   s.charAt(z[i]) ==
                   s.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > right) {

                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }

    // =====================================================
    // Z-FUNCTION SEARCH
    // =====================================================

    static boolean zSearch(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.length() == 0) {
            return true;
        }

        String combined = pattern + "$" + text;

        int[] z = calculateZ(combined);

        for (int i = 0; i < z.length; i++) {

            if (z[i] == pattern.length()) {
                return true;
            }
        }

        return false;
    }

    // =====================================================
    // EDIT DISTANCE
    // =====================================================

    static int editDistance(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        // Deleting characters from a
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Inserting characters into a
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (a.charAt(i - 1) ==
                    b.charAt(j - 1)) {

                    dp[i][j] =
                        dp[i - 1][j - 1];

                } else {

                    int insert =
                        dp[i][j - 1];

                    int delete =
                        dp[i - 1][j];

                    int replace =
                        dp[i - 1][j - 1];

                    dp[i][j] =
                        1 + Math.min(
                            insert,
                            Math.min(delete, replace)
                        );
                }
            }
        }

        return dp[m][n];
    }

    // =====================================================
    // SIMILARITY
    // =====================================================

    static double similarity(String a, String b) {

        int distance = editDistance(a, b);

        int maxLength =
            Math.max(a.length(), b.length());

        if (maxLength == 0) {
            return 100.0;
        }

        return (1.0 -
                (double) distance / maxLength) * 100;
    }

    // =====================================================
    // DISPLAY ALL ALUMNI
    // =====================================================

    static void displayAll(ArrayList<Alumni> list) {

        System.out.println("\n========== ALL ALUMNI ==========");

        for (Alumni a : list) {
            a.display();
        }
    }

    // =====================================================
    // EXACT SEARCH USING Z-FUNCTION
    // =====================================================

    static void searchAlumni(ArrayList<Alumni> list,
                             String query) {

        boolean found = false;

        System.out.println(
            "\n========== Z-FUNCTION SEARCH ==========");

        for (Alumni a : list) {

            boolean skillMatch =
                zSearch(a.skills, query);

            boolean interestMatch =
                zSearch(a.interests, query);

            boolean organizationMatch =
                zSearch(a.organization, query);

            boolean nameMatch =
                zSearch(a.name, query);

            if (skillMatch ||
                interestMatch ||
                organizationMatch ||
                nameMatch) {

                a.display();

                System.out.println(
                    "Match Method: Z-Function");

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                "No exact matches found.");
        }
    }

    // =====================================================
    // FUZZY SEARCH USING EDIT DISTANCE
    // =====================================================

    static void fuzzySearch(ArrayList<Alumni> list,
                            String query) {

        boolean found = false;

        System.out.println(
            "\n========== EDIT DISTANCE SEARCH ==========");

        for (Alumni a : list) {

            String[] skills =
                a.skills.split(";");

            for (String skill : skills) {

                skill = skill.trim();

                int distance =
                    editDistance(query, skill);

                double sim =
                    similarity(query, skill);

                // Show reasonably similar results
                if (distance <= 2 ||
                    sim >= 60) {

                    a.display();

                    System.out.println(
                        "Compared Skill : " + skill);

                    System.out.println(
                        "Edit Distance  : " + distance);

                    System.out.printf(
                        "Similarity     : %.2f%%\n",
                        sim);

                    found = true;

                    break;
                }
            }
        }

        if (!found) {

            System.out.println(
                "No similar alumni found.");
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Alumni> alumniList =
            new ArrayList<>();

        // =================================================
        // ALUMNI DATASET
        // =================================================

        alumniList.add(
            new Alumni(
                1,
                "Rahul Sharma",
                2020,
                "TCS",
                "Software Engineer",
                "Java; SQL; Spring Boot",
                "Web Development; Mentoring",
                5
            )
        );

        alumniList.add(
            new Alumni(
                2,
                "Priya Reddy",
                2019,
                "Infosys",
                "Data Analyst",
                "Python; SQL; Excel",
                "Data Science; Analytics",
                6
            )
        );

        alumniList.add(
            new Alumni(
                3,
                "Arjun Kumar",
                2021,
                "Microsoft",
                "Software Developer",
                "Java; Python; React",
                "AI; Web Development",
                4
            )
        );

        alumniList.add(
            new Alumni(
                4,
                "Sneha Patel",
                2018,
                "Amazon",
                "Cloud Engineer",
                "AWS; Java; Linux",
                "Cloud Computing; DevOps",
                7
            )
        );

        alumniList.add(
            new Alumni(
                5,
                "Vikram Singh",
                2022,
                "Wipro",
                "Software Engineer",
                "C++; Java; DSA",
                "Programming; Mentoring",
                3
            )
        );

        alumniList.add(
            new Alumni(
                6,
                "Ananya Rao",
                2020,
                "Google",
                "Data Scientist",
                "Python; Machine Learning; SQL",
                "AI; Data Science",
                5
            )
        );

        alumniList.add(
            new Alumni(
                7,
                "Kiran Kumar",
                2019,
                "Accenture",
                "Full Stack Developer",
                "JavaScript; React; Node.js",
                "Web Development",
                6
            )
        );

        alumniList.add(
            new Alumni(
                8,
                "Meena Reddy",
                2017,
                "Deloitte",
                "Business Analyst",
                "Excel; SQL; Power BI",
                "Business Analytics; Career Guidance",
                8
            )
        );

        alumniList.add(
            new Alumni(
                9,
                "Rohit Verma",
                2021,
                "IBM",
                "Cloud Developer",
                "Python; AWS; Docker",
                "Cloud Computing; DevOps",
                4
            )
        );

        alumniList.add(
            new Alumni(
                10,
                "Divya Sharma",
                2018,
                "Tech Mahindra",
                "HR Manager",
                "Recruitment; Communication; Management",
                "HR; Mentoring",
                7
            )
        );

        // =================================================
        // MENU
        // =================================================

        while (true) {

            System.out.println("\n");
            System.out.println("========================================");
            System.out.println("          ALUMNI CONNECT");
            System.out.println("========================================");
            System.out.println("1. Display All Alumni");
            System.out.println("2. Search using Z-Function");
            System.out.println("3. Search using Edit Distance");
            System.out.println("4. Exit");
            System.out.println("========================================");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            sc.nextLine();

            switch (choice) {

                case 1:

                    displayAll(alumniList);

                    break;

                case 2:

                    System.out.print(
                        "Enter skill, interest, name or organization: ");

                    String query1 =
                        sc.nextLine();

                    searchAlumni(
                        alumniList,
                        query1
                    );

                    break;

                case 3:

                    System.out.print(
                        "Enter skill to search: ");

                    String query2 =
                        sc.nextLine();

                    fuzzySearch(
                        alumniList,
                        query2
                    );

                    break;

                case 4:

                    System.out.println(
                        "\nThank you for using Alumni Connect!");

                    sc.close();

                    return;

                default:

                    System.out.println(
                        "Invalid choice. Try again.");
            }
        }
    }
}