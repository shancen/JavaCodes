CREATE TABLE t_user(
      userId  VARCHAR(14) NOT NULL PRIMARY KEY COMMENT '用户id',
      userName VARCHAR(10) NOT NULL COMMENT '用户名',
      sex   VARCHAR(1) NOT NULL COMMENT '性别',
      userAddress VARCHAR(256) NOT NULL COMMENT '地址',
      linkPhone   VARCHAR(11) NOT NULL COMMENT '联系方式',
      status     VARCHAR(3) NOT NULL COMMENT '状态',
      oprCode   VARCHAR(14) NOT NULL COMMENT '创建人',
      modifyOpr  VARCHAR(14) NOT NULL COMMENT '修改人',
      createDate timestamp NOT NULL COMMENT '创建时间',
      stateDate  timestamp NOT NULL COMMENT '更新时间',
      extKeyOne VARCHAR(128)  NULL COMMENT '拓展字段1',
      extKeyTwo VARCHAR(128)  NULL COMMENT '拓展字段2',
      extKeyThr VARCHAR(128)  NULL COMMENT '拓展字段3'
)engine = InnoDB character set = utf8mb4;