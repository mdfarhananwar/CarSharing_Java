package carsharing.db;

import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

public class DbCompanyDao implements CompanyDao {

    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS COMPANY(" +
            "ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(40) UNIQUE NOT NULL);";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT = "SELECT * FROM COMPANY WHERE id = %d";
    private static final String INSERT_DATA = "INSERT INTO COMPANY (NAME) VALUES ('%s')";

    private static final String UPDATE_DATA = "UPDATE COMPANY SET NAME " +
            "= '%s' WHERE id = %d";
    private static final String DELETE_DATA = "DELETE FROM COMPANY WHERE id = %d";

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
        dbClient.run(String.format(
                INSERT_DATA, company.getName()));
    }

    @Override
    public List<Company> findAll() {
        return dbClient.selectForList(SELECT_ALL);
    }

    @Override
    public Company findById(int id) {
        Company company = dbClient.select(String.format(SELECT, id));

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
}
