USE DATABASE unittesting;

CREATE STAGE IF NOT EXISTS UNITTESTING.PUBLIC.MYSTAGE ;

-- Create Employee table
CREATE OR REPLACE TABLE Employee (
    EmployeeID INT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    Department VARCHAR(50),
    Salary DECIMAL(10,2)
);

-- Insert sample data
INSERT INTO Employee (EmployeeID, FirstName, LastName, Department, Salary)
VALUES 
    (1, 'John', 'Doe', 'HR', 55000.00),
    (2, 'Jane', 'Smith', 'Marketing', 60000.00),
    (3, 'Michael', 'Johnson', 'Finance', 65000.00),
    (4, 'Emily', 'Brown', 'IT', 70000.00);

-- Create Product table
CREATE OR REPLACE TABLE Product (
    ProductID INT,
    ProductName VARCHAR(100),
    Category VARCHAR(50),
    Price DECIMAL(10,2)
);

-- Insert sample data
INSERT INTO Product (ProductID, ProductName, Category, Price)
VALUES 
    (101, 'Laptop', 'Electronics', 1200.00),
    (102, 'Smartphone', 'Electronics', 800.00),
    (103, 'Headphones', 'Electronics', 100.00),
    (104, 'T-shirt', 'Clothing', 20.00),
    (105, 'Jeans', 'Clothing', 50.00);

-- Create Order table
CREATE OR REPLACE TABLE "ORDER" (
    OrderID INT,
    ProductID INT,
    Quantity INT,
    OrderDate DATE
);

-- Insert sample data
INSERT INTO "ORDER" (OrderID, ProductID, Quantity, OrderDate)
VALUES 
    (1, 101, 2, '2024-03-20'),
    (2, 102, 1, '2024-03-21'),
    (3, 104, 3, '2024-03-22'),
    (4, 105, 2, '2024-03-22');
