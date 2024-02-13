
package carsharing;

import carsharing.db.*;

import java.util.*;

public class Main {
    static String DB_URL = "jdbc:h2:./src/carsharing/db/";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String dbFileName = "";
        if (args[0].equalsIgnoreCase("-databaseFileName")) {
            dbFileName = args[1];
        }
        DB_URL += dbFileName;

        CompanyDao companyDao = new DbCompanyDao(DB_URL);
        CarDao carDao = new DbCarDao(DB_URL);

        printMainMenu();
        selectOptionsFromMainMenu(companyDao, carDao);
    }

    public static void printMainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("0. Exit");
    }

    public static void selectOptionsFromMainMenu(CompanyDao companyDao, CarDao carDao) {
        int choiceNumber = getChoice();
        switch (choiceNumber) {
            case 1:
                printManagerMenu();
                selectOptionsFromManagerMenu(companyDao, carDao);
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    public static int getChoice() {
        int choiceNumber = -1;
        try {
            choiceNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Select correct options");
        }
        return choiceNumber;
    }

    public static void printManagerMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    public static void selectOptionsFromManagerMenu(CompanyDao companyDao, CarDao carDao) {
        int choiceNumber = getChoice();
        switch (choiceNumber) {
            case 1:
                getCompanyName(companyDao, carDao);
                break;
            case 2:
                addCompany(companyDao, carDao);
                break;
            case 0:
                printMainMenu();
                selectOptionsFromMainMenu(companyDao, carDao);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    public static void getCompanyName(CompanyDao companyDao, CarDao carDao) {
        List<Company> companies = companyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
            selectOptionsFromManagerMenu(companyDao, carDao);
        } else {
            System.out.println("Choose the company:");
            Map<Integer, Company> companyMap = new HashMap<>();
            for (int i = 0; i < companies.size(); i++) {
                System.out.println(i + 1 + ". " + companies.get(i).getName());
                companyMap.put(i + 1, companies.get(i));
            }
            System.out.println("0. Back");
            carMenu(carDao, companyMap, companies, companyDao);
        }
    }

    public static void addCompany(CompanyDao companyDao, CarDao carDao) {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.add(company);
        System.out.println("The company was created!");
        printManagerMenu();
        selectOptionsFromManagerMenu(companyDao, carDao);
    }

    public static void carMenu(CarDao carDao, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        int chosenNumber = getChoice();
        String companyName = "";
        int companyId = -1;
        if (chosenNumber == 0) {
            selectOptionsFromManagerMenu(companyDao, carDao);
        } else if (companyMap.containsKey(chosenNumber)) {
            Company companyFromMap = companyMap.get(chosenNumber);
            companyName = companyMap.get(chosenNumber).getName();
            companyId = companyFromMap.getId();
            System.out.println("' " + companyName + "' company");
            printCarMenu();
        }
        selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao);
    }

    public static void printCarMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    public static void selectFromCarMenu(CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        int selectNumber = getChoice();
        switch (selectNumber) {
            case 1:
                getCarNameByCompanyId(carDao, companyId, companyMap, companies, companyDao);
                break;
            case 2:
                addCar(carDao, companyId, companyMap, companies, companyDao);
                break;
            case 0:
                printManagerMenu();
                selectOptionsFromManagerMenu(companyDao, carDao);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    public static void getCarNameByCompanyId(CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        List<Car> cars = carDao.findAllByCompanyId(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            printCarMenu();
            selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao);
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
            carMenu(carDao, companyMap, companies, companyDao);
        }
    }

    public static void addCar(CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        Company company = companyDao.findById(companyId);
        if (company == null) {
            System.out.println("Company not found with ID: " + companyId);
            carMenu(carDao, companyMap, companies, companyDao);
            return;
        }
        System.out.println(company.getName());
        Car car = new Car(name, companyId);
        carDao.add(name, companyId);
        company.addCar(car);
        System.out.println("The car was added!");
        printCarMenu();
        selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao);
    }
}
