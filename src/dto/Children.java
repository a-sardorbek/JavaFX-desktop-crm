
package dto;


public class Children {
    
    private int id,employeeId;
    private String fio,dob;
    
    public Children(){}

    public Children(int id, int employeeId, String fio, String dob) {
        this.id = id;
        this.employeeId = employeeId;
        this.fio = fio;
        this.dob = dob;
    }
    
     public Children(int id, String fio, String dob) {
        this.id = id;
        this.fio = fio;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
    
    
}
