# Android Applications Project

This repository contains two separate Android applications developed as part of a learning/academic project:

1. **User Management System** (MyApplication) - A simple user management application with login, profile management, and admin functionality.
2. **Personal Finance Manager** (MyApplication3) - A comprehensive finance tracking application for managing income and expenses.

## User Management System

### Overview
The User Management System is an Android application that provides basic user authentication and management functionality. The application allows users to:

- Login to the system
- Add new users
- Edit existing user profiles
- Search for users by username, email, or name
- Delete users from the system

### Features
- **Login System**: Secure login with username and password validation
- **User Management**: CRUD operations (Create, Read, Update, Delete) for user accounts
- **Data Validation**: Form validation for user input fields
- **Search Functionality**: Filter users based on search criteria
- **SQLite Database**: Local data storage for user accounts
- **Date Picker**: Calendar integration for date of birth selection

### Architecture
The application follows a simple MVC (Model-View-Controller) pattern:
- **Model**: User data structure and database operations
- **View**: XML layouts for UI components
- **Controller**: Activity classes handling user interactions

### Core Components
- `LoginAct`: Handles user authentication
- `UserHomeAct`: Main dashboard after login
- `AddViewAct`: Interface for adding new users
- `SearchAct`: Interface for searching and managing users
- `EditAct`: Interface for editing user profiles
- `User`: Model class representing user data
- `UserDao`: Data access object for user operations

## Personal Finance Manager

### Overview
The Personal Finance Manager is a more advanced application designed to help users track their personal finances. It allows users to:

- Record income and expenses
- Categorize transactions
- View financial statistics
- Analyze spending patterns
- Track daily, monthly, and yearly financial activities

### Features
- **Transaction Management**: Add, edit, and delete financial transactions
- **Categorization**: Organize transactions by custom categories
- **Financial Statistics**: View pie charts and bar graphs of financial data
- **Calendar View**: Track transactions by date with visual indicators
- **Category Management**: Create and manage transaction categories
- **Period-based Analysis**: View statistics for current month, previous month, current year, etc.
- **SQLite Database**: Local storage for transaction data

### Key Components
- **Transaction Entry**: Add transactions with amount, category, date, and notes
- **Statistical Analysis**: View income/expense breakdowns by category and time period
- **Calendar Integration**: Date-based transaction tracking with visual indicators
- **Data Visualization**: Pie charts for category distribution and bar charts for time-based trends

### Architecture
The app follows a layered architecture:
- **Model Layer**: Transaction, Category, and other data models
- **DAO Layer**: Database access objects for each entity
- **Activity Layer**: UI controllers for different screens
- **Adapter Layer**: Handles data binding to various UI components
- **Fragment Layer**: Modular UI components

### Libraries
The Personal Finance Manager utilizes several external libraries:
- MPAndroidChart: For pie charts and bar charts visualization
- RecyclerView: For efficient list display
- GridLayout: For calendar display
- SQLite: For local database operations

## Getting Started

### Prerequisites
- Android Studio 
- Android SDK (minimum API level: 21)
- Java Development Kit (JDK)

### Installation
1. Clone this repository
2. Open the project in Android Studio
3. Build and run the application on an emulator or physical device

## Project Structure
The codebase is structured as follows:

```
MyApplication/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/map/hung/myapplication/
│           │   ├── activity/            # Activity classes
│           │   ├── dao/                 # Data access objects
│           │   ├── model/               # Data models
│           │   └── preference/          # Preference management
│           └── res/                     # Resources (layouts, drawables, etc.)
│ 
MyApplication3/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/map/hung/myapplication/
│           │   ├── activity/            # Activity classes
│           │   ├── adapter/             # RecyclerView adapters
│           │   ├── dao/                 # Data access objects
│           │   ├── fragment/            # UI fragments
│           │   ├── model/               # Data models
│           │   └── ui/                  # UI components
│           └── res/                     # Resources (layouts, drawables, etc.)
```

## Database Schema

### User Management System
- **tbUser**: Stores user information (username, password, fullname, email, tel, dob, gender)

### Personal Finance Manager
- **tblCategory**: Stores categories for transactions
- **tblInOut**: Defines income or expense types
- **tblCatInOut**: Links categories to income/expense types
- **tblTransaction**: Stores transaction data
- **tblDailyStat**: Stores daily financial statistics

## Future Enhancements
- User authentication integration between both applications
- Cloud synchronization for financial data
- Budget planning and goal setting
- Receipt scanning and automatic transaction entry
- Multi-currency support
- Export reports to PDF/Excel

## Contributors
- Nguyen Van Hung - Main Developer

## License
This project is intended for educational purposes and is not licensed for commercial use.
