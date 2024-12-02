/**
 * Provides data access methods for Employee objects.
 */

package edu.jsu.mcis.cs310.tas_fa24.dao;

import edu.jsu.mcis.cs310.tas_fa24.Employee;
import edu.jsu.mcis.cs310.tas_fa24.Department;
import edu.jsu.mcis.cs310.tas_fa24.Badge;
import edu.jsu.mcis.cs310.tas_fa24.Shift;
import java.sql.*;

public class EmployeeDAO {
    /**
     * Constructs an EmployeeDAO with a DAOFactory.
     *
     * @param daoFactory The DAOFactory instance used for database connections.
     */

    private final DAOFactory daoFactory;

    public EmployeeDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Employee find(int id) {
     /**
     * Finds an Employee object by its ID.
     *
     * @param id The ID of the employee to find.
     * @return The Employee object if found, or null if not.
     */
        Employee employee = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();
            String query = "SELECT * FROM employee WHERE id = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                employee = extractEmployeeFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            DAOUtility.close(rs, pst);
        }

        return employee;
    }

    public Employee find(Badge badge) { 
     /**
     * Finds an Employee object by a Badge object.
     *
     * @param badge The Badge object used to locate the employee.
     * @return The Employee object if found, or null if not.
     */
        Employee employee = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();
            String query = "SELECT * FROM employee WHERE badgeid = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, badge.getId());
            rs = pst.executeQuery();

            if (rs.next()) {
                employee = extractEmployeeFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            DAOUtility.close(rs, pst);
        }

        return employee;
    }

    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("firstname");
        String middleName = rs.getString("middlename");
        String lastName = rs.getString("lastname");
        String badgeId = rs.getString("badgeid");
        int employeeTypeId = rs.getInt("employeetypeid");
        int departmentId = rs.getInt("departmentid");
        int shiftId = rs.getInt("shiftid");
        Date active = rs.getDate("active");
        
        Badge badge = daoFactory.getBadgeDAO().find(badgeId);
        Shift shift = daoFactory.getShiftDAO().find(shiftId);
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        Department department = departmentDAO.find(departmentId);

        return new Employee(id, badge, firstName, middleName, lastName, employeeTypeId, department, shift, active);
    }
}