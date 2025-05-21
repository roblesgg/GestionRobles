/**
 * 
 */
/**
 * 
 */
module GestionRobles {
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires java.sql;
    exports main;
    opens controller to javafx.fxml;
    opens model to javafx.base;


}