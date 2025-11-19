# TrendTaster Backend - Documentation Index

This directory contains comprehensive documentation for the TrendTaster Backend project. Below is a guide to each document and when to use it.

## Documentation Files

### 1. CLAUDE.md (Primary Reference)
**File Size:** 20 KB | **Length:** 508 lines | **Read Time:** 15-20 minutes

**Purpose:** Comprehensive project documentation for Claude Code and future developers
**Use This When:** 
- You need complete technical details about the codebase
- You're implementing new features
- You want to understand architecture patterns
- You're setting up the development environment
- You need security considerations

**Contains:**
- Full project overview and tech stack
- Detailed directory structure and file purposes
- Build, test, and run commands
- Architecture patterns with data flow examples
- Complete API endpoints reference
- Security configuration details
- Code style conventions and patterns
- Known issues and gaps
- Development workflow instructions
- Future improvement suggestions

---

### 2. CODEBASE_ANALYSIS_SUMMARY.md (Quick Reference)
**File Size:** 11 KB | **Length:** 300+ lines | **Read Time:** 10 minutes

**Purpose:** High-level overview and quick reference guide
**Use This When:**
- You're new to the project and need a quick understanding
- You want a cheat sheet for common tasks
- You need to recall essential commands
- You want to understand why architectural decisions were made
- You're looking for a quick architecture diagram

**Contains:**
- Project quick facts
- Technology stack overview
- Essential commands table
- Layered architecture diagram
- Core design concepts
- Important configuration settings
- API endpoint categories
- Code style patterns with examples
- Notable design decisions
- Known gaps and TODOs
- Environment setup instructions
- Quick cheat sheet

---

### 3. README.md (Original Documentation)
**File Size:** 3.6 KB | **Purpose:** Original Korean-language project overview
**Use This When:**
- You need Korean language documentation
- You want to see the original project description
- You need database schema details from the original README

---

### 4. FILES_UPDATED.md (Migration Record)
**File Size:** 12 KB | **Purpose:** Records package refactoring from `newproduct.backend` to `trendTaster`
**Use This When:**
- You need to understand package migration history
- You're searching for where specific classes moved
- You want to see the complete list of 30+ files that were refactored

---

## Quick Navigation by Task

### I'm New to This Project
1. Start: Read **CODEBASE_ANALYSIS_SUMMARY.md** (5-10 min overview)
2. Then: Skim **CLAUDE.md** sections relevant to your role
3. Reference: Keep "Quick Cheat Sheet" from CODEBASE_ANALYSIS_SUMMARY handy

### I Need to Build and Run the Project
1. Go to: **CLAUDE.md** → "Build, Test & Run Commands" section
2. Or: **CODEBASE_ANALYSIS_SUMMARY.md** → "Essential Commands" table
3. Setup: **CLAUDE.md** → "Development Workflow" → "Setting Up Local Environment"

### I'm Writing New Code
1. Reference: **CLAUDE.md** → "Code Style & Conventions" section
2. Patterns: **CODEBASE_ANALYSIS_SUMMARY.md** → "Code Style Patterns Used"
3. Details: **CLAUDE.md** → "Architecture & Design Patterns"

### I Need to Understand How Something Works
1. Architecture: **CODEBASE_ANALYSIS_SUMMARY.md** → "Architecture Overview"
2. Data flows: **CLAUDE.md** → "Data Flow Examples" subsection
3. API details: **CLAUDE.md** → "API Endpoints Summary"

### I'm Implementing a New Feature
1. Study: **CLAUDE.md** → "Architecture & Design Patterns"
2. Check: **CLAUDE.md** → "Code Style & Conventions"
3. Patterns: **CODEBASE_ANALYSIS_SUMMARY.md** → "Code Style Patterns Used"
4. Security: **CLAUDE.md** → "Security Considerations"
5. Add: Follow examples from existing similar features

### I Need to Debug an Issue
1. Checks: **CLAUDE.md** → "Important Notes & Known Issues"
2. Logs: Look for DEBUG level logs per application.yml settings
3. Database: Check PostgreSQL connection and schema
4. Security: Verify JWT token is valid and not expired

### I Need to Deploy or Configure
1. Environment: **CLAUDE.md** → "Configuration" section
2. Variables: "Environment Variables Required" subsection
3. Security: **CLAUDE.md** → "Security Considerations"
4. Production: See "Future Improvements" for production recommendations

## File Locations

```
/Users/moonsung/workspace/trendTasterBack/
├── CLAUDE.md                           ← START HERE for comprehensive reference
├── CODEBASE_ANALYSIS_SUMMARY.md       ← START HERE for quick overview
├── DOCUMENTATION_INDEX.md              ← This file
├── README.md                           ← Original Korean documentation
├── FILES_UPDATED.md                    ← Package migration history
├── build.gradle                        ← Build configuration
├── settings.gradle                     ← Gradle settings
├── gradlew                             ← Gradle wrapper script
│
├── src/
│   ├── main/
│   │   ├── java/com/trendTaster/
│   │   │   ├── TrendTasterBackendApplication.java
│   │   │   ├── config/                 ← Spring configuration
│   │   │   ├── controller/             ← REST endpoints (6 controllers)
│   │   │   ├── service/                ← Business logic (3 services)
│   │   │   ├── repository/             ← Data access (4 repositories)
│   │   │   ├── domain/                 ← JPA entities (6 entities)
│   │   │   ├── dto/                    ← Data transfer objects (5 files)
│   │   │   ├── security/               ← JWT & auth (2 files)
│   │   │   └── exception/              ← Exception handler (1 file)
│   │   └── resources/
│   │       ├── application.yml         ← Spring configuration
│   │       └── static/index.html       ← Static files
│   └── test/                           ← (Currently empty, needs tests)
│
├── build/                              ← Build output
└── .gradle/                            ← Gradle cache
```

