package carsharing.db;

import java.util.List;

public interface CustomerDao {
    List<Customer> findAll();
    Customer findById(int id);
    void add(Customer customer);
    void update(Customer customer, Integer carId);
    void deleteById(int id);
    void updateNotRentedCarID(Customer customer);
    //void addCar(Car car);

    Customer findByName(String customerName);
}
