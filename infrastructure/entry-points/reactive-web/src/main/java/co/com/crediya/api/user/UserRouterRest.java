package co.com.crediya.api.user;

import co.com.crediya.api.dto.CreateUserDTO;
import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.api.dto.LoginResponseDTO;
import co.com.crediya.api.dto.UserDTO;
import co.com.crediya.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    beanClass = UserHandler.class,
                    beanMethod = "registerUser",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Register a new ADMINISTRATOR",
                            description = "Registers a new ADMINISTRATOR with personal data: first name, last name, birth date, address, phone, email, identity document, base salary and role. " +
                                    "Email must be unique and all required fields must be provided with valid formats.",
                            security = { @SecurityRequirement(name = "bearerAuth") },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "ADMINISTRATOR registration data",
                                    content = @Content(
                                            schema = @Schema(implementation = CreateUserDTO.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Scenario 1 - Successful registration",
                                                            summary = "Valid ADMINISTRATOR data",
                                                            value = """
                                                                    {
                                                                        "firstName": "John",
                                                                        "lastName": "Doe",
                                                                        "birthDate": "1990-05-15",
                                                                        "address": "123 Main St, City",
                                                                        "phone": "+573001234567",
                                                                        "email": "john.doe@email.com",
                                                                        "identityDocument": "1234567890",
                                                                        "baseSalary": 2500000.00,
                                                                        "roleName": "ADMINISTRATOR",
                                                                        "password": "password"
                                                                    }
                                                                    """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 2 - Invalid email format",
                                                            summary = "Email with incorrect format",
                                                            value = """
                                                                    {
                                                                        "firstName": "Jane",
                                                                        "lastName": "Smith",
                                                                        "birthDate": "1985-12-20",
                                                                        "address": "456 Oak Ave, Town",
                                                                        "phone": "+573009876543",
                                                                        "email": "invalid-email-format",
                                                                        "identityDocument": "0987654321",
                                                                        "baseSalary": 3000000.00,
                                                                        "roleName": "ADMINISTRATOR",
                                                                         "password": "password"
                                                                    }
                                                                    """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 3 - Base salary out of range",
                                                            summary = "Salary below minimum or above maximum",
                                                            value = """
                                                                    {
                                                                        "firstName": "Robert",
                                                                        "lastName": "Johnson",
                                                                        "birthDate": "1992-08-10",
                                                                        "address": "789 Pine Rd, Village",
                                                                        "phone": "+573005551234",
                                                                        "email": "robert.johnson@email.com",
                                                                        "identityDocument": "1122334455",
                                                                        "baseSalary": -500000.00,
                                                                        "roleName": "ADMINISTRATOR",
                                                                         "password": "password"
                                                                    }
                                                                    """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 4 - Missing required fields",
                                                            summary = "Incomplete registration data",
                                                            value = """
                                                                    {
                                                                        "firstName": "",
                                                                        "lastName": "Wilson",
                                                                        "birthDate": "1988-03-25",
                                                                        "address": "321 Cedar Ln, Borough",
                                                                        "phone": "+573004447788",
                                                                        "email": "sarah.wilson@email.com",
                                                                        "identityDocument": "5566778899",
                                                                        "baseSalary": 4000000.00,
                                                                        "roleName": "",
                                                                         "password": "password"
                                                                    }
                                                                    """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 5 - Duplicate email attempt",
                                                            summary = "Email already registered in system",
                                                            value = """
                                                                    {
                                                                        "firstName": "Michael",
                                                                        "lastName": "Brown",
                                                                        "birthDate": "1995-11-30",
                                                                        "address": "654 Elm St, District",
                                                                        "phone": "+573006667777",
                                                                        "email": "john.doe@email.com",
                                                                        "identityDocument": "9988776655",
                                                                        "baseSalary": 3500000.00,
                                                                        "roleName": "ADMINISTRATOR",
                                                                         "password": "password"
                                                                    }
                                                                    """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 6 - Non-existent role",
                                                            summary = "Role not configured in system",
                                                            value = """
                                                                    {
                                                                        "firstName": "Emily",
                                                                        "lastName": "Davis",
                                                                        "birthDate": "1993-07-14",
                                                                        "address": "987 Birch Blvd, County",
                                                                        "phone": "+573008889999",
                                                                        "email": "emily.davis@email.com",
                                                                        "identityDocument": "4433221100",
                                                                        "baseSalary": 2800000.00,
                                                                        "roleName": "FARMER",
                                                                         "password": "password"
                                                                    }
                                                                    """
                                                    )
                                            }
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "ADMINISTRATOR registered successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = User.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Success response",
                                                                    value = """
                                                                            {
                                                                                "id": 1,
                                                                                "firstName": "John",
                                                                                "lastName": "Doe",
                                                                                "birthDate": "1990-05-15",
                                                                                "address": "123 Main St, City",
                                                                                "phone": "+573001234567",
                                                                                "email": "john.doe@email.com",
                                                                                "identityDocument": "1234567890",
                                                                                "baseSalary": 2500000.00,
                                                                                "roleId": 2,
                                                                                "createdAt": "2024-01-15T10:30:00Z",
                                                                                "updatedAt": "2024-01-15T10:30:00Z"
                                                                            }
                                                                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad Request - Validation failed",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Invalid email format",
                                                                    value = """
                                                                            {
                                                                                "timestamp": "2024-01-15T10:30:00Z",
                                                                                "status": 400,
                                                                                "error": "Bad Request",
                                                                                "message": "Invalid format email",
                                                                                "path": "/api/v1/users"
                                                                            }
                                                                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Salary out of range",
                                                                    value = """
                                                                            {
                                                                                "timestamp": "2024-01-15T10:30:00Z",
                                                                                "status": 400,
                                                                                "error": "Bad Request",
                                                                                "message": "BaseSalary must be greater than 0.",
                                                                                "path": "/api/v1/users"
                                                                            }
                                                                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Missing required field",
                                                                    value = """
                                                                            {
                                                                                "timestamp": "2024-01-15T10:30:00Z",
                                                                                "status": 400,
                                                                                "error": "Bad Request",
                                                                                "message": "FirstName is required",
                                                                                "path": "/api/v1/users"
                                                                            }
                                                                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Conflict - Email already registered",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Duplicate email",
                                                                    value = """
                                                                            {
                                                                                "timestamp": "2024-01-15T10:30:00Z",
                                                                                "status": 409,
                                                                                "error": "Conflict",
                                                                                "message": "Email is already registered",
                                                                                "path": "/api/v1/users"
                                                                            }
                                                                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Not Found - Role does not exist",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Non-existent role",
                                                                    value = """
                                                                            {
                                                                                "timestamp": "2024-01-15T10:30:00Z",
                                                                                "status": 404,
                                                                                "error": "Not Found",
                                                                                "message": "Role not found: ADMINISTRATOR",
                                                                                "path": "/api/v1/users"
                                                                            }
                                                                            """
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/identity-document/{identityDocument}",
                    beanClass = UserHandler.class,
                    beanMethod = "getUserByIdentityDocument",
                    operation = @Operation(
                            operationId = "getUserByIdentityDocument",
                            summary = "Get user by identity document",
                            description = "Retrieves user information by identity document number. The identity document is validated (must be 6-10 digits) before searching. Used by other microservices to validate user existence for credit applications.",
                            security = { @SecurityRequirement(name = "bearerAuth") },
                            parameters = {
                                    @Parameter(
                                            name = "identityDocument",
                                            description = "Identity document number (cédula, DNI, passport, etc.), 6-10 digits",
                                            required = true,
                                            example = "1234567890",
                                            in = ParameterIn.PATH,
                                            schema = @Schema(type = "string", pattern = "\\d{6,10}")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User found successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTO.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Scenario 1 - Standard user found",
                                                                    summary = "Valid identity document, user exists",
                                                                    value = """
                            {
                              "id": 1,
                              "firstName": "John",
                              "lastName": "Doe",
                              "birthDate": "1990-05-15",
                              "address": "123 Main St, City",
                              "phone": "+573001234567",
                              "email": "john.doe@email.com",
                              "identityDocument": "1234567890",
                              "baseSalary": 2500000.00,
                              "roleName": "Admin"
                            }
                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Scenario 2 - Another user found",
                                                                    summary = "Different identity document, user exists",
                                                                    value = """
                            {
                              "id": 2,
                              "firstName": "Emily",
                              "lastName": "Davis",
                              "birthDate": "1993-07-14",
                              "address": "987 Birch Blvd, County",
                              "phone": "+573008889999",
                              "email": "emily.davis@email.com",
                              "identityDocument": "4433221100",
                              "baseSalary": 2800000.00,
                              "roleName": "Manager"
                            }
                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad Request - Invalid identity document format",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Scenario 1 - Blank identity document",
                                                                    summary = "Identity document is empty",
                                                                    value = """
                            {
                              "timestamp": "2024-01-15T10:30:00Z",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Identity document cannot be blank",
                              "path": "/api/v1/users/identity-document/"
                            }
                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Scenario 2 - Identity document too short",
                                                                    summary = "Identity document less than 6 digits",
                                                                    value = """
                            {
                              "timestamp": "2024-01-15T10:30:00Z",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Identity document must be 6-10 digits",
                              "path": "/api/v1/users/identity-document/123"
                            }
                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Scenario 3 - Identity document too long",
                                                                    summary = "Identity document more than 10 digits",
                                                                    value = """
                            {
                              "timestamp": "2024-01-15T10:30:00Z",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Identity document must be 6-10 digits",
                              "path": "/api/v1/users/identity-document/123456789012"
                            }
                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Not Found - User not found",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Scenario 1 - User does not exist",
                                                                    summary = "Identity document valid but no user found",
                                                                    value = """
                            {
                              "timestamp": "2024-01-15T10:30:00Z",
                              "status": 404,
                              "error": "Not Found",
                              "message": "User not found with identity document: 9999999999",
                              "path": "/api/v1/users/identity-document/9999999999"
                            }
                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal Server Error - Database or system error",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Scenario 1 - Database connection failure",
                                                                    summary = "Error connecting to database",
                                                                    value = """
                            {
                              "timestamp": "2024-01-15T10:30:00Z",
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Database connection failed",
                              "path": "/api/v1/users/identity-document/1234567890"
                            }
                            """
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    beanClass = UserHandler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Authenticate a user and generate access token",
                            description = "Authenticates a user using email and password. If credentials are valid, returns a JWT token to be used for subsequent requests. If invalid, returns 401 Unauthorized.",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Login credentials",
                                    content = @Content(
                                            schema = @Schema(implementation = LoginRequestDTO.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Scenario 1 - Successful login",
                                                            summary = "Valid credentials",
                                                            value = """
                                                        {
                                                          "email": "admin@example.com",
                                                          "password": "password"
                                                        }
                                                        """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 2 - Invalid email format",
                                                            summary = "Incorrect email format",
                                                            value = """
                                                        {
                                                          "email": "invalid-email",
                                                          "password": "anyPassword"
                                                        }
                                                        """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Scenario 3 - Missing password",
                                                            summary = "Password not provided",
                                                            value = """
                                                        {
                                                          "email": "john.doe@email.com",
                                                          "password": ""
                                                        }
                                                        """
                                                    )
                                            }
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login successful - JWT token generated",
                                            content = @Content(
                                                    schema = @Schema(implementation = LoginResponseDTO.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Success response",
                                                                    value = """
                                                                {
                                                                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
                                                                }
                                                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad Request - Validation failed",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Invalid email format",
                                                                    value = """
                                                                {
                                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                                  "status": 400,
                                                                  "error": "Bad Request",
                                                                  "message": "Invalid format email",
                                                                  "path": "/api/v1/login"
                                                                }
                                                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Missing password",
                                                                    value = """
                                                                {
                                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                                  "status": 400,
                                                                  "error": "Bad Request",
                                                                  "message": "Password is required",
                                                                  "path": "/api/v1/login"
                                                                }
                                                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - Invalid credentials",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Wrong password",
                                                                    value = """
                                                                {
                                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                                  "status": 401,
                                                                  "error": "Unauthorized",
                                                                  "message": "Invalid credentials",
                                                                  "path": "/api/v1/login"
                                                                }
                                                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Email not found",
                                                                    value = """
                                                                {
                                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                                  "status": 401,
                                                                  "error": "Unauthorized",
                                                                  "message": "Invalid credentials",
                                                                  "path": "/api/v1/login"
                                                                }
                                                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal Server Error - System error",
                                            content = @Content(
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Database failure",
                                                                    value = """
                                                                {
                                                                  "timestamp": "2024-01-15T10:30:00Z",
                                                                  "status": 500,
                                                                  "error": "Internal Server Error",
                                                                  "message": "Database connection failed",
                                                                  "path": "/api/v1/login"
                                                                }
                                                                """
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return route(POST("/api/v1/users"), handler::registerUser)
                .andRoute(GET("/api/v1/users/identity-document/{identityDocument}"),
                        handler::getUserByIdentityDocument)
                .andRoute(POST("/api/v1/login"), handler::login);
    }
}