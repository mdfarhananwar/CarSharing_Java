package carsharing.db;

import java.util.List;

public interface CarDao {
    List<Car> findAllByCompanyId(int companyId);
    void add(String name, int companyId);
    void update(Car car);
    void deleteById(int id);
}
