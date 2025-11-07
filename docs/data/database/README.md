# Banco de Dados

## Modelo Conceitual

<img src='./ConceptualModel.png'>

---

## Modelo Logico

<img src='./LogicModel.png'>

---

## Modelo Físico

```bash

CREATE TABLE mothers (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  cpf VARCHAR(14),
  email VARCHAR(100),
  phone VARCHAR(20)
);

CREATE TABLE services (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  description TEXT
);

CREATE TABLE meets (
  id INT PRIMARY KEY AUTO_INCREMENT,
  TIMESTAMP DATE,
  address VARCHAR(200),
  status VARCHAR(50)
);

CREATE TABLE enlace (
  idservices INT,
  idmothers INT,
  idmeets INT,
  PRIMARY KEY (idservices, idmothers, idmeets),
  FOREIGN KEY (idservices) REFERENCES services(id),
  FOREIGN KEY (idmothers) REFERENCES mothers(id),
  FOREIGN KEY (idmeets) REFERENCES meets(id)
);

```