## Documentation Coverage

| Aspect | Where to Find | File |
|--------|---------------|------|
| Project Overview | First section | CLAUDE.md or CODEBASE_ANALYSIS_SUMMARY.md |
| Tech Stack | Tech Stack section | Both files |
| Directory Structure | Directory Structure section | CLAUDE.md (detailed) |
| Build Commands | "Build, Test & Run Commands" | Both files |
| API Endpoints | "API Endpoints Summary" | CLAUDE.md (complete) |
| Architecture | "Architecture & Design Patterns" | Both files (CODEBASE has diagram) |
| Security | "Security Considerations" | CLAUDE.md (detailed) |
| Code Conventions | "Code Style & Conventions" | CLAUDE.md (detailed) |
| Known Issues | "Important Notes & Known Issues" | CLAUDE.md |
| Configuration | "Configuration" section | CLAUDE.md |
| Database Schema | "Database Schema Highlights" | CLAUDE.md |
| Development Setup | "Development Workflow" | CLAUDE.md |
| Pattern Examples | "Code Style Patterns Used" | CODEBASE_ANALYSIS_SUMMARY.md |
| Quick Reference | Quick Reference section | CODEBASE_ANALYSIS_SUMMARY.md |

## Key Statistics

| Metric | Value |
|--------|-------|
| Total Documentation | 45+ KB across 4 files |
| Total Lines | 1,100+ lines of documentation |
| Java Files | 30 files across 9 packages |
| Controllers | 6 (Auth, Product, Store, Admin, Upload, Health) |
| Services | 3 (Auth, Product, Store) |
| Repositories | 4 (User, Product, Store, UploadToken) |
| Entities | 6 (User, Product, ProductImage, Store, UploadToken, BaseEntity) |
| DTOs | 5 classes with inner static classes |
| API Endpoints | 20+ documented endpoints |
| Spring Boot Version | 3.3.5 |
| Java Version | 21 |
| Database | PostgreSQL |

## How to Update Documentation

### When Adding a New Feature
1. Update **CLAUDE.md** → "API Endpoints Summary" if adding endpoints
2. Update **CLAUDE.md** → "Directory Structure" if adding new packages
3. Update **CODEBASE_ANALYSIS_SUMMARY.md** → relevant sections

### When Changing Configuration
1. Update **CLAUDE.md** → "Configuration" section
2. Update **CLAUDE.md** → "Security Considerations" if security-related
3. Update environment variables in both files if needed

### When Discovering Issues or Gaps
1. Add to **CLAUDE.md** → "Important Notes & Known Issues"
2. Add to **CODEBASE_ANALYSIS_SUMMARY.md** → "Known Gaps & TODOs"

## Documentation Standards

All documentation files follow these standards:
- Markdown format (.md)
- Clear section headings with hierarchy
- Code examples in proper syntax blocks
- File paths as absolute paths
- Tables for organized information
- Bullet points for lists
- Links to relevant sections within documents
- Dates for last update tracking

## Getting Help

### For Technical Questions
1. Check relevant documentation section first
2. Review code comments in source files
3. Check test files for usage examples (when tests are added)
4. Refer to Spring Boot official documentation

### For Architecture Questions
1. Read "Architecture & Design Patterns" section
2. Review entity relationships in database schema section
3. Study data flow examples

### For Development Setup Issues
1. Follow "Development Workflow" section step-by-step
2. Verify all environment variables are set
3. Check PostgreSQL is running and database exists
4. Review logs for detailed error messages

## Document Versioning

| Document | Last Updated | Version | Status |
|----------|--------------|---------|--------|
| CLAUDE.md | 2025-11-19 | 1.0 | Current |
| CODEBASE_ANALYSIS_SUMMARY.md | 2025-11-19 | 1.0 | Current |
| DOCUMENTATION_INDEX.md | 2025-11-19 | 1.0 | Current |
| README.md | 2025-11-16 | Original | Reference |
| FILES_UPDATED.md | 2025-11-18 | Original | Historical |

---

## Summary

This documentation package provides complete coverage of the TrendTaster Backend project:

- **CLAUDE.md** (20 KB) - Comprehensive technical reference for developers and architects
- **CODEBASE_ANALYSIS_SUMMARY.md** (11 KB) - Quick overview and cheat sheet
- **DOCUMENTATION_INDEX.md** (this file) - Navigation guide
- **README.md** - Original Korean documentation
- **FILES_UPDATED.md** - Migration history

Start with the summary for quick orientation, then use the comprehensive guide for detailed work. Both documents are designed to help Claude Code instances (and human developers) quickly understand and work with this codebase.

**Happy coding!**

---

Generated: 2025-11-19  
Project: TrendTaster Backend (오늘의신상)  
Status: Active Development
