package carsharing.db;

import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

public class DbCarDao implements CarDao{
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CAR (" +
            "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
            "NAME VARCHAR(40) UNIQUE NOT NULL, " +
            "COMPANY_ID INTEGER NOT NULL, " +
            "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
    private static final String SELECT_ALL = "SELECT * FROM CAR WHERE COMPANY_ID = ?";
    private static final String INSERT_DATA = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
    private static final String UPDATE_DATA = "UPDATE CAR SET NAME " +
            "= '%s' WHERE ID = %d";
    private static final String DELETE_DATA = "DELETE FROM CAR WHERE ID = %d";
    private final DbClient dbClient;

    public DbCarDao(String url) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(url);
        dbClient = new DbClient(dataSource);
        dbClient.run(CREATE_DB);
        //System.out.println("Companies data structure create");
    }
    @Override
    public List<Car> findAllByCompanyId(int companyId) {
        return dbClient.selectForListCar(SELECT_ALL, companyId);
    }
    @Override
    public void add(String name, int companyId) {
        dbClient.addCar(
                INSERT_DATA, name, companyId);
    }
    @Override
    public void update(Car car) {
        dbClient.run(String.format(
                UPDATE_DATA, car.getName(), car.getId()));
        System.out.println("Car: Id " + car.getId() + ", updated");
    }
    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE_DATA, id));
    }
}
