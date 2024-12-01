To run the program you need to connect to MySQL database, have the tables attached bellow and have at least the data attached bellow

SQL command for creating and adding tables and their contents:
{
createing tables:
  CREATE TABLE Warehouses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    capacity INT
  );

  CREATE TABLE MaterialTypes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    icon VARCHAR(255),
    capacity INT
  );

  CREATE TABLE Materials (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type_id INT,
    quantity INT,
    warehouse_id INT,
    capacity INT,
    FOREIGN KEY (type_id) REFERENCES MaterialTypes(id),
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses(id)
  );
adding inntial table contents:
  INSERT INTO MaterialTypes (id, name, description, icon, capacity) VALUES
  (1, 'Iron Ore', 'Made from substance using a synthesizer', 'src/main/resources/icons/IronOre.webp', 3000);

  INSERT INTO MaterialTypes (name, description, icon, capacity) VALUES
  ('Carbon', 'Made from substance using a synthesizer', 'src/main/resources/icons/Carbon.webp', 2700),
  ('Copper Ore', 'Made from substance using a synthesizer', 'src/main/resources/icons/CopperOre.webp', 3500),
  ('Silicon', 'Made from substance using a synthesizer', 'src/main/resources/icons/Silicon.webp', 2500);

  INSERT INTO Warehouses (id, name, capacity) VALUES
  (1, 'Warehouse 1', 25000);

  INSERT INTO Warehouses (name, capacity) VALUES
  ('Warehouse 2', 20000);
}

![Screenshot from 2024-04-21 18-47-11](https://github.com/Henze1/Engineer_technical_interview_test0001/assets/119739497/1e6502e9-80bd-4f08-a594-9196aa6d5155)

![Screenshot from 2024-04-21 18-31-20](https://github.com/Henze1/Engineer_technical_interview_test0001/assets/119739497/2c184f2d-2732-43d6-8f35-f2a2de0568ef)
![Screenshot from 2024-04-21 18-31-01](https://github.com/Henze1/Engineer_technical_interview_test0001/assets/119739497/72aba82c-afff-445b-afc1-1cb764d28ceb)
