package com.example.ISP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.awt.Color;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.control.Button;
//import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.vandeseer.easytable.OverflowOnSamePageTableDrawer;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;


public class Reports_Controller extends Parent_Controller  implements Initializable {

    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d-MM-uuuu");
    String currentDate = date.format(formatters);
    File initialSavingDirectory = new File(System.getProperty("user.home") + "\\Documents");

    String areaCode;
    String startingYear;
    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private ComboBox<String> areaCodeComboBox;

    @FXML
    private Text monthText = new Text();

    @FXML
    private TextField userNameField;
    @FXML
    private Button unpaidUsersButton;
    @FXML
    private Button MELButton;
    @FXML
    private Button defalutersButton;
    @FXML
    private Button activeUsersButton;
    @FXML
    private Button inActiveUsersButton;

    private Table headingTable(String heading) {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(530); //Heading

        TextCell headingCell = TextCell.builder()
                .text(heading)
                .backgroundColor(Color.WHITE)
                .borderColor(Color.WHITE)
                .borderColorBottom(Color.black)
                .textColor(Color.black)
                .font(PDType1Font.HELVETICA_BOLD)
                .fontSize(16)
                .paddingTop(5)
                .paddingLeft(230)
                .borderWidth(1F)
                .build();

        tableBuilder.addRow(
                Row.builder()
                        .add(headingCell)
                        .build());

        return tableBuilder.build();

    }

    public void getSelectedAreaCode(ActionEvent e){
        areaCode = areaCodeComboBox.getValue();
    }

    public void getSelectedYear(ActionEvent e){
        startingYear = yearComboBox.getValue();
    }

