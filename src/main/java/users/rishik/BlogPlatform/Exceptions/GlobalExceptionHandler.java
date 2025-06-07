package users.rishik.BlogPlatform.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: Validation errors (DTO fields with @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessage, request);
    }

    // 500: Invalid Enum value
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = "Invalid input: possibly wrong enum value. " + ex.getMostSpecificCause().getMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, request);
    }

    // 400: Constraint violations (e.g., query params, path vars)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint Violation", message, request);
    }

    // 400: Missing required parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = "Missing parameter: " + ex.getParameterName();
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", message, request);
    }

    // 400: Type mismatch in query/path
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Invalid value for '" + ex.getName() + "': expected " + ex.getRequiredType().getSimpleName();
        return buildResponse(HttpStatus.BAD_REQUEST, "Type Mismatch", message, request);
    }

    // 500: Invalid Endpoint
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundPath(NoHandlerFoundException ex, HttpServletRequest request) {
        String message = "No endpoint found for path: " + request.getRequestURI();
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", message, request);
    }

    // 405: Wrong HTTP method (e.g., POST to GET endpoint)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", ex.getMessage(), request);
    }

    // 415: Unsupported content type (e.g., XML when only JSON supported)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type", ex.getMessage(), request);
    }

    // 403: User authenticated but not authorized
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to access this resource.", request);
    }

    // 401: Authentication failure (bad credentials, expired token, etc.)
    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            AccountExpiredException.class,
            LockedException.class,
            DisabledException.class,
            CredentialsExpiredException.class,
            AuthenticationCredentialsNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), request);
    }

    // 409: Unique constraint or foreign key violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Data Conflict", "Database constraint violation.", request);
    }

    // 400: Invalid state/argument
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiErrorResponse> handleIllegalArgs(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Input", ex.getMessage(), request);
    }

    // 404: Custom Not Found Exception
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    // 500: Catch-all for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandled(Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); // Log this in production
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred.", request);
    }

    // Utility method to build the error response
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }
}
