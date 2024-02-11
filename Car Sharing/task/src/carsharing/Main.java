package carsharing;

import carsharing.db.Company;
import carsharing.db.CompanyDao;
import carsharing.db.DbCompanyDao;

import java.util.List;
import java.util.Scanner;

public class Main {
    static String DB_URL = "jdbc:h2:./src/carsharing/db/";
    public static void main(String[] args) {
        // write your code here
        String dbFileName = "";
        if (args[0].equalsIgnoreCase("-databaseFileName")) {
            dbFileName = args[1];
        }
        DB_URL += dbFileName;
        CompanyDao companyDao = new DbCompanyDao(DB_URL);
        List<Company> companyList = companyDao.findAll();
        Scanner scanner = new Scanner(System.in);
        printMainMenu(scanner, companyList, companyDao);

//        Connection conn = null;
//        Statement stmt = null;
//
//
//        try {
//            // STEP 1: Register JDBC driver
//            Class.forName(JDBC_DRIVER);
//
//            //STEP 2: Open a connection
//            System.out.println("Connecting to database...");
//            conn = DriverManager.getConnection(DB_URL);
//            //STEP 3: Execute a query
//            System.out.println("Creating table in given database...");
//            stmt = conn.createStatement();
//            String sql =  "CREATE TABLE COMPANY " +
//                    "(ID INTEGER not NULL, " +
//                    " NAME VARCHAR(255), " +
//
//                    " PRIMARY KEY ( ID ))";
//            stmt.executeUpdate(sql);
//            System.out.println("Created table in given database...");
//            // STEP 4: Clean-up environment
//            stmt.close();
//            conn.close();
//        } catch(SQLException se) {
//            se.printStackTrace();
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            //finally block used to close resources
//            try{
//                conn.setAutoCommit(true);
//                if(stmt!=null) stmt.close();
//            } catch(SQLException se2) {
//            } // nothing we can do
//            try {
//                if(conn!=null) conn.close();
//            } catch(SQLException se){
//                se.printStackTrace();
//            } //end finally try
//        } //end try
//        System.out.println("Goodbye!");
//    }
    }
    public static void printMainMenu(Scanner scanner, List<Company> companies,CompanyDao companyDao) {
        System.out.println("1. Log in as a manager");
        System.out.println("0. Exit");
        selectOptionsFromMainMenu(scanner, companies, companyDao);
    }

    public static void printManagerMenu(Scanner scanner, List<Company> companies,CompanyDao companyDao) {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        selectOptionsFromManagerMenu(scanner, companies, companyDao);
    }

    public static void getCompanyName(Scanner scanner, List<Company> companies, CompanyDao companyDao) {
        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
            selectOptionsFromManagerMenu(scanner, companies, companyDao);
        } else {
            for (int i = 0; i < companies.size(); i++) {
                System.out.println(i + 1 + ". " + companies.get(i).getName());
            }
            selectOptionsFromManagerMenu(scanner, companies, companyDao);
        }


    }
    public static void addCompany(Scanner scanner, CompanyDao companyDao) {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.add(company);
        List<Company> companies = companyDao.findAll();
        System.out.println("The company was created!");
        printManagerMenu(scanner, companies, companyDao);
    }
    public static void selectOptionsFromMainMenu(Scanner scanner, List<Company> companies,CompanyDao companyDao) {
        int choiceNumber = Integer.parseInt(scanner.nextLine());
        if (choiceNumber == 1) {
            printManagerMenu(scanner, companies, companyDao);
        }
    }
    public static void selectOptionsFromManagerMenu(Scanner scanner, List<Company> companies,CompanyDao companyDao) {
        int choiceNumber = Integer.parseInt(scanner.nextLine());
        switch (choiceNumber) {
            case 1 : getCompanyName(scanner, companies, companyDao);
            break;
            case 2: addCompany(scanner, companyDao);
            break;
            case 0: printMainMenu(scanner, companies, companyDao);
            break;
            default:
                System.out.println("Select correct options");
        }
    }

}