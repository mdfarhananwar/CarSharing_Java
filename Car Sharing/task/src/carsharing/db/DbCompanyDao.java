package carsharing.db;

import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

public class DbCompanyDao implements CompanyDao {
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS COMPANY(" +
            "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(40) UNIQUE NOT NULL);";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT = "SELECT * FROM COMPANY WHERE ID = ?";
    private static final String INSERT_DATA = "INSERT INTO COMPANY (NAME) VALUES (?)";

    private static final String UPDATE_DATA = "UPDATE COMPANY SET NAME " +
            "= '%s' WHERE ID = %d";
    private static final String DELETE_DATA = "DELETE FROM COMPANY WHERE ID = %d";
    private static final String SELECT_NAME = "SELECT NAME FROM COMPANY WHERE NAME = ?";
    private final DbClient dbClient;

    public DbCompanyDao(String url) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(url);
        dbClient = new DbClient(dataSource);
        dbClient.run(CREATE_DB);
        //System.out.println("Companies data structure create");
    }
    @Override
    public void add(Company company) {
        dbClient.addCompany(INSERT_DATA, company);
    }

    @Override
    public List<Company> findAll() {
        return dbClient.selectForListCompany(SELECT_ALL);
    }

    @Override
    public Company findById(int id) {
        Company company = dbClient.selectCompany(SELECT, id);

        if (company != null) {
            System.out.println("Company: Id " + id + ", found");
            return company;
        } else {
            System.out.println("Company: Id " + id + ", not found");
            return null;
        }
    }
    @Override
    public void update(Company company) {
        dbClient.run(String.format(
                UPDATE_DATA, company.getName(), company.getId()));
        System.out.println("Company: Id " + company.getId() + ", updated");
    }
    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE_DATA, id));
        System.out.println("Company: Id " + id + ", deleted");
    }

    @Override
    public void addCar(Car car) {

    }
    @Override
    public Company findByName(String companyName) {
        Company company = dbClient.selectCompanyName(SELECT_NAME, companyName);

        if (company != null) {
            System.out.println("Company: Name " + companyName + ", found");
            System.out.println(company);
            return company;
        } else {
            System.out.println("Company: Name " + companyName + ", not found");
            return null;
        }
    }
}
