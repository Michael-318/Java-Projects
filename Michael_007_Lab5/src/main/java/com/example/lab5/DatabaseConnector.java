package com.example.lab5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseConnector {

    private static DatabaseConnector _dbConnector;
    private Connection dbConnection;
    private final String TABLE_NAME = "Students GPA";
    private PreparedStatement insertStatement;
    public ObservableList<GPA> GPAList = FXCollections.observableArrayList();

    public static DatabaseConnector getInstance() {
        if (_dbConnector == null){
            _dbConnector = new DatabaseConnector();
        }

        return _dbConnector;
    }

    private DatabaseConnector() {
        this.connectToDB();
    }

    public void connectToDB() {
        try{
            String driverType = "jdbc:oracle:thin";

            // when connecting from campus CcwSecure wifi
            String host = "@oracle1.centennialcollege.ca";

//            // when connecting from anywhere else
//            String host = "@199.212.26.208";

            int port = 1521;
            String sid = "SQLD";

            //jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD
            String hostString = driverType + ":" + host + ":" + port + ":" + sid;

            System.out.println("hostString : " + hostString);

            String username = "COMP228_F24_jig_49";
            String password = "password";

            //register the driver
            Class.forName("oracle.jdbc.OracleDriver");

            this.dbConnection =
                    DriverManager.getConnection(hostString, username, password);

            if(this.dbConnection != null){
                System.out.println("Database connection established successfully");

                this.createTable();

                String insertQuery = "INSERT INTO \"Students GPA\" VALUES(?, ?, ?, ?, ?)";

                System.out.println("insertQuery : " + insertQuery);
                this.insertStatement = this.dbConnection.prepareStatement(insertQuery);

            }else{
                System.out.println("Cannot get database connection.");
            }
        }catch (SQLException ex){
            System.out.println("Error while connecting to database : " + ex);
        }catch (Exception ex){
            System.out.println("Something went wrong : " + ex);
        }
    }



    private void createTable() {
        try {
            DatabaseMetaData databaseMetaData = this.dbConnection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, TABLE_NAME, null);

            if (resultSet.next()) {
                System.out.println("Table already exists.");
                this.getAllRecords();
            } else {
                String createTableQuery = "CREATE TABLE \"" + TABLE_NAME + "\"" +
                        " (" +
                        "Id NUMBER(5), " +
                        "GPA1 NUMBER(3, 2), " +
                        "GPA2 NUMBER(3, 2), " +
                        "GPA3 NUMBER(3, 2), " +
                        "CGPA NUMBER(3, 2) " +
                        ")";
                System.out.println("Query : " + createTableQuery);

                if (!this.dbConnection.isClosed()) {
                    Statement statement = this.dbConnection.createStatement();
                    int n = statement.executeUpdate(createTableQuery);

                    if (n >= 0) {
                        System.out.println("Table " + TABLE_NAME + " created successfully");
                    } else {
                        System.out.println("Cannot create table " + TABLE_NAME);
                    }

                    if (!statement.isClosed()) {
                        statement.close();
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error while creating table : " + ex);
        } catch (Exception ex) {
            System.out.println("Something went wrong : " + ex);
        }
    }





    public boolean insertToDB(GPA gpaToInsert) {
        try {
            if (!this.dbConnection.isClosed()) {
                this.insertStatement.setInt(1, gpaToInsert.getId());
                this.insertStatement.setDouble(2, gpaToInsert.getGpa1());
                this.insertStatement.setDouble(3, gpaToInsert.getGpa2());
                this.insertStatement.setDouble(4, gpaToInsert.getGpa3());
                this.insertStatement.setDouble(5, gpaToInsert.getCgpa());

                int n = this.insertStatement.executeUpdate();

                if (n > 0) {
                    System.out.println("Record inserted successfully. Id : " + gpaToInsert.getId());
                    this.getAllRecords();
                    return true;
                } else {
                    System.out.println("Record not inserted for gpa : " + gpaToInsert.getId());
                }
            } else {
                System.out.println("Cannot insert. Database is closed.");
            }
        } catch (SQLException ex) {
            System.out.println("Error while inserting record : " + ex);
        } catch (Exception ex) {
            System.out.println("Something went wrong : " + ex);
        }
        return false;
    }


    public void getAllRecords() {
        try {
            if (!this.dbConnection.isClosed()) {
                Statement statement = this.dbConnection.createStatement();
                String selectQuery = "SELECT * FROM \"Students GPA\"";
                ResultSet resultSet = statement.executeQuery(selectQuery);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                if (columnCount > 0) {
                    this.GPAList = FXCollections.observableArrayList();
                }

                GPA gpaFromDB;
                while (resultSet.next()) {
                    gpaFromDB = new GPA(
                            resultSet.getInt(1),
                            resultSet.getDouble(2),
                            resultSet.getDouble(3),
                            resultSet.getDouble(4),
                            resultSet.getDouble(5)
                    );

                    // Add object to array
                    this.GPAList.add(gpaFromDB);
                }

                System.out.println("GPAList : " + GPAList);
            }
        } catch (SQLException ex) {
            System.out.println("Error while retrieving data : " + ex);
        } catch (Exception ex) {
            System.out.println("Something went wrong : " + ex);
        }
    }


    public void closeConnections() {
        try{

            if(!this.insertStatement.isClosed()){
                this.insertStatement.close();
            }

            if(!this.dbConnection.isClosed()){
                this.dbConnection.close();
            }

        }catch (SQLException ex){
            System.out.println("Error while closing connection : " + ex);
        }catch (Exception ex){
            System.out.println("Something went wrong : " + ex);
        }
    }

    public boolean isIdDuplicate(int id) {
        try {
            String query = "SELECT COUNT(*) FROM \"Students GPA\" WHERE Id = ?";
            PreparedStatement statement = this.dbConnection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error while checking for duplicate ID: " + ex);
        }
        return false;
    }

}
