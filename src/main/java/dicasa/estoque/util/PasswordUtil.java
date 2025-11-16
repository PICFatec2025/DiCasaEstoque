package dicasa.estoque.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordUtil {

    private static final int BCRYPT_STRENGTH = 10;
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

    private PasswordUtil() {}

    /** Gera hash BCrypt a partir da senha em texto plano */
    public static String hashPassword(String plainPassword) {
        return ENCODER.encode(plainPassword);
    }

    /** Verifica se a senha em texto bate com o hash BCrypt */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        return ENCODER.matches(plainPassword, hashedPassword);
    }
}
