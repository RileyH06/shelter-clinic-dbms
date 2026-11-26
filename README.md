# shelter-clinic-dbms
Group 10 project
## authors
Riley Herritt, Kelsey Wright

## Run the JavaFX frontend with a local MySQL database

You need a JDK, Maven, and a running MySQL server. The app connects to `localhost:3306` and uses the `shelterclinic_schema` database.

1. In MySQL Workbench, connect to `localhost` on port `3306` using an existing MySQL account. Use an account that has access to `shelterclinic_schema` when running the app.
2. If you haven't set up the database yet, run this in Workbench, then open and execute [shelterclinic_schema.sql](shelterclinic_schema.sql) to create the tables.

   ```sql
   CREATE DATABASE IF NOT EXISTS shelterclinic_schema;
   ```

3. Open a PowerShell terminal in VS Code at the repository root. Set `DB_USER` to your MySQL username and enter your MySQL password at the prompt. Use the values you set when making the database connection.

   ```powershell
   $env:DB_USER = "your_mysql_username"
   $dbPassword = Read-Host "MySQL password" -AsSecureString
   $env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $dbPassword).Password
   ```

4. In the same terminal, start the app.

   ```powershell
   cd shelterclinic
   mvn javafx:run
   ```

Look for `Connected to MySQL: shelterclinic_schema` in the terminal. The PowerShell variables last for that terminal session, so set them again if you open a new terminal.
