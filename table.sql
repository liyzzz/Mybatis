CREATE TABLE `tbl_dept` (
  `dept_id` int(16) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `tbl_emp` (
  `emp_id` int(16) NOT NULL AUTO_INCREMENT,
  `emp_name` varchar(64) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `d_id` int(16) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=167016 DEFAULT CHARSET=utf8;


CREATE TABLE `blog` (
  `bid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `author` (
  `author_id` int(16) NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8;

CREATE TABLE `comment` (
  `comment_id` int(16) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `bid` int(16) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `blog` (`bid`, `name`, `author_id`) VALUES (1, 'spring源码分析', 1001);
INSERT INTO `blog` (`bid`, `name`, `author_id`) VALUES (2, 'MyBatis源码分析', 1008);
INSERT INTO `author` (`author_id`, `author_name`) VALUES (1001, '李岳泽');
INSERT INTO `comment` (`comment_id`, `content`, `bid`) VALUES (1, '学习了', 1);
INSERT INTO `comment` (`comment_id`, `content`, `bid`) VALUES (2, '刚好碰到这个问题，谢谢', 1);




INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (1, '王路', 'M', 'a@b.com', 2);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (2, '李心', 'F', 'a@b.com', 2);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (3, '杨杰', 'F', 'a@b.com', 1);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (4, '候诚', 'F', 'a@b.com', 2);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (5, '张严', 'M', 'a@b.com', 1);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (6, '何宽', 'F', 'a@b.com', 2);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (7, '李微', 'M', 'a@b.com', 1);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (8, '刘虹', 'M', 'a@b.com', 1);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (9, '黄羽', 'F', 'a@b.com', 2);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (10, '吴莉', 'F', 'a@b.com', 3);
INSERT INTO `tbl_emp` (`emp_id`, `emp_name`, `gender`, `email`, `d_id`) VALUES (11, '阿里', 'M', 'a@b.com', 3);
