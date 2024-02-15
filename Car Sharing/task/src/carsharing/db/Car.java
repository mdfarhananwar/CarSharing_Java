package carsharing.db;

public class Car {
    private Integer id;
    private String name;
    private Integer companyId;
    Company company;

    public Car() {
    }
    public Car(String name,Integer companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public Car(Integer id, String name,Integer companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                ", company=" + company +
                '}';
    }
}
