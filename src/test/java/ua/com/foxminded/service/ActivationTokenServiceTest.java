package ua.com.foxminded.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.repository.ActivationTokenRepository;

@ExtendWith(MockitoExtension.class)
class ActivationTokenServiceTest {

    @Mock
    private ActivationTokenRepository tokenRepo;

    @InjectMocks
    private ActivationTokenService activationTokenService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = mock(AppUser.class);
    }

    @Test
    void createToken_shouldDeletePreviousTokenForUser() {
        activationTokenService.createToken(user);

        verify(tokenRepo).deleteByUser(user);
    }

    @Test
    void createToken_shouldDeleteBeforeSavingNewToken() {
        activationTokenService.createToken(user);

        InOrder inOrder = Mockito.inOrder(tokenRepo);
        inOrder.verify(tokenRepo).deleteByUser(user);
        inOrder.verify(tokenRepo).save(any(ActivationToken.class));
    }

    @Test
    void createToken_shouldSaveTokenWithCorrectUserAndExpiry() {
        LocalDateTime beforeCall = LocalDateTime.now();

        activationTokenService.createToken(user);

        ArgumentCaptor<ActivationToken> tokenCaptor = ArgumentCaptor.forClass(ActivationToken.class);
        verify(tokenRepo).save(tokenCaptor.capture());

        ActivationToken savedToken = tokenCaptor.getValue();

        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.getToken()).isNotBlank();
        assertThat(savedToken.getExpiryDate()).isCloseTo(beforeCall.plusHours(48), within(2, ChronoUnit.SECONDS));
    }

    @Test
    void createToken_shouldReturnValidUuidStringMatchingSavedToken() {
        String returnedToken = activationTokenService.createToken(user);

        ArgumentCaptor<ActivationToken> tokenCaptor = ArgumentCaptor.forClass(ActivationToken.class);
        verify(tokenRepo).save(tokenCaptor.capture());

        assertThat(returnedToken).isEqualTo(tokenCaptor.getValue().getToken());
        // не должно бросить исключение — значит это валидный UUID
        assertThat(UUID.fromString(returnedToken)).isNotNull();
    }

    @Test
    void createToken_shouldGenerateDifferentTokensOnEachCall() {
        String firstToken = activationTokenService.createToken(user);
        String secondToken = activationTokenService.createToken(user);

        assertThat(firstToken).isNotEqualTo(secondToken);
    }

}
