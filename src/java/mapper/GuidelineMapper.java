package mapper;

import model.Guideline;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuidelineMapper {

    /**
     * Maps the current row of the given ResultSet to a Guideline object.
     *
     * @param rs the ResultSet, already positioned at the current row
     * @return a Guideline object mapped from the ResultSet row
     * @throws SQLException if a database access error occurs
     */
    public static Guideline map(ResultSet rs) throws SQLException {
        Guideline g = new Guideline();
        g.setId(rs.getString("id"));
        g.setTitle(rs.getString("title"));
        g.setContent(rs.getString("content"));
        return g;
    }
}
