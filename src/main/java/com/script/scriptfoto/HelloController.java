package com.script.scriptfoto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        File selectedFile = fileChooser.showOpenDialog(welcomeText.getScene().getWindow());
        String origem = selectedFile.getAbsolutePath();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
        File selectedDirectory = directoryChooser.showDialog(welcomeText.getScene().getWindow());
        String destino = selectedDirectory.getAbsolutePath();
        System.out.println(destino);
        String seqproduto;
        BufferedReader br = new BufferedReader(new FileReader(origem));
        String line;
        Connection con = DriverManager.getConnection(("""
                    TNS vai aqui                    
                """, "USUARIO", "SENHA");

        Statement state = con.createStatement();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(";");
            seqproduto = values[0];
            ResultSet rs = state.executeQuery("SELECT A.CODACESSO FROM MAP_PRODCODIGO A\n" +
                                            "       WHERE A.SEQPRODUTO = " + seqproduto + " \n" +
                                              "       AND A.TIPCODIGO = 'E'");
            rs.next();
            String ean = rs.getString("CODACESSO");
            URI uri = new URI("http://www.eanpictures.com.br:9000/api/gtin/" + ean);
            URL url = uri.toURL();
            try {
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(destino + "\\" + seqproduto + ".jpg"));
                for (int i; (i = in.read()) != -1; ) {
                    out.write(i);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                System.out.println("Imagem não encontrada");
            }
        }

    }
}