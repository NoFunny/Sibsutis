package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import java.math.BigInteger;

public class Controller {

    @FXML
    private Text output;
    private BigInteger num1 = new BigInteger("0");
    private BigInteger num2 = new BigInteger("0");

    private boolean start = true;

    private String operator = "";
    private Model model = new Model();

    @FXML
    private void numbers(ActionEvent event) {
        if(start) {
            output.setText("");
            start = false;
        }
        String value = ((Button)event.getSource()).getText();
        output.setText(output.getText()+value);
    }
    @FXML
    private void operators(ActionEvent event) {
        String value = ((Button)event.getSource()).getText();
        if(!"=".equals(value)) {
            if(!operator.isEmpty())
                return;
            operator = value;
            num1 = new BigInteger(output.getText());
            output.setText("");
        }else{
            if (operator.isEmpty()) return;
            output.setText(String.valueOf(model.calculation(num1,new BigInteger(output.getText()), operator)));
            operator = "";
            start = true;
        }
        if(value.equals("C")){
            output.setText("");
            operator = "";
            start = true;
        }
    }

}
