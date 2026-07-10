class Employee:

    company_name = "ABC Company"

    def __init__(self, name, employee_id):
        self.name = name
        self.employee_id = employee_id
        self.salaries = []

    def add_salary(self, salary):
        self.salaries.append(salary)



    # ***
    def average_salary(self):   
        return sum(self.salaries) / len(self.salaries)
    


    def max_slaary(self):
        return max(self.salaries)
    
    def annual_income(self):
        return sum(self.salaries)
    

    # ***
    def __str__(self):
        return (
            f"Employee Name : {self.name}\n"
            f"Employee ID   : {self.employee_id}\n"
            f"Average Salary : {self.average_salary():.2f}\n"
            f"Highest Salary : {self.highest_salary()}\n"
            f"Annual Income  : {self.annual_income()}"
        )
                

employees = []

name = input("Employee name: ")
id = input("Employee id :")
emp = Employee(name, id)

print("Salaries:")
for _ in range(12):
    salary = float(input())
    emp.add_salary(salary)

employees.append(emp)

for emp in employees:
    print(emp)
