package lk.crystal.asset.employee.entity.enums;

public enum EmployeeStatus {
    WORKING("Working"),
    LEAVE("Leave"),
    SUSPENDED("Suspended"),
    NOPAY("No pay"),
    MEDICAL("Medical Leave"),
    BLOCK("Block"),
    RESIGNED("Resigned"),
    RETIRED("Retired");

    private final String employeeStatus;

    EmployeeStatus(String employeeStatus) {

        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }
}
