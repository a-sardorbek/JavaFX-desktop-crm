
package dto;


public class Employee {
    
    private int id;
    private String fio,position,dob,phone;

    public Employee(int id, String fio, String position, String dob,String phone) {
        this.id = id;
        this.fio = fio;
        this.position = position;
        this.dob = dob;
        this.phone = phone;
    }
    
    public Employee(){}

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
    
    
    
}
