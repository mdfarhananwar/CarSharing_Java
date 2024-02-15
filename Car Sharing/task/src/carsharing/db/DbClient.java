package carsharing.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    private final DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void run(String str) {
        try (Connection con = dataSource.getConnection(); // Statement creation
             Statement statement = con.createStatement()
        ) {
            statement.executeUpdate(str); // Statement execution
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Company selectCompany(String query, int id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            // Set parameter
            statement.setInt(1, id);

            try (ResultSet resultSetItem = statement.executeQuery()) {
                if (resultSetItem.next()) {
                    // Retrieve column values
                    resultSetItem.getInt("ID");
                    String name = resultSetItem.getString("NAME");
                    return new Company(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Company> selectForListCompany(String query) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int companyId = resultSetItem.getInt("ID"); // Retrieve company ID
                String name = resultSetItem.getString("NAME");
                Company company = new Company(name, companyId); // Pass the companyId to the Company constructor
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public List<Car> selectForListCar(String query, int companyId) {
        List<Car> cars = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            // Set parameter
            statement.setInt(1, companyId);

            try (ResultSet resultSetItem = statement.executeQuery()) {
                while (resultSetItem.next()) {
                    // Retrieve column values
                    int id = resultSetItem.getInt("ID");
                    String name = resultSetItem.getString("NAME");
                    Car car = new Car(id,name, companyId);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public void addCar(String insertData, String name, int companyId) {
        if (!isCompanyIdValid(companyId)) {
            System.out.println("Invalid COMPANY_ID. Aborting car insertion.");
            return;
        }
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(insertData)) {
            // Set parameters
            statement.setString(1, name);
            statement.setInt(2, companyId);

            // Execute the update
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean isCompanyIdValid(int companyId) {
        String query = "SELECT ID FROM COMPANY WHERE ID = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if companyId exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an exception occurs or companyId does not exist
    }

    public Company selectCompanyName(String query, String companyName) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            // Set parameter
            statement.setString(1, companyName);
            try (ResultSet resultSetItem = statement.executeQuery()) {
                if (resultSetItem.next()) {
                    // Retrieve column values
                    // Make sure to use the correct column name here
                    //int companyId = resultSetItem.getInt("ID"); // Check if "ID" is the correct column name
                    String name = resultSetItem.getString("NAME");
                    return new Company(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addCompany(String insertData, Company company) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(insertData, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Set the value of the parameter
            statement.setString(1, company.getName());

            // Execute the insert query
            int affectedRows = statement.executeUpdate();

            // Check if any rows were affected
            if (affectedRows == 0) {
                throw new SQLException("Creating company failed, no rows affected.");
            }

            // Retrieve the generated keys (ID)
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Set the generated ID to the Company object
                    int generatedId = generatedKeys.getInt(1);
                    company.setId(generatedId);
                } else {
                    throw new SQLException("Creating company failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> selectForListCustomer(String query) {
        List<Customer> customers = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                int id = resultSetItem.getInt("ID"); // Retrieve company ID
                String name = resultSetItem.getString("NAME");
                Customer customer = new Customer(name, id); // Pass the companyId to the Company constructor
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Customer selectCustomer(String query, int id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            // Set parameter
            statement.setInt(1, id);

            try (ResultSet resultSetItem = statement.executeQuery()) {
                if (resultSetItem.next()) {
                    // Retrieve column values
                    resultSetItem.getInt("ID");
                    String name = resultSetItem.getString("NAME");
                    return new Customer(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addCustomer(String insertData, Customer customer) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(insertData, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Set the value of the parameter
            statement.setString(1, customer.getName());

            // Execute the insert query
            int affectedRows = statement.executeUpdate();

            // Check if any rows were affected
            if (affectedRows == 0) {
                throw new SQLException("Creating company failed, no rows affected.");
            }

            // Retrieve the generated keys (ID)
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Set the generated ID to the Company object
                    int generatedId = generatedKeys.getInt(1);
                    customer.setId(generatedId);
                } else {
                    throw new SQLException("Creating company failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer selectCustomerName(String query, String customerName) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            // Set parameter
            statement.setString(1, customerName);
            try (ResultSet resultSetItem = statement.executeQuery()) {
                if (resultSetItem.next()) {
                    // Retrieve column values
                    // Make sure to use the correct column name here
                    //int companyId = resultSetItem.getInt("ID"); // Check if "ID" is the correct column name
                    String name = resultSetItem.getString("NAME");
                    return new Customer(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRentedCarId(String updateData,Customer customer, Integer carId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateData)) {
            statement.setString(1, customer.getName());
            statement.setInt(2, carId);
            statement.setInt(3, customer.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNotRentedCarId(String updateData, Customer customer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateData)) {
            statement.setString(1, customer.getName());
            statement.setNull(2,Types.INTEGER);
            statement.setInt(3, customer.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
