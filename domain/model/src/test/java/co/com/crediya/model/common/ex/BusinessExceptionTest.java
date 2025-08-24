package co.com.crediya.model.common.ex;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.function.Supplier;

public class BusinessExceptionTest {

    @Test
    public void testException() {
        final BusinessException ex = BusinessException.Type.FIRSTNAME_REQUIRED.build();
        assertThat(ex).hasMessage("First name is required");
        assertThat(ex.getCode()).isEqualTo("FIRSTNAME_REQUIRED");
    }

    @Test
    public void testExceptionDefer() {
        final Supplier<Throwable> defer = BusinessException.Type.FIRSTNAME_REQUIRED.defer();
        assertThat(defer.get()).hasMessage("First name is required");
        assertThat(((BusinessException)defer.get()).getCode()).isEqualTo("FIRSTNAME_REQUIRED");
    }
}