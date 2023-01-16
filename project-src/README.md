# Information Technology (ITSCI)
## โครงร่างโปรเจ็คสำหรับนักศึกษาของสาขาเทคโนโลยีสารสนเทศ

### คำสั่งสำหรับสร้างฐานข้อมูล
```
CREATE SCHEMA `itsci_template_db` DEFAULT CHARACTER SET utf8mb4 ;
```

### คำสั่งสำหรับแทรกข้อมูลเริ่มต้นลงระบบฐานข้อมูล
```
use itsci_exam_db;

INSERT INTO `itsci_template_db`.`authorities` (`authority`, `description`) VALUES ('ROLE_ADMIN', 'ผู้ดูแลระบบ');
INSERT INTO `itsci_template_db`.`authorities` (`authority`, `description`) VALUES ('ROLE_MEMBER', 'สมาชิก');
```

### คำสั่งสำหรับแทรกข้อมูล login โดยมีรหัสผ่านเป็น '1234'
```
INSERT INTO `itsci_template_db`.`logins` (`enabled`, `password`, `username`) VALUES ('1', '{bcrypt}$2a$10$/GUlfBF1jG6Z7h2IiF6UGOCniw.HQeza8pWpW/x2eGWm6LL/rAlLO', 'infoitsci@mju.ac.th');
```

### คำสั่งสำหรับแทรกข้อมูลผู้ใช้
```
INSERT INTO `itsci_template_db`.`users` (`DTYPE`, `firstName`, `lastName`, `expiredDate`, `validFrom`, `login_id`) VALUES ('Member', 'itsci', 'itsci', NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), '1');
```
