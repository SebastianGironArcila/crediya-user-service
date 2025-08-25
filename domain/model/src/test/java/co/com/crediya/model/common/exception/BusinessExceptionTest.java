package co.com.crediya.model.common.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.function.Supplier;

public class BusinessExceptionTest {

    @Test
    public void testException() {
        final BusinessException ex = BusinessException.Type.EMAIL_ALREADY_REGISTERED.build();
        assertThat(ex).hasMessage("Email is already registered");
        assertThat(ex.getCode()).isEqualTo("EMAIL_ALREADY_REGISTERED");
    }

    @Test
    public void testExceptionDefer() {
        final Supplier<Throwable> defer = BusinessException.Type.EMAIL_ALREADY_REGISTERED.defer();
        assertThat(defer.get()).hasMessage("Email is already registered");
        assertThat(((BusinessException)defer.get()).getCode()).isEqualTo("EMAIL_ALREADY_REGISTERED");
    }
}