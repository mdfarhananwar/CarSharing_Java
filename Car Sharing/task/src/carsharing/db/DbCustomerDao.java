package carsharing.db;

import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

public class DbCustomerDao implements CustomerDao{
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
            "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
            "NAME VARCHAR(40) UNIQUE NOT NULL, " +
            "RENTED_CAR_ID INTEGER, " +
            "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String SELECT = "SELECT * FROM CUSTOMER WHERE ID = ?";
    private static final String INSERT_DATA = "INSERT INTO CUSTOMER (NAME) VALUES (?)";

    private static final String UPDATE_DATA = "UPDATE CUSTOMER SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?";
    private static final String UPDATE_DATA_NOT_RENTED_CAR_ID = "UPDATE CUSTOMER SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?";

    private static final String DELETE_DATA = "DELETE FROM CUSTOMER WHERE ID = %d";
    private static final String SELECT_NAME = "SELECT NAME FROM CUSTOMER WHERE NAME = ?";
    private final DbClient dbClient;

    public DbCustomerDao(String url) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(url);
        dbClient = new DbClient(dataSource);
        dbClient.run(CREATE_DB);
    }
    @Override
    public List<Customer> findAll() {
        return dbClient.selectForListCustomer(SELECT_ALL);
    }

    @Override
    public Customer findById(int id) {
        Customer customer = dbClient.selectCustomer(SELECT, id);

        if (customer != null) {
            System.out.println("Customer: Id " + id + ", found");
            return customer;
        } else {
            System.out.println("Customer: Id " + id + ", not found");
            return null;
        }
    }

    @Override
    public void add(Customer customer) {
        dbClient.addCustomer(INSERT_DATA, customer);
    }

    @Override
    public void update(Customer customer, Integer carId) {
        dbClient.updateRentedCarId(
                UPDATE_DATA, customer, carId);
        System.out.println("customer: Id " + customer.getId() + ", updated");
    }
    public void updateNotRentedCarID(Customer customer) {
        dbClient.updateNotRentedCarId(
                UPDATE_DATA_NOT_RENTED_CAR_ID, customer);
        System.out.println("customer: Id " + customer.getId() + ", updated");
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE_DATA, id));
        System.out.println("Customer: Id " + id + ", deleted");
    }

    @Override
    public Customer findByName(String customerName) {
        Customer customer = dbClient.selectCustomerName(SELECT_NAME, customerName);
        if (customer != null) {
            System.out.println("Company: Name " + customerName + ", found");
            System.out.println(customer);
            return customer;
        } else {
            System.out.println("Company: Name " + customerName + ", not found");
            return null;
        }
    }
}
