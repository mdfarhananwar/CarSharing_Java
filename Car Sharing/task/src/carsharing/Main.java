
package carsharing;

import carsharing.db.*;

import java.util.*;

public class Main {
    static String DB_URL = "jdbc:h2:./src/carsharing/db/";
    static Scanner scanner = new Scanner(System.in);
    static String[] carAndCompany = new String[0];

    public static void main(String[] args) {
        String dbFileName = "";
        if (args[0].equalsIgnoreCase("-databaseFileName")) {
            dbFileName = args[1];
        }
        DB_URL += dbFileName;
        CompanyDao companyDao = new DbCompanyDao(DB_URL);
        CarDao carDao = new DbCarDao(DB_URL);
        CustomerDao customerDao = new DbCustomerDao(DB_URL);
        printMainMenu();
        selectOptionsFromMainMenu(companyDao, carDao, customerDao);
    }

    public static void printMainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }
    public static void selectOptionsFromMainMenu(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        int choiceNumber = getChoice();
        switch (choiceNumber) {
            case 1:
                printManagerMenu();
                selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
                break;
            case 2:
                //printCustomerMenu();
                getCustomerList(companyDao, carDao, customerDao);
                //selectOptionsFromCustomerMenu(customerDao);
                break;
            case 3:
                addCustomer(customerDao);
                printMainMenu();
                selectOptionsFromMainMenu(companyDao,carDao,customerDao);
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    private static void getCustomerList(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        List<Customer> customers = customerDao.findAll();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            printMainMenu();
            selectOptionsFromMainMenu(companyDao,carDao, customerDao);
            //selectOptionsFromManagerMenu(customerDao, carDao, customerDao);
        } else {
            System.out.println("Customer list:");
            Map<Integer, Customer> customerMap = new HashMap<>();
            for (int i = 0; i < customers.size(); i++) {
                System.out.println(i + 1 + ". " + customers.get(i).getName());
                customerMap.put(i + 1, customers.get(i));
            }
            System.out.println("0. Back");
            selectOptionsFromCustomerList(customerMap,companyDao, carDao, customerDao);
        }
    }

    private static void selectOptionsFromCustomerList(Map<Integer, Customer> customerMap,CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        int chosenNumber = getChoice();
        if (chosenNumber == 0) {
            printMainMenu();
            selectOptionsFromMainMenu(companyDao,carDao, customerDao);
        } else if (customerMap.containsKey(chosenNumber)) {
            Customer customer = customerMap.get(chosenNumber);
            String customerName = customerMap.get(chosenNumber).getName();
            customerMap.get(chosenNumber).getId();
            System.out.println("' " + customerName + "' company");
            printRentMenu();
            selectOptionsFromRentMenu(companyDao, carDao, customerDao, customer);
        }
    }

    private static void selectOptionsFromRentMenu(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {
        int choiceNumber = getChoice();
        switch (choiceNumber) {
            case 1:
                if (carAndCompany.length > 0 && carAndCompany[2].equals(customer.getName())) {
                    System.out.println("You've already rented a car!");
                }
                else {
                    rentCarMenu(companyDao, carDao, customerDao, customer);
                }
                printRentMenu();
                selectOptionsFromRentMenu(companyDao, carDao, customerDao, customer);
                break;
            case 2:
                if (carAndCompany.length == 0) {
                    System.out.println("You didn't rent a car!");
                } else {
                    System.out.println("You've returned a rented car!");
                    //customer.setRentedCarId(null);
                    customerDao.updateNotRentedCarID(customer);
                    carAndCompany = new String[0];
                }
                printRentMenu();
                selectOptionsFromRentMenu(companyDao, carDao, customerDao, customer);
                break;
            case 3:
                if (carAndCompany.length == 0) {
                    System.out.println("You didn't rent a car!");
                } else {
                    System.out.println(carAndCompany.length);
                    rentedCarPrint(carAndCompany[0], carAndCompany[1]);
                }
                printRentMenu();
                selectOptionsFromRentMenu(companyDao, carDao, customerDao, customer);
                break;
            case 0:
                printMainMenu();
                selectOptionsFromMainMenu(companyDao, carDao, customerDao);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    private static void printRentMenu() {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
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

    public static void rentedCarPrint(String carName, String companyName) {
        System.out.println("Your rented car:");
        System.out.println(carName);
        System.out.println("Company:");
        System.out.println(companyName);
    }

    public static void selectOptionsFromManagerMenu(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        int choiceNumber = getChoice();
        switch (choiceNumber) {
            case 1:
                getCompanyName(companyDao, carDao, customerDao);
                break;
            case 2:
                addCompany(companyDao, carDao, customerDao);
                break;
            case 0:
                printMainMenu();
                selectOptionsFromMainMenu(companyDao, carDao, customerDao);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    public static void getCompanyName(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        List<Company> companies = companyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
            printManagerMenu();
            selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
        } else {
            System.out.println("Choose the company:");
            Map<Integer, Company> companyMap = printCompanyMap(companies);
            carMenu(customerDao,carDao, companyMap, companies, companyDao);
        }
    }

    private static Map<Integer, Company>  printCompanyMap(List<Company> companies) {
        Map<Integer, Company> companyMap = new HashMap<>();
        for (int i = 0; i < companies.size(); i++) {
            System.out.println(i + 1 + ". " + companies.get(i).getName());
            companyMap.put(i + 1, companies.get(i));
        }
        System.out.println("0. Back");
        return companyMap;
    }

    public static void addCompany(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        companyDao.add(company);
        System.out.println("The company was created!");
        printManagerMenu();
        selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
    }

    public static void carMenu(CustomerDao customerDao, CarDao carDao, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        int chosenNumber = getChoice();
        int companyId = -1;
        if (chosenNumber == 0) {
            printManagerMenu();
            selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
        } else if (companyMap.containsKey(chosenNumber)) {
            Company companyFromMap = companyMap.get(chosenNumber);
            String companyName = companyMap.get(chosenNumber).getName();
            companyId = companyFromMap.getId();
            System.out.println("'" + companyName + "' company");
            printCarMenu();
        }
        selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao, customerDao);
    }

    public static void printCarMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    public static void selectFromCarMenu(CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao, CustomerDao customerDao) {
        int selectNumber = getChoice();
        switch (selectNumber) {
            case 1:
                getCarNameByCompanyId(carDao, companyId, companyMap, companies, companyDao, customerDao);
                break;
            case 2:
                addCar(customerDao,carDao, companyId, companyMap, companies, companyDao);
                break;
            case 0:
                printManagerMenu();
                selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
                break;
            default:
                System.out.println("Select correct options");
        }
    }

    public static void getCarNameByCompanyId(CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao, CustomerDao customerDao) {
        List<Car> cars = carDao.findAllByCompanyId(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            printCarMenu();
            selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao, customerDao);
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
            System.out.println("0. Back");
            carMenu(customerDao, carDao, companyMap, companies, companyDao);
        }
    }

    public static void addCar(CustomerDao customerDao,CarDao carDao, int companyId, Map<Integer, Company> companyMap, List<Company> companies, CompanyDao companyDao) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        Company company = companyDao.findById(companyId);
        if (company == null) {
            System.out.println("Company not found with ID: " + companyId);
            carMenu(customerDao, carDao, companyMap, companies, companyDao);
            return;
        }
        Car car = new Car(name, companyId);
        carDao.add(name, companyId);
        company.addCar(car);
        System.out.println("The car was added!");
        printCarMenu();
        selectFromCarMenu(carDao, companyId, companyMap, companies, companyDao, customerDao);
    }
    public static void addCustomer(CustomerDao customerDao) {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        Customer customer = new Customer(name);
        customerDao.add(customer);
        System.out.println("The customer was added!");
        printMainMenu();
    }
    public static void rentCarMenu(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Customer customer) {
        List<Company> companies = companyDao.findAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
            printManagerMenu();
            selectOptionsFromManagerMenu(companyDao, carDao, customerDao);
        } else {
            System.out.println("Choose a company:");
            Map<Integer, Company> companyMap = printCompanyMap(companies);
            printManagerMenu();
            selectFromCompanyList(companyDao, carDao, customerDao, companyMap, customer);
        }
    }
    public static void selectFromCompanyList(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao, Map<Integer, Company> companyMap, Customer customer) {
        int chosenNumber = getChoice();
        if (chosenNumber == 0) {
            printRentMenu();
            selectOptionsFromRentMenu(companyDao, carDao, customerDao, customer);
        } else if (companyMap.containsKey(chosenNumber)) {
            Company companyFromMap = companyMap.get(chosenNumber);
            getCarRentList(carDao, companyFromMap,customerDao, customer);
        }
    }
    public static void getCarRentList(CarDao carDao, Company company,CustomerDao customerDao, Customer customer) {
        String companyName = company.getName();
        int companyId = company.getId();
        List<Car> cars = carDao.findAllByCompanyId(companyId);
        if (carAndCompany.length > 0){
            cars.removeIf(car ->
                    car.getName().equals(carAndCompany[0])
            );
        }
        Map<Integer, Car> carMap = new HashMap<>();
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Choose a car:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
                System.out.println(cars);
                carMap.put(i + 1, cars.get(i));
            }
            System.out.println("0. Back");
            selectFromCarRentList(carMap, companyName, customerDao, customer);
        }
    }

    private static void selectFromCarRentList(Map<Integer, Car> carMap, String companyName, CustomerDao customerDao, Customer customer) {
        String customerName = customer.getName();

        int chosenNumber = getChoice();
        System.out.println(carMap);
        System.out.println(Arrays.toString(carAndCompany));

        if (chosenNumber == 0) {

        } else if (carMap.containsKey(chosenNumber)) {
            Car carFromMap = carMap.get(chosenNumber);
            System.out.println(carFromMap);
            String carName = carMap.get(chosenNumber).getName();
            int carId = carFromMap.getId();
            System.out.println("You rented '" + carName + "'");
            carAndCompany = new String[3];
            customer.setRentedCarId(carId);
            customerDao.update(customer, carId);
            carAndCompany[0] = carName;
            carAndCompany[1] = companyName;
            carAndCompany[2] = customerName;
            System.out.println(customer);
        }
    }
}
