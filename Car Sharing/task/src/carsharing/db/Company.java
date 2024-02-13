package carsharing.db;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private int id;
    private String name;
    private List<Car> cars;

    public Company() {
        cars = new ArrayList<>();
    }

    public Company(String name, int id) {
        this.name = name;
        cars = new ArrayList<>();
        this.id = id;
    }
    public Company(String name) {
        this.name = name;
        cars = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void removeCar(Car car) {
        cars.remove(car);
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
