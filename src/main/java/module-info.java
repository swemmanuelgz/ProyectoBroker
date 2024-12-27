module com.example.proyectobroker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.fasterxml.jackson.databind;
    requires jbcrypt;
    requires spring.security.crypto;
    requires org.apache.httpcomponents.httpcore;
    requires spring.jcl;
    requires java.net.http;
    requires finazon.grpc.java;
    requires io.grpc;

    opens com.example.proyectobroker to javafx.fxml;
    exports com.example.proyectobroker;
    exports com.example.proyectobroker.controller;
    opens com.example.proyectobroker.controller to javafx.fxml;
}