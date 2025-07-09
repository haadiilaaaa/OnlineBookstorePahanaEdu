package sql;

public class AdminSQL {
    public static final String INSERT_ADMIN =
        "INSERT INTO admin (id, username, first_name, last_name, email, contact_number, password_hash, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String FIND_BY_EMAIL = "SELECT * FROM admin WHERE email = ?";
    public static final String FIND_BY_USERNAME = "SELECT * FROM admin WHERE username = ?";
    public static final String FIND_BY_ID = "SELECT * FROM admin WHERE id = ?";
    public static final String FIND_BY_USERNAME_OR_EMAIL = "SELECT * FROM admin WHERE username = ? OR email = ?";
    public static final String COUNT_ADMINS = "SELECT COUNT(*) FROM admin";
    public static final String VERIFY_ADMIN = "UPDATE admin SET is_verified = TRUE WHERE id = ?";
    public static final String UPDATE_PASSWORD = "UPDATE admin SET password_hash = ? WHERE id = ?";
    public static final String GET_MAX_ADMIN_ID =
        "SELECT MAX(CAST(SUBSTRING(id, LENGTH(?) + 1) AS UNSIGNED)) AS max_id FROM admin WHERE id LIKE ?";
}