    public Table createPaymentHistoryTable(){

        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(88)
                    .addColumnOfWidth(88)
                    .addColumnOfWidth(88);



            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("Sr. #"))
                            .add(createHeaderCell("Date"))
                            .add(createHeaderCell("Received"))
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select paymentID, paymentDate, amountRecieved from tblPayment where areaCode = ? and userID = ? and Year(paymentDate) >= ?  order by paymentDate"
            );

            statement.setString(1,areaCode);
            statement.setInt(2, Integer.parseInt(userNameField.getText()));
            statement.setString(3,startingYear);

            ResultSet rs = statement.executeQuery();


            while (rs.next()) {
                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//Serial Number
                                        .text(rs.getString(1))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//User ID
                                        .text(rs.getString(2))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Name
                                        .text(rs.getString(3))
                                        .borderWidth(1F)
                                        .build())
                                .build());
            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
            return null;
        }


    }

    public void loadAreas() throws SQLException {
        ObservableList<String> areasList = FXCollections.observableArrayList();
        Connection connection = makeConnection();
        String query = "SELECT areaCode FROM tblArea";
        Statement statement = connection.createStatement();
        ResultSet areaIDsResult = statement.executeQuery(query);
        while (areaIDsResult.next()) {
            areasList.add(areaIDsResult.getString("areaCode"));
        }
        areaCodeComboBox.setItems(areasList);
    }

    public void loadYears() throws SQLException {
        ObservableList<String> yearList = FXCollections.observableArrayList();
        Connection connection = makeConnection();
        String query = "select DISTINCT Year(paymentDate) from tblPayment";
        Statement statement = connection.createStatement();
        ResultSet areaIDsResult = statement.executeQuery(query);
        while (areaIDsResult.next()) {
            yearList.add(areaIDsResult.getString(1));
        }
        yearComboBox.setItems(yearList);
    }


    //Kindda Generic Function
    private void drawMultipageTableOn(PDDocument document, Table table) {
        try {

            RepeatedHeaderTableDrawer.builder()
                    .table(table)
                    .startX(30f)
                    .startY(60f)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);
        } catch (IOException e) {
            this.alert("Error", "Something went wrong with incoming data stream", Alert.AlertType.ERROR);
        }


    }

    public void createTableWithOverflowOnSamePage(PDDocument document, Table table) throws IOException {

        try {

            OverflowOnSamePageTableDrawer.builder()
                    .table(table)
                    .startX(25f)
                    .lanesPerPage(2)
                    .spaceInBetween(10)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        } catch (IOException e) {
            this.alert("Error", "Something went wrong with incoming data stream", Alert.AlertType.ERROR);
        }

    }

    //will work for all headers
    private TextCell createHeaderCell(String text) {
        return TextCell.builder()
                .text(text)
                .backgroundColor(Color.WHITE)
                .textColor(Color.black)
                .font(PDType1Font.HELVETICA_BOLD)
                .paddingTop(5)
                .borderWidth(1F)
                .build();
    }

    private Table createUnpaidsTable() {
        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(40) //serial no.
                    .addColumnOfWidth(40) //ID
                    .addColumnOfWidth(145) //Name
                    .addColumnOfWidth(75)//balance
                    .addColumnOfWidth(225);//info

            TextCell infoHeaderCell = TextCell.builder()
                    .text("INFO. ")
                    .backgroundColor(Color.WHITE)
                    .textColor(Color.black)
                    .font(PDType1Font.HELVETICA_BOLD)
                    .paddingTop(5)
                    .paddingLeft(90)
                    .borderWidth(1F)
                    .build();

            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("Sr. #"))
                            .add(createHeaderCell("ID"))
                            .add(createHeaderCell("NAME"))
                            .add(createHeaderCell("Amount"))
                            .add(infoHeaderCell)
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select areaCode, userID, fName, lName, balance From tblUser Where paymentStatus = 'UnPaid' and connectivityStatus = 'Active' and packageID <> 41 " +
                            " Order By areaCode, userID "
            );
            ResultSet rs = statement.executeQuery();
            int userCounter = 1;

            while (rs.next()) {
                String areaCode = rs.getString(1);
                String userID = rs.getString(2);

                Connection connection1 = makeConnection();
                PreparedStatement s = connection1.prepareStatement(
                        "select * from tblPayment where areaCode = ? and userID = ? and month(paymentDate) = month(GETDATE()) and year(paymentDate) = year(GETDATE())"
                );

                s.setString(1,areaCode);

                s.setInt(2,Integer.parseInt(userID));
                ResultSet r = s.executeQuery();
                if(r.next()){
                    tableBuilder.addRow(
                            Row.builder()
                                    .add(TextCell.builder()//Serial Number
                                            .text(String.valueOf(userCounter))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//User ID
                                            .text( areaCode+userID+"*" )
                                            .font(PDType1Font.HELVETICA_BOLD)
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Name
                                            .text(rs.getString(3) + " " + rs.getString(4))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Balance
                                            .text(rs.getString(5))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Info
                                            .text(" ")
                                            .borderWidth(1F)
                                            .build())
                                    .build());
                }
                else{
                    tableBuilder.addRow(
                            Row.builder()
                                    .add(TextCell.builder()//Serial Number
                                            .text(String.valueOf(userCounter))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//User ID
                                            .text( areaCode+userID )
                                            .font(PDType1Font.HELVETICA_BOLD)
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Name
                                            .text(rs.getString(3) + " " + rs.getString(4))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Balance
                                            .text(rs.getString(5))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Info
                                            .text(" ")
                                            .borderWidth(1F)
                                            .build())
                                    .build());
                }

                userCounter++;
            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
            return null;
        }

    }

    private Table createMELTable() {
        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(50)//ID  1
                    .addColumnOfWidth(95) //SERIAL NO. 2
                    .addColumnOfWidth(65) //RECEIVED 3
                    .addColumnOfWidth(60);//BALANCE 4


            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("ID"))
                            .add(createHeaderCell("Serial Number"))
                            .add(createHeaderCell("Received"))
                            .add(createHeaderCell("Balance"))
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select streetID,name from tblStreet order by areaCode "
            );
            ResultSet rs = statement.executeQuery();
            int currentStreetID = 0;
            String currentStreetName = null;
            while (rs.next()) { //outer while loop for getting streets


                currentStreetID = rs.getInt(1);
                currentStreetName = rs.getString(2);

                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//ID
                                        .text(currentStreetName)
                                        .font(PDType1Font.HELVETICA_BOLD)
                                        .colSpan(4)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                        .borderWidth(2F)
                                        .build())
                                .build());

                PreparedStatement statement1 = connection.prepareStatement(
                        "Select areaCode, userID, connectivityStatus, balance, packageID from tblUser where streetID = ? order by areaCode,userID "
                );
                statement1.setInt(1, currentStreetID);

                ResultSet resultSet = statement1.executeQuery();
                while (resultSet.next()) { //inner while for getting users in street
                    String areaCode = resultSet.getString("areaCode");
                    int userID = resultSet.getInt("userID");
                    String connectivityStatus = resultSet.getString("connectivityStatus");
                    int balance = resultSet.getInt("balance");
                    int packageID = resultSet.getInt("packageID");
                    String srNoString = " ";
                    String strBalance = "";
                    String strReceived = " ";
                    if (connectivityStatus.equals("InActive")) {
                        srNoString = "D.C";
                    }
                    else if(areaCode.equals("D") && (userID == 18 || userID == 19 || userID == 20)){
                        srNoString = "Saleem";
                    }
                    else if (packageID == 41){
                        srNoString = "F";
                    }


                    else if (balance < 0 ){
                        PreparedStatement st = connection.prepareStatement(
                                "select monthlyFee from tblPackage where packageID = ? "
                        );
                        st.setInt(1,packageID);
                        ResultSet f = st.executeQuery();
                        f.next();
                        int fee = f.getInt(1);

                        int tbal = balance * (-1);

                        int monthsLeft = tbal/fee;

                        LocalDate futureDate = LocalDate.now().plusMonths(monthsLeft);

                        srNoString = "Till " + futureDate.getMonthValue() +"/"+ futureDate.getYear() ;

                    }

//"select paymentID, amountRecieved from tblPayment  " +
//                                        " where month(paymentDate) = month(GETDATE()) and year(paymentDate) = year(GETDATE()) and userID = ? and areaCode = ? "
                    else if(connectivityStatus.equals("Active")){
                        PreparedStatement st1 = connection.prepareStatement(
                                "select paymentID, amountRecieved from tblPayment  " +
                                        " where month(paymentDate) = month(GETDATE()) and year(paymentDate) = year(GETDATE()) and userID = ? and areaCode = ? "
                        );
                        st1.setInt(1,userID);
                        st1.setString(2,areaCode);
                        ResultSet resultSet1 = st1.executeQuery();

                        if(resultSet1.next()){
                            String serialNumber = resultSet1.getString("paymentID");
                            int amountReceived = resultSet1.getInt("amountRecieved");

                            srNoString = serialNumber;
                            if(balance != 0){
                                strBalance = String.valueOf(balance);
                            }

                            strReceived = String.valueOf(amountReceived);
                        }
                        else{
                            if(balance == 0){
                                LocalDate futureDate = LocalDate.now();
                                srNoString = "Till " + futureDate.getMonthValue() +"/"+ futureDate.getYear() ;
                            }
                            else{
                                PreparedStatement st = connection.prepareStatement(
                                        "select monthlyFee from tblPackage where packageID = ? "
                                );
                                st.setInt(1,packageID);
                                ResultSet f = st.executeQuery();
                                f.next();
                                int fee = f.getInt(1);

                                int previousBalance = balance - fee;

                                if(previousBalance > 0){
                                    strBalance = String.valueOf(previousBalance);
                                }
                            }

                        }


                    }



                    tableBuilder.addRow(
                            Row.builder()
                                    .add(TextCell.builder()//ID
                                            .text(resultSet.getString("areaCode") + resultSet.getString("userID"))
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Serial No.
                                            .text(srNoString)
                                            .fontSize(10)
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                            .borderWidth(1F)
                                            .build())
                                    .add(TextCell.builder()//Received
                                            .text(strReceived)
                                            .fontSize(10)
                                            .borderWidth(1F)
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                            .build())
                                    .add(TextCell.builder()//Balance
                                            .text(strBalance)
                                            .fontSize(10)
                                            .borderWidth(1F)
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                            .build())
                                    .build());


                }
                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//ID
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Serial No.
                                        .text(" ")
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Received
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Balance
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .build());

            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong while building table", Alert.AlertType.ERROR);
            return null;
        }


    }

    private Table createActivesTable() {
        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(40) //serial no.
                    .addColumnOfWidth(40) //ID
                    .addColumnOfWidth(145) //Name
                    .addColumnOfWidth(75)//balance
                    .addColumnOfWidth(225);//info

            TextCell infoHeaderCell = TextCell.builder()
                    .text("INFO. ")
                    .backgroundColor(Color.WHITE)
                    .textColor(Color.black)
                    .font(PDType1Font.HELVETICA_BOLD)
                    .paddingTop(5)
                    .paddingLeft(90)
                    .borderWidth(1F)
                    .build();

            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("Sr. #"))
                            .add(createHeaderCell("ID"))
                            .add(createHeaderCell("NAME"))
                            .add(createHeaderCell("BALANCE"))
                            .add(infoHeaderCell)
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select areaCode, userID, fName, lName, balance From tblUser Where connectivityStatus = 'Active' " +
                            " Order By areaCode "
            );
            ResultSet rs = statement.executeQuery();
            int userCounter = 1;

            while (rs.next()) {
                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//Serial Number
                                        .text(String.valueOf(userCounter))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//User ID
                                        .text(rs.getString(1) + rs.getString(2)).font(PDType1Font.HELVETICA_BOLD)
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Name
                                        .text(rs.getString(3) + " " + rs.getString(4))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Balance
                                        .text(rs.getString(5))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Info
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .build());
                userCounter++;
            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
            return null;
        }
    }

    private Table createInActivesTable() {
        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(40) //serial no.
                    .addColumnOfWidth(40) //ID
                    .addColumnOfWidth(145) //Name
                    .addColumnOfWidth(75)//balance
                    .addColumnOfWidth(225);//info

            TextCell infoHeaderCell = TextCell.builder()
                    .text("INFO. ")
                    .backgroundColor(Color.WHITE)
                    .textColor(Color.black)
                    .font(PDType1Font.HELVETICA_BOLD)
                    .paddingTop(5)
                    .paddingLeft(90)
                    .borderWidth(1F)
                    .build();

            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("Sr. #"))
                            .add(createHeaderCell("ID"))
                            .add(createHeaderCell("NAME"))
                            .add(createHeaderCell("BALANCE"))
                            .add(infoHeaderCell)
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select areaCode, userID, fName, lName, balance From tblUser Where connectivityStatus = 'InActive' " +
                            " Order By areaCode "
            );
            ResultSet rs = statement.executeQuery();
            int userCounter = 1;

            while (rs.next()) {
                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//Serial Number
                                        .text(String.valueOf(userCounter))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//User ID
                                        .text(rs.getString(1) + rs.getString(2)).font(PDType1Font.HELVETICA_BOLD)
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Name
                                        .text(rs.getString(3) + " " + rs.getString(4))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Balance
                                        .text(rs.getString(5))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Info
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .build());
                userCounter++;
            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
            return null;
        }
    }

    private Table createDefalutersTable() {
        try {
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnOfWidth(40) //serial no.
                    .addColumnOfWidth(40) //ID
                    .addColumnOfWidth(145) //Name
                    .addColumnOfWidth(75)//balance
                    .addColumnOfWidth(225);//info

            TextCell infoHeaderCell = TextCell.builder()
                    .text("INFO. ")
                    .backgroundColor(Color.WHITE)
                    .textColor(Color.black)
                    .font(PDType1Font.HELVETICA_BOLD)
                    .paddingTop(5)
                    .paddingLeft(90)
                    .borderWidth(1F)
                    .build();

            tableBuilder.addRow(
                    Row.builder()
                            .add(createHeaderCell("Sr. #"))
                            .add(createHeaderCell("ID"))
                            .add(createHeaderCell("NAME"))
                            .add(createHeaderCell("Amount"))
                            .add(infoHeaderCell)
                            .build());

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Select areaCode, userID, fName, lName, balance From tblUser u where u.balance >=  ((select p.monthlyFee from tblPackage p where p.packageID = u.packageID) * 2) and u.packageID <> 41 " +
                            " Order By areaCode "
            );
            ResultSet rs = statement.executeQuery();
            int userCounter = 1;

            while (rs.next()) {
                tableBuilder.addRow(
                        Row.builder()
                                .add(TextCell.builder()//Serial Number
                                        .text(String.valueOf(userCounter))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//User ID
                                        .text(rs.getString(1) + rs.getString(2)).font(PDType1Font.HELVETICA_BOLD)
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Name
                                        .text(rs.getString(3) + " " + rs.getString(4))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Balance
                                        .text(rs.getString(5))
                                        .borderWidth(1F)
                                        .build())
                                .add(TextCell.builder()//Info
                                        .text(" ")
                                        .borderWidth(1F)
                                        .build())
                                .build());
                userCounter++;
            }

            return tableBuilder.build();
        } catch (SQLException sqlException) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
            return null;
        }
    }

    private File getSavingLocation(String title, String fileName) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title);
            fileChooser.setInitialFileName(fileName);
            fileChooser.setInitialDirectory(initialSavingDirectory);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File", "*.pdf"));
            File file = fileChooser.showSaveDialog(unpaidUsersButton.getScene().getWindow());
            if (file != null) {
                initialSavingDirectory = file.getParentFile();
            }
            return file;
        } catch (Exception e) {
            System.out.println(e.getCause());
            e.printStackTrace();
            alert("File Saving Error", "Something went Wrong", Alert.AlertType.ERROR);
            return null;
        }

    }

    private void addPDFHeading(PDDocument document, String heading) {
        try {
            PDPage firstPage = document.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, true, false);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(30, firstPage.getMediaBox().getHeight() - 30);
            contentStream.showText(heading);
            contentStream.endText();
            contentStream.close();
        } catch (Exception e) {
            this.alert("Error", "Something went wrong while adding heading to PDF.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void PDFUnpaidUsers(ActionEvent event) {

        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document, createUnpaidsTable());
            String type = "Unpaid Users";
            String pdfHeading = type + " List ( " + currentDate + " )";
            addPDFHeading(document, pdfHeading);

            String fileName = type + " " + currentDate;
            String saveDialogTitle = type + " List";
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            // new File(System.getProperty("user.home") + "\\Desktop" + "\\Unpaid Users "+ currentDate +".pdf");
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", type + " List Saved.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception exception) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }


    }

    @FXML
    public void PDFMonthlyEvaluationList(ActionEvent event) {
        try (final PDDocument document = new PDDocument()) {
            createTableWithOverflowOnSamePage(document, createMELTable());
            LocalDate currentdate = LocalDate.now();
            String monthYear = currentdate.getMonth() + " " + currentdate.getYear() ;

            monthYear =  monthYear.substring(0,1).toUpperCase() + monthYear.substring(1).toLowerCase();

            String pdfHeading = "Monthly Evaluation List ( " + monthYear + " )";
            addPDFHeading(document, pdfHeading);

            String fileName = "Monthly Evaluation List ( " + monthYear + " )";
            String saveDialogTitle = "Monthly Evaluation List";
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", "Monthly Evaluation List Saved.", Alert.AlertType.INFORMATION);
            } else {
                document.close();
            }


        } catch (Exception exception) {
            exception.printStackTrace();
            this.alert("Error", "Something went wrong in printMonthyEvaluationList ", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void PDFDefaulters(ActionEvent event) {
        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document, createDefalutersTable());

            String pdfHeading = "Defaulters List ( " + currentDate + " )";
            addPDFHeading(document, pdfHeading);

            String fileName = "Defaulters " + currentDate;
            String saveDialogTitle = "Defaulters List";
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            // new File(System.getProperty("user.home") + "\\Desktop" + "\\Unpaid Users "+ currentDate +".pdf");
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", "Defaulters List Saved.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception exception) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void PDFActiveUsers(ActionEvent event) {
        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document, createActivesTable());
            String type = "Active Users";
            String pdfHeading = type + " List ( " + currentDate + " )";
            addPDFHeading(document, pdfHeading);

            String fileName = type + " " + currentDate;
            String saveDialogTitle = type + " List";
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            // new File(System.getProperty("user.home") + "\\Desktop" + "\\Unpaid Users "+ currentDate +".pdf");
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", type + " List Saved.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception exception) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void PDFInActiveUsers(ActionEvent event) {
        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document, createInActivesTable());
            String type = "InActive Users";
            String pdfHeading = type + " List ( " + currentDate + " )";
            addPDFHeading(document, pdfHeading);

            String fileName = type + " " + currentDate;
            String saveDialogTitle = type + " List";
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            // new File(System.getProperty("user.home") + "\\Desktop" + "\\Unpaid Users "+ currentDate +".pdf");
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", type + " List Saved.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception exception) {
            this.alert("Error", "Something went wrong", Alert.AlertType.ERROR);
        }
    }

    public void PDFUserPaymentHistory(ActionEvent event){
        if(userNameField.getText().equals("")){
            alert("Error", "Enter user ID.", Alert.AlertType.ERROR);
            return;
        }
        if(!isNumeric(userNameField.getText())){
            alert("Error", "Invalid user ID.", Alert.AlertType.ERROR);
            return;
        }

        try (final PDDocument document = new PDDocument()) {
            createTableWithOverflowOnSamePage(document, createPaymentHistoryTable());

            String pdfHeading = "Payment History "+ areaCode + userNameField.getText();
            addPDFHeading(document, pdfHeading);

            String fileName = pdfHeading;
            String saveDialogTitle = pdfHeading;
            File PDF = getSavingLocation(saveDialogTitle, fileName);
            if (PDF != null) {
                document.save(PDF);
                document.close();
                this.alert("Print Message", "PDF Saved.", Alert.AlertType.INFORMATION);
            } else {
                document.close();
            }


        } catch (Exception exception) {
            exception.printStackTrace();
            this.alert("Error", "Something went wrong in Payment History PDF ", Alert.AlertType.ERROR);
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            monthText.setText(String.valueOf(date.getMonth()));
            loadAreas();
            loadYears();
        } catch (SQLException e) {
            this.alert("Error", "Error in loading areas ", Alert.AlertType.ERROR);;
        }
    }
}
