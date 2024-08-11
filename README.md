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
  - `BaseCurrency`: Represents the base currency used for exchange rates (set in `config.properties`).
  - `Currency`: Represents currency exchange rate data.
  - `FXTransaction`: Holds individual transaction records.
  - `User`: Represents user wallet data.

- **Exceptions:**
  - `ConfigSettingException`: Raised when settings cannot be accessed from `config.properties`.
  - `DataSessionException`: Raised when accessing a `DataSession` instance before initialisation.
  - `DataSourceException`: Raised when source files are missing data required for the program to function.
  - `InsufficientFundsException`: Raised when a user has insufficient funds to perform a transaction.
  - `InvalidCurrencyException`: Raised when a currency code in a transaction is invalid.
  - `UserNotFoundException`: Raised when a user involved in a transaction cannot be found.

- **Utility Classes:**
  - `ConfigLoader`: Loads config settings from `config.properties`.
  - `Converter`: Handles conversions between two currencies.
  - `LogHandler`: Manages log statements across the program.
  - `TransactionProcessor`: Validates and processes transactions.

## Installation

To clone this repo, open the terminal on your machine and run the following command from within the desired workspace:

```console
$ git clone git@github.com:essteer/fx-app
```

## Documentation

Refer to the Javadocs for detailed information on the classes and methods provided by the application.
