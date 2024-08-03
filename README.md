# FX App

This app provides functionality for converting between different currencies. It supports transactions that involve conversions, transaction data validation, and user wallet maintenance. The app ensures that users have sufficient funds before processing transactions and logs all relevant operations.

## Features

- **Currency Conversion:** Convert amounts between different currencies using USD as an intermediary if necessary.
- **Transaction Processing:** Validate and process individual transactions, ensuring users have sufficient funds.
- **User Wallet Management:** Maintain and update user wallets with various currencies.
- **Logging:** Comprehensive logging of all operations for tracking and debugging.

## Key Components

- **Data Classes**
  - `DataIO`: Handles the loading and saving of data to and from files.
  - `DataSession`: Holds `Currency` and `User` data in Map form for access across the session.
  - `DataValidator`: Performs validation checks at different stages to ensure data is present and correct.

- **Entities:**
  - `Currency`: Represents currency exchange rate data.
  - `FXTransaction`: Holds individual transaction records.
  - `User`: Represents user wallet data.

- **Exceptions:**
  - `DataSessionException`: Raised when accessing a `DataSession` instance before initialisation.
  - `InsufficientFundsException`: Raised when a user has insufficient funds to perform a transaction.
  - `InvalidCurrencyException`: Raised when a currency code in a transaction is invalid.
  - `UserNotFoundException`: Raised when a user involved in a transaction cannot be found.

- **Utility Classes:**
  - `Converter`: Handles the conversion between two currencies.
  - `TransactionProcessor`: Validates and processes transactions.

## Installation

To clone this repo, open the terminal on your machine and run the following command from within the desired workspace:

```console
$ git clone https://git.fdmgroup.com/elliott.steer/assessment-3-elliott.git
```

Import the cloned repo into the Eclipse IDE as a Maven project.

## Documentation

For detailed information on the classes and methods provided by the application, please refer to the Javadocs documentation. The Javadocs provide comprehensive details on the functionality and use of each component within the app.