module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.net.http;
    requires static lombok;
    requires java.sql;

    // Spring Boot
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.beans;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.tx;
    requires spring.core;

    // JPA
    requires jakarta.persistence;

    // Jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    // Hibernate
    requires org.hibernate.orm.core;

    // Connection pool
    requires com.zaxxer.hikari;

    opens lab to javafx.fxml, com.fasterxml.jackson.databind;
    opens lab.score to javafx.fxml, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server to spring.beans, spring.context, spring.web, spring.boot.autoconfigure, spring.core, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server.entity to org.hibernate.orm.core, spring.beans, jakarta.persistence, spring.core, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server.repository to spring.data.jpa, spring.beans, spring.core, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server.controller to spring.web, spring.beans, spring.core, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server.service to spring.beans, spring.context, spring.core, com.fasterxml.jackson.databind;
    opens cz.vsb.fei.java2.server.config to spring.beans, spring.context, spring.core, com.fasterxml.jackson.databind;

    exports lab;
    exports lab.score;
}
