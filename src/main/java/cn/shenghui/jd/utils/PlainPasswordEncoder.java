package cn.shenghui.jd.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author shenghui
 * @version 1.0
 * @since 2019/8/22 11:04
 * Replace Spring Security 5.x NoOpPasswordEncoder
 */
public final class PlainPasswordEncoder implements PasswordEncoder {

    private static final PasswordEncoder INSTANCE = new PlainPasswordEncoder();

    private PlainPasswordEncoder() {

    }

    /**
     * Get the singleton {@link PlainPasswordEncoder}.
     */
    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    /**
     * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
     * greater hash combined with an 8-byte or greater randomly generated salt.
     *
     * @param rawPassword
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * Verify the encoded password obtained from storage matches the submitted raw
     * password after it too is encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param rawPassword     the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from
     * storage
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
