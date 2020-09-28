package cloud.nativ.quarkus;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.spi.HealthCheckResponseProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MicroserviceLivenessCheckTest implements HealthCheckResponseProvider {

    @BeforeEach
    void setUp() {
        HealthCheckResponse.setResponseProvider(this);
    }

    @Test
    void callLivenessCheck() {
        MicroserviceLivenessCheck livenessCheck = new MicroserviceLivenessCheck();
        HealthCheckResponse response = livenessCheck.call();
        assertNotNull(response, "Response should not be NULL.");
    }

    @Override
    public HealthCheckResponseBuilder createResponseBuilder() {
        return new TestableHealthCheckResponseBuilder();
    }
}