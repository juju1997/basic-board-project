package basic.boardproject.config;

import basic.boardproject.domain.UserAccount;
import basic.boardproject.repository.UserAccountRepository;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "jujuTest",
                "pwTest",
                "juju@mail.com",
                "juju-test",
                "test memo"
        )));
    }
}
