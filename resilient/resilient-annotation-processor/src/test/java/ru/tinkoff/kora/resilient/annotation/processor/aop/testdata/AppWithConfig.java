package ru.tinkoff.kora.resilient.annotation.processor.aop.testdata;

import com.typesafe.config.ConfigFactory;
import ru.tinkoff.kora.common.KoraApp;
import ru.tinkoff.kora.config.common.Config;
import ru.tinkoff.kora.config.common.DefaultConfigExtractorsModule;
import ru.tinkoff.kora.config.common.origin.SimpleConfigOrigin;
import ru.tinkoff.kora.config.hocon.HoconConfigFactory;
import ru.tinkoff.kora.resilient.circuitbreaker.simple.CircuitBreakerModule;
import ru.tinkoff.kora.resilient.fallback.simple.FallbackModule;
import ru.tinkoff.kora.resilient.retry.simple.RetryableModule;
import ru.tinkoff.kora.resilient.timeout.simple.TimeoutModule;

@KoraApp
public interface AppWithConfig extends CircuitBreakerModule, FallbackModule, TimeoutModule, RetryableModule, DefaultConfigExtractorsModule {

    default Config config() {
        return HoconConfigFactory.fromHocon(new SimpleConfigOrigin("test"), ConfigFactory.parseString(
            """
                resilient {
                  circuitbreaker {
                    default {
                      slidingWindowSize = 1
                      minimumRequiredCalls = 1
                      failureRateThreshold = 100
                      permittedCallsInHalfOpenState = 1
                      waitDurationInOpenState = 1s
                    }
                  }
                  timeout {
                    default {
                      duration = 200ms
                    }
                  }
                  retry {
                    default {
                      delay = 100ms
                      attempts = 2
                    }
                  }
                }
                """
        ).resolve());
    }
}
