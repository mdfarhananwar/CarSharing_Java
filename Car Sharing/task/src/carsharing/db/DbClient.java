package carsharing.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Company select(String query) {
        List<Company> companies = selectForList(query);
        if (companies.size() == 1) {
            return companies.get(0);
        } else if (companies.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("Query returned more than one object");
        }
    }

    public List<Company> selectForList(String query) {
        List<Company> companies = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                // Retrieve column values
                resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                Company company = new Company(name);
                companies.add(company);
            }

            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }
}
