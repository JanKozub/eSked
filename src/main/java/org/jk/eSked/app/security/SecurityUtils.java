package org.jk.eSked.app.security;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SecurityUtils {

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    static boolean isAccessGranted(Class<?> securedClass) {
        Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        if (secured == null) {
            return true;
        }

        List<String> allowedRoles = Arrays.asList(secured.value());
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        return userAuthentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);
    }
}
