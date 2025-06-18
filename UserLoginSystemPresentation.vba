Sub CreateUserLoginSystemPresentation()
    Dim pptApp As PowerPoint.Application
    Dim pptPres As PowerPoint.Presentation
    Dim pptSlide As PowerPoint.Slide
    Dim slideIndex As Integer
    
    ' Create PowerPoint application and presentation
    Set pptApp = New PowerPoint.Application
    pptApp.Visible = True
    Set pptPres = pptApp.Presentations.Add
    
    slideIndex = 1
    
    ' Slide 1: Title Slide
    Set pptSlide = pptPres.Slides.Add(slideIndex, 1) ' ppLayoutTitle = 1
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "User Login System"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = "Spring Boot Security Implementation" & vbCrLf & _
            "Four Authentication Approaches Demonstration" & vbCrLf & _
            "Java Spring Boot Application"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 2: Project Overview
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2) ' 2 = 2
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Project Overview"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "• Purpose: Educational demonstration of Spring Security authentication" & vbCrLf & _
            "• Technology: Java 17 + Spring Boot 3.2.0 + Spring Security" & vbCrLf & _
            "• Database: H2 In-Memory Database" & vbCrLf & _
            "• Architecture: Multi-profile layered Spring Boot application" & vbCrLf & _
            "• Build Tool: Maven" & vbCrLf & _
            "• Four distinct authentication approaches implemented"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 3: Technology Stack
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2) ' 2 = 2
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Technology Stack"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "Backend Framework:" & vbCrLf & _
            "• Spring Boot 3.2.0" & vbCrLf & _
            "• Spring Security (Authentication & Authorization)" & vbCrLf & _
            "• Spring Data JPA (Data Access)" & vbCrLf & vbCrLf & _
            "Database & Persistence:" & vbCrLf & _
            "• H2 In-Memory Database" & vbCrLf & _
            "• Hibernate ORM" & vbCrLf & vbCrLf & _
            "Additional Technologies:" & vbCrLf & _
            "• Jakarta Bean Validation" & vbCrLf & _
            "• BCrypt Password Encoding" & vbCrLf & _
            "• Maven Build System"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 4: Application Architecture
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Application Architecture"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "src/main/java/com/example/userlogin/" & vbCrLf & _
            "├── UserLoginApplication.java (Main Application)" & vbCrLf & _
            "├── config/ (Security Configurations)" & vbCrLf & _
            "├── controller/ (REST API Controllers)" & vbCrLf & _
            "├── dto/ (Data Transfer Objects)" & vbCrLf & _
            "├── entity/ (JPA Entities)" & vbCrLf & _
            "├── repository/ (Data Access Layer)" & vbCrLf & _
            "└── service/ (Business Logic Layer)" & vbCrLf & vbCrLf & _
            "resources/" & vbCrLf & _
            "├── application.yml (Configuration)" & vbCrLf & _
            "├── schema.sql (Database Schema)" & vbCrLf & _
            "└── data.sql (Test Data)"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 5: Four Authentication Approaches
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Four Authentication Approaches"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "1. InMemory Authentication (/api/inmemory/*)" & vbCrLf & _
            "   • Users stored in application memory" & vbCrLf & _
            "   • Uses InMemoryUserDetailsManager" & vbCrLf & vbCrLf & _
            "2. JDBC Authentication (/api/jdbc/*)" & vbCrLf & _
            "   • Users stored in database" & vbCrLf & _
            "   • Uses JdbcUserDetailsManager" & vbCrLf & vbCrLf & _
            "3. Custom JPA Authentication (/api/custom-jpa/*)" & vbCrLf & _
            "   • Custom JPA entities and repositories" & vbCrLf & _
            "   • Custom UserDetailsService implementation" & vbCrLf & vbCrLf & _
            "4. Manual Authentication (/api/manual/*)" & vbCrLf & _
            "   • Manual AuthenticationProvider configuration" & vbCrLf & _
            "   • Full control over authentication process"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 6: API Endpoints Structure
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "API Endpoints Structure"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "Each authentication approach provides identical endpoints:" & vbCrLf & vbCrLf & _
            "POST /api/{approach}/register" & vbCrLf & _
            "• User registration endpoint" & vbCrLf & vbCrLf & _
            "POST /api/{approach}/login" & vbCrLf & _
            "• User authentication endpoint" & vbCrLf & vbCrLf & _
            "GET /api/{approach}/user/profile" & vbCrLf & _
            "• User profile access (USER role required)" & vbCrLf & vbCrLf & _
            "GET /api/{approach}/admin/dashboard" & vbCrLf & _
            "• Admin dashboard (ADMIN role required)" & vbCrLf & vbCrLf & _
            "Where {approach} = inmemory | jdbc | custom-jpa | manual"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 7: Core Domain Objects
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Core Domain Objects"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "User Entity:" & vbCrLf & _
            "• Fields: id, username, email, password, role" & vbCrLf & _
            "• Roles: USER, ADMIN" & vbCrLf & _
            "• JPA annotations for database mapping" & vbCrLf & vbCrLf & _
            "Data Transfer Objects (DTOs):" & vbCrLf & _
            "• LoginRequest: Username/password credentials" & vbCrLf & _
            "• RegistrationRequest: User registration data" & vbCrLf & _
            "• AuthResponse: Authentication response status" & vbCrLf & vbCrLf & _
            "Security Features:" & vbCrLf & _
            "• BCrypt password encoding" & vbCrLf & _
            "• Role-based access control" & vbCrLf & _
            "• Jakarta Bean Validation"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 8: Database Schema
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Database Schema"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "JDBC Approach (Standard Spring Security):" & vbCrLf & _
            "• users table: username, password, enabled" & vbCrLf & _
            "• authorities table: username, authority" & vbCrLf & _
            "• user_profiles table: additional user info" & vbCrLf & vbCrLf & _
            "JPA/Manual Approaches (Custom Schema):" & vbCrLf & _
            "• users table: id, username, email, password, role" & vbCrLf & vbCrLf & _
            "Database Features:" & vbCrLf & _
            "• H2 In-Memory Database" & vbCrLf & _
            "• H2 Console available at /h2-console" & vbCrLf & _
            "• Pre-configured test users" & vbCrLf & _
            "• SQL scripts for schema and data initialization"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 9: Security Configuration
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Security Configuration"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "Security Configuration Classes:" & vbCrLf & _
            "• InMemorySecurityConfig" & vbCrLf & _
            "• JdbcSecurityConfig" & vbCrLf & _
            "• JpaSecurityConfig" & vbCrLf & _
            "• ManualSecurityConfig" & vbCrLf & vbCrLf & _
            "Profile-based Configuration:" & vbCrLf & _
            "• Spring profiles: inmemory, jdbc, custom-jpa, manual" & vbCrLf & _
            "• application.yml configuration" & vbCrLf & vbCrLf & _
            "Security Features:" & vbCrLf & _
            "• BCrypt password encoding" & vbCrLf & _
            "• Role-based authorization (USER/ADMIN)" & vbCrLf & _
            "• Debug logging for Spring Security"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 10: Future Enhancements
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Future Enhancements"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "JWT Implementation Plan:" & vbCrLf & _
            "• Migrate to stateless authentication" & vbCrLf & _
            "• Token-based security" & vbCrLf & _
            "• JWT generation and validation" & vbCrLf & vbCrLf & _
            "Potential Improvements:" & vbCrLf & _
            "• OAuth2 integration" & vbCrLf & _
            "• Multi-factor authentication" & vbCrLf & _
            "• Rate limiting and security headers" & vbCrLf & _
            "• Production database integration" & vbCrLf & _
            "• API documentation with Swagger" & vbCrLf & vbCrLf & _
            "Documentation:" & vbCrLf & _
            "• Comprehensive README.md" & vbCrLf & _
            "• JWT_IMPLEMENTATION_PLAN.md"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 11: Key Benefits & Learning Outcomes
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Key Benefits & Learning Outcomes"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "Educational Value:" & vbCrLf & _
            "• Demonstrates evolution of Spring Security approaches" & vbCrLf & _
            "• From simple in-memory to production-ready implementations" & vbCrLf & _
            "• Comparative analysis of different authentication methods" & vbCrLf & vbCrLf & _
            "Technical Benefits:" & vbCrLf & _
            "• Clean, modular architecture" & vbCrLf & _
            "• Profile-based configuration flexibility" & vbCrLf & _
            "• Comprehensive test data and examples" & vbCrLf & _
            "• Production-ready code patterns" & vbCrLf & vbCrLf & _
            "Developer Experience:" & vbCrLf & _
            "• Well-documented with curl examples" & vbCrLf & _
            "• H2 console for database inspection" & vbCrLf & _
            "• Debug logging for troubleshooting"
    End With
    slideIndex = slideIndex + 1
    
    ' Slide 12: Conclusion
    Set pptSlide = pptPres.Slides.Add(slideIndex, 2)
    With pptSlide
        .Shapes.Title.TextFrame.TextRange.Text = "Conclusion"
        .Shapes.Placeholders(2).TextFrame.TextRange.Text = _
            "Project Summary:" & vbCrLf & _
            "• Comprehensive Spring Security demonstration" & vbCrLf & _
            "• Four distinct authentication approaches" & vbCrLf & _
            "• Production-ready code patterns and practices" & vbCrLf & _
            "• Excellent educational resource" & vbCrLf & vbCrLf & _
            "Technical Excellence:" & vbCrLf & _
            "• Clean architecture with separation of concerns" & vbCrLf & _
            "• Modern Spring Boot 3.2.0 implementation" & vbCrLf & _
            "• Comprehensive documentation and examples" & vbCrLf & vbCrLf & _
            "Ready for Extension:" & vbCrLf & _
            "• JWT implementation planned" & vbCrLf & _
            "• Scalable architecture for future enhancements" & vbCrLf & _
            "• Professional development practices"
    End With
    
    ' Format presentation
    Call FormatPresentation(pptPres)
    
    ' Clean up
    Set pptSlide = Nothing
    Set pptPres = Nothing
    Set pptApp = Nothing
    
    MsgBox "User Login System presentation created successfully!", vbInformation
End Sub

Sub FormatPresentation(pres As PowerPoint.Presentation)
    Dim slide As PowerPoint.Slide
    Dim shape As PowerPoint.Shape
    
    ' Apply consistent formatting to all slides
    For Each slide In pres.Slides
        For Each shape In slide.Shapes
            If shape.HasTextFrame Then
                With shape.TextFrame.TextRange.Font
                    .Name = "Calibri"
                    If shape.Name = "Title 1" Then
                        .Size = 36
                        .Bold = True
                        .Color.RGB = RGB(0, 0, 139) ' Dark blue
                    Else
                        .Size = 18
                        .Bold = False
                        .Color.RGB = RGB(0, 0, 0) ' Black
                    End If
                End With
            End If
        Next shape
        
        ' Set slide background
        slide.Background.Fill.ForeColor.RGB = RGB(248, 248, 255) ' Light blue background
    Next slide
End Sub