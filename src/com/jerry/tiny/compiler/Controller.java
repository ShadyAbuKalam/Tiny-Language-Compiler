package com.jerry.tiny.compiler;

import com.jerry.tiny.compiler.scanner.TinyScanner;
import com.jerry.tiny.compiler.scanner.Token;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.io.ByteArrayInputStream;


public class Controller {
    public TextArea CodeArea;
    public TableView TokenTable;
    public ObservableList<Token> tokens = FXCollections.observableArrayList();
    public TableColumn TokenColumn;
    
    @FXML
    public void initialize() {
        TokenTable.setItems(tokens);
        
    }
    
    public void ScanCode(ActionEvent actionEvent) {
        tokens.clear();
        TinyScanner scanner = new TinyScanner();
        
        scanner.setInputStream(new ByteArrayInputStream(CodeArea.getText().getBytes()));
        Token token;
    
        while((token = scanner.scan()) != null){
            tokens.add(token);
        }
    }
}
