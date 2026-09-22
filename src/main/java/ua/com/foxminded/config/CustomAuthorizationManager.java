package ua.com.foxminded.config;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import ua.com.foxminded.security.CustomUserDetails;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final AuthorizationManager<RequestAuthorizationContext> delegate;

    private CustomAuthorizationManager(AuthorizationManager<RequestAuthorizationContext> delegate) {
        this.delegate = delegate;
    }

    public static CustomAuthorizationManager hasAnyRole(String... roles) {
        return new CustomAuthorizationManager(AuthorityAuthorizationManager.hasAnyRole(roles));
    }

    public static CustomAuthorizationManager authenticated() {
        return new CustomAuthorizationManager(AuthenticatedAuthorizationManager.authenticated());
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext object) {

        AuthorizationDecision roleDecision = delegate.check(authenticationSupplier, object);

        if (roleDecision == null || !roleDecision.isGranted()) {
            return roleDecision;
        }

        Authentication authentication = authenticationSupplier.get();

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return new AuthorizationDecision(true);
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return new AuthorizationDecision(false);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new AuthorizationDecision(userDetails.isRegistrationCompleted());
    }

}
