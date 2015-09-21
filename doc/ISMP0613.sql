---------------------------------------------
-- Export file for user ISMP               --
-- Created by Kitian on 2011/6/13, 9:07:50 --
---------------------------------------------

spool ISMP0613.log

prompt
prompt Creating table ACQUIRE_FAULT_TRX
prompt ================================
prompt
create table ACQUIRE_FAULT_TRX
(
  ID               NUMBER(19) not null,
  VERSION          NUMBER(19) not null,
  ACQUIRE_AUTHCODE VARCHAR2(255 CHAR),
  ACQUIRE_CARDNUM  VARCHAR2(19 CHAR),
  ACQUIRE_CODE     VARCHAR2(255 CHAR) not null,
  ACQUIRE_DATE     VARCHAR2(255 CHAR),
  ACQUIRE_MERCHANT VARCHAR2(255 CHAR) not null,
  ACQUIRE_REFNUM   VARCHAR2(255 CHAR),
  ACQUIRE_SEQ      VARCHAR2(255 CHAR),
  ACQUIRE_TRXNUM   VARCHAR2(255 CHAR) not null,
  AUTH_DATE        TIMESTAMP(6) not null,
  AUTH_OPER        VARCHAR2(255 CHAR),
  AUTH_STS         VARCHAR2(1 CHAR) not null,
  CHANGE_APPLIER   VARCHAR2(255 CHAR) not null,
  CHANGE_STS       NUMBER(10) not null,
  CREATE_DATE      TIMESTAMP(6) not null,
  DATASRC          VARCHAR2(9 CHAR),
  FAULT_ADVICE     VARCHAR2(255 CHAR),
  FINAL_RESULT     VARCHAR2(255 CHAR),
  FINAL_STS        VARCHAR2(9 CHAR) not null,
  TRXAMOUNT        NUMBER(10) not null,
  TRXDATE          VARCHAR2(8 CHAR) not null,
  TRXID            VARCHAR2(255 CHAR) not null,
  UPDATE_DATE      TIMESTAMP(6) not null,
  INI_STS          NUMBER(10),
  PAYER_IP         VARCHAR2(255 CHAR),
  BATCHNUM         VARCHAR2(255 CHAR)
)
;
alter table ACQUIRE_FAULT_TRX
  add primary key (ID);

prompt
prompt Creating table ACQUIRE_SYN_TRX
prompt ==============================
prompt
create table ACQUIRE_SYN_TRX
(
  ID               NUMBER(19) not null,
  VERSION          NUMBER(19) not null,
  ACQUIRE_AUTHCODE VARCHAR2(255 CHAR),
  ACQUIRE_CARDNUM  VARCHAR2(255 CHAR),
  ACQUIRE_CODE     VARCHAR2(255 CHAR) not null,
  ACQUIRE_DATE     VARCHAR2(255 CHAR),
  ACQUIRE_MERCHANT VARCHAR2(255 CHAR) not null,
  ACQUIRE_REFNUM   VARCHAR2(255 CHAR),
  ACQUIRE_SEQ      VARCHAR2(255 CHAR),
  AMOUNT           NUMBER(10) not null,
  BATCHNUM         VARCHAR2(255 CHAR),
  CREATE_DATE      TIMESTAMP(6) not null,
  PAYER_IP         VARCHAR2(255 CHAR),
  TRXID            VARCHAR2(255 CHAR) not null,
  TRXSTS           NUMBER(10) not null
)
;
alter table ACQUIRE_SYN_TRX
  add primary key (ID);

prompt
prompt Creating table CM_CORPORATION_INFO
prompt ==================================
prompt
create table CM_CORPORATION_INFO
(
  ID                        NUMBER(19) not null,
  BUSINESS_LICENSE_CODE     VARCHAR2(20 CHAR),
  BUSINESS_SCOPE            VARCHAR2(500 CHAR),
  CHECK_DATE                TIMESTAMP(6),
  CHECK_OPERATOR_ID         NUMBER(19),
  CHECK_STATUS              VARCHAR2(16 CHAR),
  COMPANY_PHONE             VARCHAR2(20 CHAR),
  CONTACT                   VARCHAR2(32 CHAR),
  CONTACT_PHONE             VARCHAR2(20 CHAR),
  CORPORATE                 VARCHAR2(32 CHAR),
  EXPECTED_TURNOVER_OF_YEAR VARCHAR2(20 CHAR),
  LICENSE_EXPIRES           TIMESTAMP(6),
  NOTE                      VARCHAR2(128 CHAR),
  NUMBER_OF_STAFF           VARCHAR2(20 CHAR),
  OFFICE_LOCATION           VARCHAR2(200 CHAR),
  ORGANIZATION_CODE         VARCHAR2(20 CHAR),
  REGISTERED_FUNDS          NUMBER(10),
  REGISTERED_PLACE          VARCHAR2(200 CHAR),
  REGISTRATION_DATE         TIMESTAMP(6),
  REGISTRATION_NAME         VARCHAR2(64 CHAR),
  TAX_REGISTRATION_NO       VARCHAR2(20 CHAR),
  ZIP_CODE                  VARCHAR2(10 CHAR)
)
;
alter table CM_CORPORATION_INFO
  add primary key (ID);

prompt
prompt Creating table CM_CUSTOMER
prompt ==========================
prompt
create table CM_CUSTOMER
(
  ID           NUMBER(19) not null,
  VERSION      NUMBER(19) not null,
  ACCOUNT_NO   VARCHAR2(24 CHAR),
  API_KEY      VARCHAR2(64 CHAR),
  CUSTOMER_NO  VARCHAR2(24 CHAR) not null,
  DATE_CREATED TIMESTAMP(6) not null,
  LAST_UPDATED TIMESTAMP(6) not null,
  NAME         VARCHAR2(32 CHAR) not null,
  NEED_INVOICE NUMBER(1),
  STATUS       VARCHAR2(16 CHAR) not null,
  TYPE         VARCHAR2(1 CHAR) not null
)
;
alter table CM_CUSTOMER
  add primary key (ID);
alter table CM_CUSTOMER
  add unique (CUSTOMER_NO);

prompt
prompt Creating table CM_CUSTOMER_ACCOUNT_MAPPING
prompt ==========================================
prompt
create table CM_CUSTOMER_ACCOUNT_MAPPING
(
  ID           NUMBER(19) not null,
  VERSION      NUMBER(19) not null,
  ACCOUNT_NO   VARCHAR2(24 CHAR) not null,
  ACCOUNT_TYPE VARCHAR2(8 CHAR) not null,
  CUSTOMER_ID  NUMBER(19) not null
)
;
alter table CM_CUSTOMER_ACCOUNT_MAPPING
  add primary key (ID);
alter table CM_CUSTOMER_ACCOUNT_MAPPING
  add constraint FK9AAE9890CCF2A3E5 foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_CUSTOMER_BANK_ACCOUNT
prompt =======================================
prompt
create table CM_CUSTOMER_BANK_ACCOUNT
(
  ID                NUMBER(19) not null,
  VERSION           NUMBER(19) not null,
  BANK_ACCOUNT_NAME VARCHAR2(40 CHAR) not null,
  BANK_ACCOUNT_NO   VARCHAR2(32 CHAR) not null,
  BANK_CODE         VARCHAR2(16 CHAR) not null,
  BANK_NO           VARCHAR2(16 CHAR) not null,
  BRANCH            VARCHAR2(64 CHAR) not null,
  CUSTOMER_ID       NUMBER(19) not null,
  IS_CORPORATE      NUMBER(1) not null,
  IS_DEFAULT        NUMBER(1) not null,
  IS_VERIFY         NUMBER(1) not null,
  NOTE              VARCHAR2(128 CHAR),
  STATUS            VARCHAR2(16 CHAR) not null,
  SUBBRANCH         VARCHAR2(64 CHAR) not null,
  DATE_CREATED      TIMESTAMP(6) not null
)
;
alter table CM_CUSTOMER_BANK_ACCOUNT
  add primary key (ID);
alter table CM_CUSTOMER_BANK_ACCOUNT
  add constraint FK4C071FD6CCF2A3E5 foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_CUSTOMER_OPERATOR
prompt ===================================
prompt
create table CM_CUSTOMER_OPERATOR
(
  ID               NUMBER(19) not null,
  VERSION          NUMBER(19) not null,
  CUSTOMER_ID      NUMBER(19) not null,
  DATE_CREATED     TIMESTAMP(6) not null,
  DEFAULT_EMAIL    VARCHAR2(64 CHAR) not null,
  DEFAULT_MOBILE   VARCHAR2(16 CHAR),
  LAST_LOGIN_TIME  TIMESTAMP(6),
  LAST_UPDATED     TIMESTAMP(6) not null,
  LOGIN_ERROR_TIME NUMBER(10) not null,
  LOGIN_PASSWORD   VARCHAR2(40 CHAR),
  NAME             VARCHAR2(32 CHAR) not null,
  PAY_PASSWORD     VARCHAR2(40 CHAR),
  ROLE_SET         VARCHAR2(64 CHAR) not null,
  STATUS           VARCHAR2(16 CHAR) not null
)
;
alter table CM_CUSTOMER_OPERATOR
  add primary key (ID);
alter table CM_CUSTOMER_OPERATOR
  add constraint FK36A14350CCF2A3E5 foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_DYNAMIC_KEY
prompt =============================
prompt
create table CM_DYNAMIC_KEY
(
  ID           NUMBER(19) not null,
  VERSION      NUMBER(19) not null,
  CUSTOMER_ID  NUMBER(19) not null,
  DATE_CREATED TIMESTAMP(6) not null,
  IS_USED      NUMBER(1) not null,
  KEY          VARCHAR2(32 CHAR) not null,
  PARAMETER    VARCHAR2(36 CHAR) not null,
  PROC_METHOD  VARCHAR2(8 CHAR) not null,
  SEND_TO      VARCHAR2(32 CHAR) not null,
  SEND_TYPE    VARCHAR2(8 CHAR) not null,
  TIME_EXPIRED TIMESTAMP(6) not null,
  TIME_USED    TIMESTAMP(6),
  USE_TYPE     VARCHAR2(16 CHAR) not null,
  VERIFICATION VARCHAR2(36 CHAR) not null
)
;
alter table CM_DYNAMIC_KEY
  add primary key (ID);
alter table CM_DYNAMIC_KEY
  add constraint FKB215120ACCF2A3E5 foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_LOGIN_CERTIFICATE
prompt ===================================
prompt
create table CM_LOGIN_CERTIFICATE
(
  ID                   NUMBER(19) not null,
  VERSION              NUMBER(19) not null,
  CERTIFICATE_TYPE     VARCHAR2(8 CHAR) not null,
  CUSTOMER_OPERATOR_ID NUMBER(19) not null,
  DATE_CREATED         TIMESTAMP(6) not null,
  IS_VERIFY            NUMBER(1) not null,
  LAST_UPDATED         TIMESTAMP(6) not null,
  LOGIN_CERTIFICATE    VARCHAR2(64 CHAR) not null
)
;
alter table CM_LOGIN_CERTIFICATE
  add primary key (ID);
alter table CM_LOGIN_CERTIFICATE
  add unique (LOGIN_CERTIFICATE);
alter table CM_LOGIN_CERTIFICATE
  add constraint FK8405292CB043B242 foreign key (CUSTOMER_OPERATOR_ID)
  references CM_CUSTOMER_OPERATOR (ID);

prompt
prompt Creating table CM_LOGIN_LOG
prompt ===========================
prompt
create table CM_LOGIN_LOG
(
  ID                   NUMBER(19) not null,
  VERSION              NUMBER(19) not null,
  CUSTOMER_ID          NUMBER(19) not null,
  CUSTOMER_OPERATOR_ID NUMBER(19) not null,
  DATE_CREATED         TIMESTAMP(6) not null,
  LOGIN_CERTIFICATE    VARCHAR2(64 CHAR) not null,
  LOGIN_IP             VARCHAR2(20 CHAR) not null,
  LOGIN_RESULT         VARCHAR2(8 CHAR) not null
)
;
alter table CM_LOGIN_LOG
  add primary key (ID);
alter table CM_LOGIN_LOG
  add constraint FK9FDEEFD9B043B242 foreign key (CUSTOMER_OPERATOR_ID)
  references CM_CUSTOMER_OPERATOR (ID);
alter table CM_LOGIN_LOG
  add constraint FK9FDEEFD9CCF2A3E5 foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_PERSONAL_INFO
prompt ===============================
prompt
create table CM_PERSONAL_INFO
(
  ID                 NUMBER(19) not null,
  DATE_CERTIFICATION TIMESTAMP(6),
  IDENTITY_NO        VARCHAR2(32 CHAR),
  IDENTITY_TYPE      VARCHAR2(8 CHAR),
  IS_CERTIFICATION   NUMBER(1)
)
;
alter table CM_PERSONAL_INFO
  add primary key (ID);

prompt
prompt Creating table CM_ROYALTY_BINDING
prompt =================================
prompt
create table CM_ROYALTY_BINDING
(
  ID                 NUMBER(19) not null,
  VERSION            NUMBER(19) not null,
  CUSTOMER_ID        NUMBER(19) not null,
  DATE_CREATED       TIMESTAMP(6) not null,
  NOPASS_REFUND_FLAG VARCHAR2(1 CHAR) not null,
  OUT_CUSTOMER_CODE  VARCHAR2(255 CHAR),
  PARTNER_ID         NUMBER(19) not null,
  STATUS             VARCHAR2(4 CHAR) not null
)
;
alter table CM_ROYALTY_BINDING
  add primary key (ID);
alter table CM_ROYALTY_BINDING
  add constraint FKC7343F3D9A75CD2A foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);
alter table CM_ROYALTY_BINDING
  add constraint FKC7343F3DE375000 foreign key (PARTNER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table CM_SEQ_CUSTNO
prompt ============================
prompt
create table CM_SEQ_CUSTNO
(
  ID      NUMBER(19) not null,
  VERSION NUMBER(19) not null
)
;
alter table CM_SEQ_CUSTNO
  add primary key (ID);

prompt
prompt Creating table GWGOODS
prompt ======================
prompt
create table GWGOODS
(
  ID          VARCHAR2(22) not null,
  GWORDERS_ID VARCHAR2(22),
  GOODID      VARCHAR2(64),
  GOODNAME    VARCHAR2(128) not null,
  COUNTS      NUMBER not null,
  UNITPRICE   NUMBER(10,2),
  AMOUNT      NUMBER(10,2) not null,
  CREATEDATE  DATE not null,
  GOODDESC    VARCHAR2(256)
)
;
alter table GWGOODS
  add constraint PK_GWGOODS primary key (ID);

prompt
prompt Creating table GWLGOPTIONS
prompt ==========================
prompt
create table GWLGOPTIONS
(
  LOGISTICS_TYPE    VARCHAR2(10) not null,
  LOGISTICS_FEE     NUMBER not null,
  LOGISTICS_PAYMENT VARCHAR2(10) not null,
  CREATEDATE        DATE,
  ID                VARCHAR2(22) not null,
  GWORDERS_ID       VARCHAR2(22)
)
;
alter table GWLGOPTIONS
  add constraint PK_GWLGOPTIONS primary key (ID);

prompt
prompt Creating table GWLOGISTIC
prompt =========================
prompt
create table GWLOGISTIC
(
  RECNAME     VARCHAR2(10) not null,
  RECPID      VARCHAR2(30),
  RECPHONE    VARCHAR2(30),
  RECADDR     VARCHAR2(128),
  RECMPHONE   VARCHAR2(30),
  RECPOST     VARCHAR2(8),
  DELIVER     VARCHAR2(20),
  DELIVERTIME VARCHAR2(128),
  DELIVERS    VARCHAR2(10),
  CREATEATE   DATE not null,
  ID          VARCHAR2(22) not null,
  GWORDERS_ID VARCHAR2(22)
)
;
alter table GWLOGISTIC
  add constraint PK_GWLOGISTIC primary key (ID);

prompt
prompt Creating table GWMESSAGES
prompt =========================
prompt
create table GWMESSAGES
(
  MESSAGEID      VARCHAR2(30) not null,
  ID             VARCHAR2(22),
  EXTTYPE        NUMBER,
  EXTID          VARCHAR2(20) not null,
  NOTIFYVALUE    VARCHAR2(800) not null,
  NOTIFYTEMPLATE VARCHAR2(64),
  RECEIVER       VARCHAR2(256) not null,
  PUB            VARCHAR2(20),
  SUBJECT        VARCHAR2(64),
  NOTIFYMODE     NUMBER not null,
  MSGSTATE       NUMBER not null,
  PROPARITY      NUMBER,
  RESTIMES       NUMBER not null,
  CREATEDATE     DATE not null,
  RESCODE        VARCHAR2(20),
  RESDAT         DATE,
  MSGTYPE        VARCHAR2(30) not null,
  XDESC2         VARCHAR2(64),
  XDESC          VARCHAR2(64)
)
;
alter table GWMESSAGES
  add constraint PK_GWMESSAGES primary key (MESSAGEID);

prompt
prompt Creating table GWORDERS
prompt =======================
prompt
create table GWORDERS
(
  ID                 VARCHAR2(22) not null,
  SERVICE            VARCHAR2(64) not null,
  ORDERNUM           VARCHAR2(128) not null,
  PARTNERID          VARCHAR2(22) not null,
  SELLER_NAME        VARCHAR2(100) not null,
  SELLER_ID          VARCHAR2(22) not null,
  BUYER_NAME         VARCHAR2(100),
  BUYER_ID           VARCHAR2(22),
  PRICE              NUMBER not null,
  QUANTITY           NUMBER not null,
  SIGN_TYPE          VARCHAR2(10) not null,
  DISCOUNT           NUMBER not null,
  DISCOUNT_MODE      VARCHAR2(10),
  DISCOUNTDESC       VARCHAR2(20),
  CURRENCY           VARCHAR2(4) not null,
  ORDERDATE          VARCHAR2(8) not null,
  CREATEDATE         DATE not null,
  QUERY_KEY          VARCHAR2(64),
  EXP_DATES          VARCHAR2(5) not null,
  IPS                VARCHAR2(20) not null,
  ORDERSTS           VARCHAR2(64) not null,
  SELLER_REMARKS     VARCHAR2(512),
  BUYER_REMARKS      VARCHAR2(512),
  RETURN_URL         VARCHAR2(512),
  NOTIFY_URL         VARCHAR2(512),
  SUBJECT            VARCHAR2(256) not null,
  BODYS              VARCHAR2(512) not null,
  ROYALTY_TYPE       VARCHAR2(2),
  ROYALTY_PARAMETERS VARCHAR2(512),
  AMOUNT             NUMBER not null,
  GWLGOPTIONS_ID     VARCHAR2(22),
  PRICECHANGED       NUMBER(1),
  APIVERSION         VARCHAR2(20),
  LOCALE             VARCHAR2(10) not null,
  PREFERENCE         VARCHAR2(10),
  REFUND_AMOUNT      NUMBER(19),
  REFUND_STS         VARCHAR2(10),
  ORDER_TYPE         VARCHAR2(3) not null,
  VERSION            NUMBER not null,
  GWL_UPDATE         DATE not null,
  CHARSETS           VARCHAR2(10) not null,
  PAYMETHOD          VARCHAR2(10),
  SERVICE_FEE        NUMBER,
  AGENTID            VARCHAR2(22),
  CLOSEDATE          DATE,
  PARTNER_ID         VARCHAR2(22),
  SHOW_URL           VARCHAR2(512)
)/*
partition by range (CREATEDATE)
(
  partition G201104 values less than (TO_DATE(' 2011-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition G201107 values less than (TO_DATE(' 2011-07-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition G201110 values less than (TO_DATE(' 2011-10-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition REST values less than (MAXVALUE)
    tablespace TISMPS
)*/;
comment on column GWORDERS.ID
  is '订单主键';
comment on column GWORDERS.SERVICE
  is '服务名';
comment on column GWORDERS.ORDERNUM
  is '订单号';
comment on column GWORDERS.PARTNERID
  is '合作商家客户号';
comment on column GWORDERS.SELLER_NAME
  is '卖家名称';
comment on column GWORDERS.SELLER_ID
  is '卖家(收款)ID';
comment on column GWORDERS.BUYER_NAME
  is '买家名称';
comment on column GWORDERS.BUYER_ID
  is '买家(付款)ID';
comment on column GWORDERS.PRICE
  is '商品单价';
comment on column GWORDERS.QUANTITY
  is '商品数量';
comment on column GWORDERS.SIGN_TYPE
  is '签名类型[MD5,RSA]';
comment on column GWORDERS.DISCOUNT
  is '折扣';
comment on column GWORDERS.DISCOUNT_MODE
  is '折扣方式(红包。。）';
comment on column GWORDERS.DISCOUNTDESC
  is '折扣说明';
comment on column GWORDERS.CURRENCY
  is '币种';
comment on column GWORDERS.ORDERDATE
  is '商家订单日期';
comment on column GWORDERS.CREATEDATE
  is '创建日期';
comment on column GWORDERS.QUERY_KEY
  is '查询密钥';
comment on column GWORDERS.EXP_DATES
  is '订单过期表达式';
comment on column GWORDERS.IPS
  is '订单创建IP';
comment on column GWORDERS.ORDERSTS
  is '订单状态[0WAIT_BUYER_PAY,1WAIT_SELLER_SEND_GOODS,2WAIT_BUYER_CONFIRM_GOODS,3TRADE_FINISHED,4TRADE_CLOSED]';
comment on column GWORDERS.SELLER_REMARKS
  is '卖家(收款)备注';
comment on column GWORDERS.BUYER_REMARKS
  is '买家(付款)备注';
comment on column GWORDERS.RETURN_URL
  is '返回URL';
comment on column GWORDERS.NOTIFY_URL
  is '通知URL';
comment on column GWORDERS.SUBJECT
  is '订单主题';
comment on column GWORDERS.BODYS
  is '订单说明';
comment on column GWORDERS.ROYALTY_TYPE
  is '分润类型';
comment on column GWORDERS.ROYALTY_PARAMETERS
  is '分润参数';
comment on column GWORDERS.AMOUNT
  is '总金额';
comment on column GWORDERS.GWLGOPTIONS_ID
  is '物流ID';
comment on column GWORDERS.PRICECHANGED
  is '价格是否调整[0未调整 1 调整]';
comment on column GWORDERS.APIVERSION
  is 'API版本[aplipay,chinabank]';
comment on column GWORDERS.LOCALE
  is '本地语言';
comment on column GWORDERS.PREFERENCE
  is '偏爱银行';
comment on column GWORDERS.REFUND_AMOUNT
  is '退款金额';
comment on column GWORDERS.REFUND_STS
  is '退款状态';
comment on column GWORDERS.ORDER_TYPE
  is '订单类型[1:商品购买 01:红包结算金预收款 02红包结算金 04 自动发货商品 2 充值]';
comment on column GWORDERS.VERSION
  is '版本';
comment on column GWORDERS.GWL_UPDATE
  is '物流更新时间';
comment on column GWORDERS.CHARSETS
  is '字符集';
comment on column GWORDERS.PAYMETHOD
  is '支付访问';
comment on column GWORDERS.SERVICE_FEE
  is '服务费';
comment on column GWORDERS.AGENTID
  is '代理商户ID';
comment on column GWORDERS.PARTNER_ID
  is '合作商家系统ID';
comment on column GWORDERS.SHOW_URL
  is '显示URL';
alter table GWORDERS
  add constraint PK_GWORDERS primary key (ID);
create index GWORDERS_CREATEDATE_IDX on GWORDERS (CREATEDATE);
create index GWORDERS_ORDERNUM_IDX on GWORDERS (ORDERNUM);
create index GWORDERS_PARTNERID_IDX on GWORDERS (PARTNERID);

prompt
prompt Creating table GWPROC
prompt =====================
prompt
create table GWPROC
(
  ID         VARCHAR2(22) not null,
  GWT_ID     VARCHAR2(22),
  TRXNUM     VARCHAR2(128) not null,
  TRXDATE    VARCHAR2(8),
  PROCID     VARCHAR2(10) not null,
  PROCNAME   VARCHAR2(30) not null,
  CREATEDATE DATE not null,
  PROSTS     VARCHAR2(3) not null,
  OPERDATE   DATE,
  OPERRESULT VARCHAR2(256),
  PARAMS     VARCHAR2(128),
  BATCH      VARCHAR2(30),
  OPERS      VARCHAR2(30)
)
;
comment on column GWPROC.GWT_ID
  is '交易主键';
comment on column GWPROC.TRXNUM
  is '外部交易号';
comment on column GWPROC.TRXDATE
  is '交易日期';
comment on column GWPROC.PROCID
  is '过程ID[0:POST,1:SETTLE]';
comment on column GWPROC.PROCNAME
  is '过程名称[0:TRX_POSTING,1:TRX_BANKSETTLE]';
comment on column GWPROC.CREATEDATE
  is '创建日期';
comment on column GWPROC.PROSTS
  is '过程状态[0WAIT_CONF,1WAIT_POST,2POSTING,3POST_SUCCESS,4POST_FAILURE]';
comment on column GWPROC.OPERDATE
  is '操作时间';
comment on column GWPROC.OPERRESULT
  is '操作结果';
comment on column GWPROC.PARAMS
  is '参数';
comment on column GWPROC.BATCH
  is '批号';
comment on column GWPROC.OPERS
  is '操作员';
alter table GWPROC
  add constraint PK_GWPROC primary key (ID);

prompt
prompt Creating table GWSYN
prompt ====================
prompt
create table GWSYN
(
  TRXNUM     VARCHAR2(128) not null,
  AMOUNT     NUMBER not null,
  TRXDATE    VARCHAR2(8) not null,
  BANK       VARCHAR2(10) not null,
  CREATEDATE DATE not null,
  BATCH      VARCHAR2(64)
)
;

prompt
prompt Creating table GWTRXS
prompt =====================
prompt
create table GWTRXS
(
  GWORDERS_ID       VARCHAR2(22),
  TRXNUM            VARCHAR2(128) not null,
  TRXTYPE           VARCHAR2(4) not null,
  CHANNEL           VARCHAR2(2) not null,
  PAYMENT_TYPE      VARCHAR2(2) not null,
  PAYMODE           VARCHAR2(2) not null,
  AMOUNT            NUMBER not null,
  CURRENCY          VARCHAR2(4) not null,
  SERVICECODE       VARCHAR2(20),
  ACQUIRER_CODE     VARCHAR2(20) not null,
  ACQUIRER_NAME     VARCHAR2(30),
  ACQUIRER_MERCHANT VARCHAR2(64) not null,
  ACQUIRER_SEQ      VARCHAR2(128),
  ACQUIRER_DATE     VARCHAR2(8),
  ACQUIRER_MSG      VARCHAR2(256),
  SUBMITDATES       VARCHAR2(20) not null,
  PAYER_IP          VARCHAR2(20),
  REFNUM            VARCHAR2(128),
  AUTHCODE          VARCHAR2(6),
  FROMACCTID        VARCHAR2(22),
  FROMACCTNUM       VARCHAR2(256) not null,
  BUYER_ID          VARCHAR2(22),
  BUYER_NAME        VARCHAR2(64),
  PAYINFO           VARCHAR2(64),
  CREATEDATE        DATE not null,
  CLOSEDATE         DATE not null,
  TRXSTS            VARCHAR2(3) not null,
  OPERS             VARCHAR2(10),
  OPERDATE          DATE not null,
  VERSION           NUMBER not null,
  TRXDESC           VARCHAR2(100),
  ID                VARCHAR2(22) not null
)/*
partition by range (CREATEDATE)
(
  partition P201104 values less than (TO_DATE(' 2011-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition P201107 values less than (TO_DATE(' 2011-07-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition P201110 values less than (TO_DATE(' 2011-10-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN'))
    tablespace TISMPS,
  partition REST values less than (MAXVALUE)
    tablespace TISMPS
)*/;
comment on column GWTRXS.GWORDERS_ID
  is '订单流水';
comment on column GWTRXS.TRXNUM
  is '交易号';
comment on column GWTRXS.TRXTYPE
  is '交易类型';
comment on column GWTRXS.CHANNEL
  is '交易通道[0:网上银行 1:余额支付]';
comment on column GWTRXS.PAYMENT_TYPE
  is '划款类型[0:立即到账 1:担保]';
comment on column GWTRXS.PAYMODE
  is '付款方式[0:借记 1:贷记 2:未知]';
comment on column GWTRXS.AMOUNT
  is '交易金额';
comment on column GWTRXS.CURRENCY
  is '币种';
comment on column GWTRXS.SERVICECODE
  is '支付服务码';
comment on column GWTRXS.ACQUIRER_CODE
  is '收单行编码';
comment on column GWTRXS.ACQUIRER_NAME
  is '收单行名称';
comment on column GWTRXS.ACQUIRER_MERCHANT
  is '收单商户号';
comment on column GWTRXS.ACQUIRER_SEQ
  is '收单流水';
comment on column GWTRXS.ACQUIRER_DATE
  is '收单返回日期';
comment on column GWTRXS.ACQUIRER_MSG
  is '收单信息备注';
comment on column GWTRXS.SUBMITDATES
  is '收单提交时间';
comment on column GWTRXS.PAYER_IP
  is '返回IP';
comment on column GWTRXS.REFNUM
  is '参考号';
comment on column GWTRXS.AUTHCODE
  is '授权号';
comment on column GWTRXS.FROMACCTID
  is '付款人账户ID';
comment on column GWTRXS.FROMACCTNUM
  is '付款账户名称';
comment on column GWTRXS.BUYER_ID
  is '收款人用户ID';
comment on column GWTRXS.BUYER_NAME
  is '收款人用户号';
comment on column GWTRXS.PAYINFO
  is '付款信息';
comment on column GWTRXS.CREATEDATE
  is '交易创建时间';
comment on column GWTRXS.CLOSEDATE
  is '交易完成时间';
comment on column GWTRXS.TRXSTS
  is '交易状态[0:WAIT_TRADE, 1:TRADE_FINISHED,2:TRADE_FAILURE,3:TRADE_CLOSED]';
comment on column GWTRXS.OPERS
  is '操作员';
comment on column GWTRXS.OPERDATE
  is '处理时间';
comment on column GWTRXS.VERSION
  is '版本';
comment on column GWTRXS.TRXDESC
  is '交易备注';
alter table GWTRXS
  add constraint PK_GGWTRXS primary key (ID);
create index GWTRXS_CLOSEDATE_IDX on GWTRXS (CLOSEDATE);
create index GWTRXS_GWORDERS_ID_IDX on GWTRXS (GWORDERS_ID);
create index GWTRXS_TRXNUM_IDX on GWTRXS (TRXNUM);

prompt
prompt Creating table MAPI_ASYNC_NOTIFY
prompt ================================
prompt
create table MAPI_ASYNC_NOTIFY
(
  ID                NUMBER(19) not null,
  DATE_CREATED      TIMESTAMP(6),
  LAST_UPDATED      TIMESTAMP(6),
  CUSTOMER_ID       NUMBER(19) not null,
  RECORD_TABLE      VARCHAR2(48),
  RECORD_ID         NUMBER(19),
  NOTIFY_METHOD     VARCHAR2(32) not null,
  NOTIFY_ADDRESS    VARCHAR2(128) not null,
  SIGN_TYPE         VARCHAR2(32),
  NOTIFY_CONTENTS   VARCHAR2(4000),
  NOTIFY_TIME       TIMESTAMP(6),
  NOTIFY_ID         VARCHAR2(64),
  NEXT_ATTEMPT_TIME TIMESTAMP(6),
  STATUS            VARCHAR2(32) not null,
  ATTEMPTS_COUNT    NUMBER,
  TIME_EXPIRED      TIMESTAMP(6),
  IS_VERIFY         NUMBER(1),
  OUTPUT_CHARSET    VARCHAR2(255 CHAR)
)
;
comment on table MAPI_ASYNC_NOTIFY
  is '异步通知客户表';
comment on column MAPI_ASYNC_NOTIFY.ID
  is 'ID';
comment on column MAPI_ASYNC_NOTIFY.DATE_CREATED
  is '创建时间 grails';
comment on column MAPI_ASYNC_NOTIFY.LAST_UPDATED
  is '更新时间 grails';
comment on column MAPI_ASYNC_NOTIFY.CUSTOMER_ID
  is '客户ID';
comment on column MAPI_ASYNC_NOTIFY.RECORD_TABLE
  is '业务表';
comment on column MAPI_ASYNC_NOTIFY.RECORD_ID
  is '业务表主键';
comment on column MAPI_ASYNC_NOTIFY.NOTIFY_METHOD
  is '通知方法 http email mobile';
comment on column MAPI_ASYNC_NOTIFY.NOTIFY_ADDRESS
  is '通知地址';
comment on column MAPI_ASYNC_NOTIFY.SIGN_TYPE
  is '签名类型';
comment on column MAPI_ASYNC_NOTIFY.NOTIFY_CONTENTS
  is '通知内容';
comment on column MAPI_ASYNC_NOTIFY.NOTIFY_TIME
  is '通知时间';
comment on column MAPI_ASYNC_NOTIFY.NOTIFY_ID
  is '通知ID';
comment on column MAPI_ASYNC_NOTIFY.NEXT_ATTEMPT_TIME
  is '下次尝试时间';
comment on column MAPI_ASYNC_NOTIFY.STATUS
  is '通知状态 未成功 已成功';
comment on column MAPI_ASYNC_NOTIFY.ATTEMPTS_COUNT
  is '尝试次数';
comment on column MAPI_ASYNC_NOTIFY.TIME_EXPIRED
  is '过期时间';
comment on column MAPI_ASYNC_NOTIFY.IS_VERIFY
  is '已经读取验证';
alter table MAPI_ASYNC_NOTIFY
  add constraint PK_MAPI_ASYNC_NOTIFY primary key (ID);
alter table MAPI_ASYNC_NOTIFY
  add constraint FKCFE43EDE9A75CD2A foreign key (CUSTOMER_ID)
  references CM_CUSTOMER (ID);

prompt
prompt Creating table TRADE_ACCOUNT_COMMAND_SAF
prompt ========================================
prompt
create table TRADE_ACCOUNT_COMMAND_SAF
(
  ID              NUMBER(19) not null,
  VERSION         NUMBER(19) not null,
  AMOUNT          NUMBER(19) not null,
  COMMAND_NO      VARCHAR2(40 CHAR) not null,
  COMMAND_TYPE    VARCHAR2(16 CHAR) not null,
  CURRENCY        VARCHAR2(4 CHAR) not null,
  DATE_CREATED    TIMESTAMP(6) not null,
  FROM_ACCOUNT_NO VARCHAR2(24 CHAR) not null,
  LAST_UPDATED    TIMESTAMP(6) not null,
  OUT_TRADE_NO    VARCHAR2(64 CHAR),
  REDO_COUNT      NUMBER(10),
  REDO_FLAG       VARCHAR2(4 CHAR),
  RESP_CODE       VARCHAR2(4 CHAR),
  SUB_SEQNO       NUMBER(10),
  SUBJECT         VARCHAR2(255 CHAR),
  SYNC_FLAG       VARCHAR2(1 CHAR) not null,
  SYNC_TIME       TIMESTAMP(6) not null,
  TO_ACCOUNT_NO   VARCHAR2(24 CHAR) not null,
  TRADE_ID        NUMBER(19) not null,
  TRADE_NO        VARCHAR2(36 CHAR) not null,
  TRANS_CODE      VARCHAR2(40 CHAR),
  TRANS_ID        NUMBER(19),
  TRANSFER_TYPE   VARCHAR2(16 CHAR) not null
)
;
alter table TRADE_ACCOUNT_COMMAND_SAF
  add primary key (ID);

prompt
prompt Creating table TRADE_BASE
prompt =========================
prompt
create table TRADE_BASE
(
  ID               NUMBER(19) not null,
  VERSION          NUMBER(19) not null,
  AMOUNT           NUMBER(19) not null,
  CURRENCY         VARCHAR2(4 CHAR) not null,
  DATE_CREATED     TIMESTAMP(6) not null,
  FEE_AMOUNT       NUMBER(19) not null,
  LAST_UPDATED     TIMESTAMP(6) not null,
  NOTE             VARCHAR2(64 CHAR),
  ORIGINAL_ID      NUMBER(19),
  OUT_TRADE_NO     VARCHAR2(64 CHAR),
  PARTNER_ID       NUMBER(19),
  PAYEE_ACCOUNT_NO VARCHAR2(24 CHAR) not null,
  PAYEE_CODE       VARCHAR2(64 CHAR),
  PAYEE_ID         NUMBER(19),
  PAYEE_NAME       VARCHAR2(32 CHAR),
  PAYER_ACCOUNT_NO VARCHAR2(24 CHAR) not null,
  PAYER_CODE       VARCHAR2(64 CHAR),
  PAYER_ID         NUMBER(19),
  PAYER_NAME       VARCHAR2(32 CHAR),
  ROOT_ID          NUMBER(19),
  STATUS           VARCHAR2(16 CHAR) not null,
  SUBJECT          VARCHAR2(256 CHAR),
  TRADE_DATE       NUMBER(10) not null,
  TRADE_NO         VARCHAR2(36 CHAR) not null,
  TRADE_TYPE       VARCHAR2(16 CHAR) not null
)
;
alter table TRADE_BASE
  add primary key (ID);

prompt
prompt Creating table TRADE_CHARGE
prompt ===========================
prompt
create table TRADE_CHARGE
(
  ID                 NUMBER(19) not null,
  ADDED_METHOD       VARCHAR2(16 CHAR),
  BACK_AMOUNT        NUMBER(19),
  FUNDING_SOURCE     VARCHAR2(16 CHAR),
  IS_CREDIT_CARD     NUMBER(1),
  PAYMENT_IP         VARCHAR2(20 CHAR),
  PAYMENT_REQUEST_ID NUMBER(19)
)
;
alter table TRADE_CHARGE
  add primary key (ID);

prompt
prompt Creating table TRADE_FROZEN
prompt ===========================
prompt
create table TRADE_FROZEN
(
  ID               NUMBER(19) not null,
  FROZEN_PARAMS    VARCHAR2(512 CHAR) not null,
  FROZEN_STATUS    VARCHAR2(16 CHAR) not null,
  FROZEN_TYPE      VARCHAR2(16 CHAR) not null,
  HANDLE_BATCH     VARCHAR2(16 CHAR),
  HANDLE_OPER_ID   NUMBER(19),
  HANDLE_OPER_NAME VARCHAR2(16 CHAR),
  HANDLE_STATUS    VARCHAR2(16 CHAR),
  HANDLE_TIME      TIMESTAMP(6),
  UNFROZEN_AMOUNT  NUMBER(19) not null
)
;
alter table TRADE_FROZEN
  add primary key (ID);

prompt
prompt Creating table TRADE_PAYMENT
prompt ============================
prompt
create table TRADE_PAYMENT
(
  ID                   NUMBER(19) not null,
  BODY                 VARCHAR2(512 CHAR),
  FROZEN_AMOUNT        NUMBER(19),
  OUT_ROYALTY_TRADE_NO VARCHAR2(128 CHAR) not null,
  PAYMENT_IP           VARCHAR2(20 CHAR) not null,
  PAYMENT_REQUEST_ID   NUMBER(19) not null,
  REFUND_AMOUNT        NUMBER(19),
  ROYALTY_PARAMS       VARCHAR2(512 CHAR),
  ROYALTY_STATUS       VARCHAR2(16 CHAR),
  ROYALTY_TYPE         VARCHAR2(16 CHAR),
  SHOW_URL             VARCHAR2(200 CHAR)
)
;
alter table TRADE_PAYMENT
  add primary key (ID);

prompt
prompt Creating table TRADE_REFUND
prompt ===========================
prompt
create table TRADE_REFUND
(
  ID                    NUMBER(19) not null,
  ACQUIRER_CODE         VARCHAR2(16 CHAR) not null,
  ACQUIRER_MERCHANT_NO  VARCHAR2(20 CHAR) not null,
  ACQUIRER_ACCOUNT_ID   NUMBER(19) not null,
  BACK_FEE              NUMBER(19) not null,
  CHECK_DATE            TIMESTAMP(6),
  CHECK_OPERATOR_ID     NUMBER(19),
  CHECK_STATUS          VARCHAR2(16 CHAR) not null,
  CUSTOMER_OPER_ID      NUMBER(19),
  HANDLE_BATCH          VARCHAR2(16 CHAR),
  HANDLE_COMMAND_NO     VARCHAR2(40 CHAR),
  HANDLE_OPER_ID        NUMBER(19),
  HANDLE_OPER_NAME      VARCHAR2(16 CHAR),
  HANDLE_STATUS         VARCHAR2(16 CHAR) not null,
  HANDLE_TIME           TIMESTAMP(6),
  REAL_REFUND_AMOUNT    NUMBER(19) not null,
  REFUND_PARAMS         VARCHAR2(512 CHAR) not null,
  REFUND_TYPE           VARCHAR2(16 CHAR) not null,
  ROYALTY_REFUND_STATUS VARCHAR2(16 CHAR) not null,
  SUBMIT_BATCH          VARCHAR2(16 CHAR) not null,
  SUBMIT_TYPE           VARCHAR2(32 CHAR) not null,
  SUBMITTER             VARCHAR2(32 CHAR) not null
)
;
alter table TRADE_REFUND
  add primary key (ID);

prompt
prompt Creating table TRADE_TRANSFER
prompt =============================
prompt
create table TRADE_TRANSFER
(
  ID               NUMBER(19) not null,
  CUSTOMER_OPER_ID NUMBER(19) not null,
  SUBMIT_IP        VARCHAR2(20 CHAR) not null,
  SUBMIT_TYPE      VARCHAR2(32 CHAR) not null,
  SUBMITTER        VARCHAR2(32 CHAR) not null
)
;
alter table TRADE_TRANSFER
  add primary key (ID);

prompt
prompt Creating table TRADE_UNFROZEN
prompt =============================
prompt
create table TRADE_UNFROZEN
(
  ID               NUMBER(19) not null,
  HANDLE_BATCH     VARCHAR2(16 CHAR) not null,
  HANDLE_OPER_NAME VARCHAR2(16 CHAR) not null,
  HANDLE_PPER_ID   NUMBER(19),
  HANDLE_STATUS    VARCHAR2(16 CHAR) not null,
  HANDLE_TIME      TIMESTAMP(6),
  UNFROZEN_PARAMS  VARCHAR2(512 CHAR) not null,
  UNFROZEN_STATUS  VARCHAR2(16 CHAR) not null,
  UNFROZEN_TYPE    VARCHAR2(16 CHAR) not null
)
;
alter table TRADE_UNFROZEN
  add primary key (ID);

prompt
prompt Creating table TRADE_WITHDRAWN
prompt ==============================
prompt
create table TRADE_WITHDRAWN
(
  ID                         NUMBER(19) not null,
  ACQUIRER_ACCOUNT_ID        NUMBER(19),
  CHECK_DATE                 TIMESTAMP(6),
  CHECK_OPERATOR_ID          NUMBER(19),
  CHECK_STATUS               VARCHAR2(16 CHAR) not null,
  CUSTOMER_BANK_ACCOUNT_ID   NUMBER(19) not null,
  CUSTOMER_BANK_ACCOUNT_NAME VARCHAR2(40 CHAR) not null,
  CUSTOMER_BANK_ACCOUNT_NO   VARCHAR2(32 CHAR) not null,
  CUSTOMER_BANK_CODE         VARCHAR2(16 CHAR),
  CUSTOMER_BANK_NO           VARCHAR2(16 CHAR) not null,
  CUSTOMER_OPER_ID           NUMBER(19) not null,
  HANDLE_BATCH               VARCHAR2(16 CHAR),
  HANDLE_COMMAND_NO          VARCHAR2(40 CHAR),
  HANDLE_OPER_ID             NUMBER(19),
  HANDLE_OPER_NAME           VARCHAR2(16 CHAR),
  HANDLE_STATUS              VARCHAR2(16 CHAR) not null,
  HANDLE_TIME                TIMESTAMP(6),
  IS_CORPORATE               NUMBER(1) not null,
  REAL_TRANSFER_AMOUNT       NUMBER(19) not null,
  SUBMIT_TYPE                VARCHAR2(32 CHAR) not null,
  SUBMITTER                  VARCHAR2(32 CHAR) not null,
  TRANSFER_FEE               NUMBER(19) not null
)
;
alter table TRADE_WITHDRAWN
  add primary key (ID);

prompt
prompt Creating sequence HIBERNATE_SEQUENCE
prompt ====================================
prompt
create sequence HIBERNATE_SEQUENCE
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_AC_ACCOUNT
prompt ================================
prompt
create sequence SEQ_AC_ACCOUNT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_AC_SEQUENTIAL
prompt ===================================
prompt
create sequence SEQ_AC_SEQUENTIAL
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_AC_TRANSACTION
prompt ====================================
prompt
create sequence SEQ_AC_TRANSACTION
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_CUSTNO
prompt ===============================
prompt
create sequence SEQ_CM_CUSTNO
minvalue 1
maxvalue 999999999999999999999999999
start with 125
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_CUSTOMER
prompt =================================
prompt
create sequence SEQ_CM_CUSTOMER
minvalue 1
maxvalue 999999999999999999999999999
start with 83
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_CUSTOMER_ACCOUNTMAPPING
prompt ================================================
prompt
create sequence SEQ_CM_CUSTOMER_ACCOUNTMAPPING
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_CUSTOMER_BANK_ACCOUNT
prompt ==============================================
prompt
create sequence SEQ_CM_CUSTOMER_BANK_ACCOUNT
minvalue 1
maxvalue 999999999999999999999999999
start with 47
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_CUSTOMER_OPERATOR
prompt ==========================================
prompt
create sequence SEQ_CM_CUSTOMER_OPERATOR
minvalue 1
maxvalue 999999999999999999999999999
start with 133
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_DYNAMIC_KEY
prompt ====================================
prompt
create sequence SEQ_CM_DYNAMIC_KEY
minvalue 1
maxvalue 999999999999999999999999999
start with 425
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_LOGIN_CERTIFICATE
prompt ==========================================
prompt
create sequence SEQ_CM_LOGIN_CERTIFICATE
minvalue 1
maxvalue 999999999999999999999999999
start with 114
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_CM_LOGIN_LOG
prompt ==================================
prompt
create sequence SEQ_CM_LOGIN_LOG
minvalue 1
maxvalue 999999999999999999999999999
start with 1293
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_MAPI_ASYNC_NOTIFY
prompt =======================================
prompt
create sequence SEQ_MAPI_ASYNC_NOTIFY
minvalue 1
maxvalue 9999999999
start with 844
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_MG_CUSTOMER_SERVICE
prompt =========================================
prompt
create sequence SEQ_MG_CUSTOMER_SERVICE
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_ORDER
prompt ===========================
prompt
create sequence SEQ_ORDER
minvalue 1
maxvalue 10000000
start with 4689
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_ORDEREXT
prompt ==============================
prompt
create sequence SEQ_ORDEREXT
minvalue 1
maxvalue 100000000
start with 1663
increment by 1
cache 20
cycle;

prompt
prompt Creating sequence SEQ_TRADE
prompt ===========================
prompt
create sequence SEQ_TRADE
minvalue 1
maxvalue 999999999999999999999999999
start with 1160
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TRADE_ACCOUNT_COMMAND
prompt ===========================================
prompt
create sequence SEQ_TRADE_ACCOUNT_COMMAND
minvalue 1
maxvalue 999999999999999999999999999
start with 1775
increment by 1
cache 20;

prompt
prompt Creating sequence SEQ_TRADE_NO
prompt ==============================
prompt
create sequence SEQ_TRADE_NO
minvalue 1
maxvalue 9999999
start with 449
increment by 1
cache 20;

prompt
prompt Creating view GWCHANNEL
prompt =======================
prompt
create or replace view gwchannel as
select m.*,m.date_created as createdate,upper(code) as ACQUIRE_CODE,bank_account_no as ACQUIRE_ACCTID, inner_acount_no as ACCOUNT_ID from
boss.bo_merchant m inner join boss.bo_acquirer_account a on m.acquirer_account_id=a.id
inner join boss.bo_bank_dic d on a.bank_id=d.id;

prompt
prompt Creating view GWVIEWACCOUNT
prompt ===========================
prompt
create or replace view gwviewaccount as
select 0 AS USER_ID,0 AS USER_TYPE,'' AS LOGIN_RECEPIT,'' AS LOGIN_PWD,'' AS PAY_PWD,0 AS ACCT_AMOUNT,0 AS ACCT_BALANCE,
  '' AS ACCT_STS,'' AS ACCT_ID,'' AS ACCT_NAME    from DUAL;

prompt
prompt Creating view GWVIEWUSER
prompt ========================
prompt
create or replace view gwviewuser as
select c.ID,API_KEY,c.CUSTOMER_NO,b.ENABLE,c.status as "STATUS",c.name as "NAME",b.SERVICE_CODE,t.LOGIN_CERTIFICATE AS "LOGIN_RECEPIT" from
CM_CUSTOMER c INNER JOIN CM_CUSTOMER_OPERATOR o on c.id=o.customer_id
inner join cm_login_certificate t on o.id=t.CUSTOMER_OPERATOR_ID
left join BOSS.BO_CUSTOMER_SERVICE b ON c.id=b.customer_id;

prompt
prompt Creating procedure PROC_CHANNELVIEWS
prompt ====================================
prompt
create or replace procedure PROC_CHANNELVIEWS(orderid in varchar2,slaresult out varchar2) is
cursor channels is select * from gwchannel g where g.channel_sts=0;
begin
     -- todo with the business type and partner service;
     -- format B2C{CMB:1,BOC:2};B2B{}
     slaresult:='B2C{';
     for x in channels loop
         slaresult:=slaresult||x.acquire_code||':'||x.id||',';
     end loop;
     slaresult:=substr(slaresult,1,length(slaresult)-1);
     slaresult:=slaresult||'}';

end PROC_CHANNELVIEWS;
/

prompt
prompt Creating procedure PROC_DECISION
prompt ================================
prompt
create or replace procedure PROC_DECISION(v_order_id in varchar,
                                             v_acquire_code  in varchar2,
                                             v_acquire_merchant in varchar2,
                                             v_code out varchar2) is
/* check wheather the trx can be generate or not*/
v_line number;
v_max number;
v_flag number;
cursor orderref is select * from gworders where id=v_order_id;
begin
     v_max:=1;
     for xref in orderref loop
       If xref.ordersts<>'0' THEN
          IF xref.ordersts='1' or xref.ordersts='2' or xref.ordersts=3 THEN --PAID SUCCESS
              v_code:='501127';
          ELSE
              v_code:='530003';
          END IF;
       Else
         SELECT COUNT(*) INTO v_line FROM GWTRXS where gworders_id=xref.id;
         IF v_line>=v_max THEN  ---REPEAT LIMITATION
            v_code:='700001';  --
         ELSE
            IF v_line>0 THEN
              --TODO OTHER CHECK
              v_code:='0';
            ELSE  -- ENABLE
              v_code:='0';
            END IF;
         END IF;
         --ORDER IS CLOSED OR NOT
         select case when
                     sysdate<(xref.createdate+to_number(
                     case when instr(xref.exp_dates,'d')>0 then to_number(substr(xref.exp_dates,0,length(xref.exp_dates)-1))*24*60*60
                          when instr(xref.exp_dates,'h')>0 then to_number(substr(xref.exp_dates,0,length(xref.exp_dates)-1))*60*60
                          when instr(xref.exp_dates,'m')>0 then to_number(substr(xref.exp_dates,0,length(xref.exp_dates)-1))*60
                          when instr(xref.exp_dates,'s')>0 then to_number(substr(xref.exp_dates,0,length(xref.exp_dates)-1))/(24*60*60)
                          else 0
                     end)) then 0
                     else 1
                end into v_flag  from dual;
         if v_flag=1 then
            update gworders set ordersts='4' where id=xref.id;
            commit;
            v_code:='530006'; --order closed
         end if;
        End IF;

     end loop;



     exception
       when others then
            Dbms_Output.put_line (sqlerrm);
            v_code:='530004'; --  CHECK EXCEPTION
end PROC_DECISION;
/

prompt
prompt Creating procedure PROC_POSTINS
prompt ===============================
prompt
create or replace procedure PROC_POSTINS(v_root_id in number, rescode out varchar, resmsg out varchar)
  is
/*generate the account instrucation */
cursor todolist is
       select b.* from TRADE_BASE b left join TRADE_ACCOUNT_COMMAND_SAF c on b.trade_no=c.trade_no
       where root_id=v_root_id and c.id is null order by decode(b.trade_type,'charge',0,'payment',1,'transfer',2) asc;

v_sub_seqno number(10);
c_payee_acctno varchar(24);
c_payee_name varchar(128);
v_seq varchar2(40);
v_command_no varchar2(40);

begin
   v_sub_seqno:=-1;
   select '90'||to_char(sysdate,'yymmdd')||lpad(SEQ_TRADE_ACCOUNT_COMMAND.NEXTVAL,9,0)||v_root_id
   into v_command_no from dual;
   for cmd in todolist loop
       v_sub_seqno:=v_sub_seqno+1;
       select SEQ_TRADE_ACCOUNT_COMMAND.NEXTVAL into v_seq from dual;
       insert into trade_account_command_SAF
         (id, version, command_no, command_type,resp_code,sync_flag,sync_time, trans_code, trans_id, amount, currency, from_account_no, subject,
          out_trade_no, sub_seqno, to_account_no, trade_id, trade_no, transfer_type,redo_count,redo_flag,date_created,last_updated)
       values
         (v_seq, 0, v_command_no, 'transfer',null,'F', sysdate,
          null, null,
          cmd.amount, cmd.currency, cmd.payer_account_no,
          cmd.note, cmd.out_trade_no, v_sub_seqno,
          cmd.payee_account_no, cmd.id,cmd.trade_no, cmd.trade_type,0,'T',sysdate,sysdate);

       if cmd.trade_type='payment' then -- generate ext fee instruction
          select ACCOUNT_NO,note into c_payee_acctno,c_payee_name
          from boss.bo_inner_account where key='feeAcc';
          select SEQ_TRADE_ACCOUNT_COMMAND.NEXTVAL into v_seq from dual;
           v_sub_seqno:=v_sub_seqno+1;

         insert into trade_account_command_saf
         (id, version, command_no, command_type,resp_code, sync_flag,sync_time, trans_code, trans_id,
          amount, currency, from_account_no, subject,
          out_trade_no, sub_seqno, to_account_no, trade_id, trade_no, transfer_type,redo_count,redo_flag,date_created,last_updated)
         values(
           v_seq,0,v_command_no,'transfer',null,'F',sysdate,null,null,cmd.fee_amount,cmd.currency,cmd.payee_account_no,
           '手续费',
           cmd.out_trade_no,v_sub_seqno,c_payee_acctno,cmd.id,cmd.trade_no||'01','fee',0,'T',sysdate,sysdate);
        end if;
   end loop;

   rescode:='0';
   resmsg:=v_command_no;
   exception
     when others then
       dbms_output.put_line(TO_CHAR(SQLCODE)||SQLERRM);
       rescode:=-1;
       resmsg:=TO_CHAR(SQLCODE)||SQLERRM;

end PROC_POSTINS;
/

prompt
prompt Creating procedure PROC_POSTTRX
prompt ===============================
prompt
create or replace procedure PROC_POSTTRX(v_trxid in varchar, v_step out varchar,v_result out varchar) is
cursor listtrxs is select t.* from gwtrxs t left join gworders d on t.gworders_id=d.id
                        where t.id=v_trxid;

cursor orders(v_id varchar2) is select * from gworders where id=v_id and ordersts=3;
cursor previoustrx(v_tid varchar2) is select * from trade_base where trade_no=v_tid;
v_gworder_id varchar2(30);
v_id       number(19);
v_root_id  number;
v_count    number;
i_partner_id number(19);
v_fee_amount number;

c_buyer_id       number(19);
c_buyer_acctno   varchar2(30);
c_buyer_name     varchar2(30);

c_seller_id      number(19);
c_payee_acctno  varchar2(30);
c_seller_name    varchar2(30);
v_compamount number;

/*
tranaction engineer
v_step [0: posttrx success][1:generate instruction success] [-1:failure]
v_seq[root_id|command_no|sqlerrmsg]
*/

begin
  --insert root trx
   v_root_id:=0;
   select gworders_id  into v_gworder_id   from gwtrxs where id=v_trxid;

   for y in orders(v_gworder_id) loop
       select SEQ_TRADE.Nextval into v_root_id from dual;
       select ID,NAME into c_seller_id,c_seller_name from cm_customer where customer_no=(select seller_id from
             gworders where id=v_gworder_id);
       select ACCOUNT_NO into c_payee_acctno from cm_customer where customer_no=y.partnerid;
       select count(*) into v_count from cm_customer where customer_no=y.buyer_id;
       if v_count!=0 then
         select ACCOUNT_NO,NAME into c_buyer_acctno,c_buyer_name from cm_customer WHERE customer_no=y.buyer_id;
       else
         select ACCOUNT_NO,y.buyer_name into c_buyer_acctno,c_buyer_name
         from boss.bo_inner_account where key='guestAcc';
       end if;

       select count(*) into v_count from trade_base where trade_no=y.id;
       select id into i_partner_id from cm_customer where customer_no=y.partnerid;
       if v_count=0 then
         --compute the ext_fee
         select round((nvl(fee_params,1)*y.amount/100),0)  into v_fee_amount from cm_customer cc inner join boss.bo_customer_service bcs on cc.id=bcs.customer_id
         where cc.id=i_partner_id and bcs.service_code='online' and is_current=1 and enable=1;
         
         if v_fee_amount=0 then 
           v_fee_amount:=1;
         end if;
         
         insert into trade_base
         (id, version, amount, currency, date_created, last_updated, note, original_id, out_trade_no, partner_id,
          payee_account_no, payee_code, payee_id, payee_name, payer_account_no, payer_code, payer_id, payer_name, root_id, status, subject, trade_date, trade_no, trade_type,fee_amount)
         values
         (v_root_id, 0, y.amount, y.currency, y.createdate, y.closedate, y.buyer_name, null, y.ordernum,i_partner_id,
          c_payee_acctno, y.seller_name,c_seller_id, c_seller_name,
          c_buyer_acctno, y.buyer_name, nvl(c_buyer_id,null),c_buyer_name,
          v_root_id, decode(y.ordersts,'3','completed','0','starting','4','closed','processing'), y.subject, to_number(to_char(y.closedate,'yyyyMMdd')), y.id, 'payment',
          v_fee_amount);

         insert into trade_payment
           (id, payment_request_id, refund_amount, frozen_amount, payment_ip, show_url, body, out_royalty_trade_no, royalty_type, royalty_params, royalty_status)
         values
           (v_root_id, y.id, 0, 0, y.ips, y.show_url, y.bodys, y.ordernum, y.royalty_type, y.royalty_parameters,  'starting');
       else
         select root_id into v_root_id from trade_base where trade_no=y.id;
       end if;

  end loop;

  for x in listtrxs loop

    if x.trxsts=1 then --success trx
       select count(*) into v_count from cm_customer where customer_no=x.buyer_id;
       if v_count!=0 then
         select ID, ACCOUNT_NO,NAME into c_buyer_id,c_buyer_acctno,c_buyer_name from cm_customer WHERE customer_no=x.buyer_id;
       else
          select ACCOUNT_NO into c_buyer_acctno
          from boss.bo_inner_account where key='guestAcc';
       end if;
      select SEQ_TRADE.Nextval into v_id from dual;
      if v_root_id is null or v_root_id=0 then
          v_root_id:=v_id;
      end if;
      select count(*) into v_count from  trade_base where trade_no=x.id;
      if v_count=0 then
        insert into trade_base
          (id, version, amount, currency,date_created, last_updated, note, ORIGINAL_ID, out_trade_no, partner_id,
           payee_account_no, payee_code, payee_id,payee_name,
           payer_account_no, payer_code, payer_id,payer_name,
           root_id,fee_amount,
           status, subject, trade_date, trade_no, trade_type)
        values
          (v_id, 0, x.amount,x.currency, x.createdate, x.closedate, x.acquirer_name||':'||x.trxnum, v_root_id, x.trxnum, null,
           c_buyer_acctno, x.buyer_name, c_buyer_id,c_buyer_name,
           x.fromacctnum, null, x.fromacctid,x.acquirer_name,
           v_root_id,0,
           decode(x.trxsts,'1','completed'), x.trxnum||'('||x.acquirer_name||')',to_char(x.closedate,'yyyymmdd'), x.id, 'charge');

        insert into trade_charge(id, added_method, back_amount, funding_source, is_credit_card, payment_ip, payment_request_id)
        values(v_id,  x.acquirer_name, 0, 'ONLINE', 0,x.payer_ip, x.gworders_id);
      end if;
    end if;


    for y in orders(x.gworders_id) loop
      select count(*) into v_count from trade_base where trade_no=y.id;
      select SEQ_TRADE.Nextval into v_id from dual;
      if x.trxsts=1 and v_count>0 and x.amount>=y.amount then --wirte payment trx
        if y.ordersts<>'completed' then
           update trade_base set status='completed',version=version+1
            where id=v_root_id and version=y.version and trade_no=y.id;
        end if;
      else
          --write payment refundTrx
          /*
          if v_count>0 then
             for r in previoustrx(x.id) loop
               insert into trade_base
                 (id, version, amount, currency,date_created, last_updated, note, ORIGINAL_ID, out_trade_no, partner_id, payee_account_no, payee_code, payee_id, payer_account_no, payer_code, payer_id,
                   root_id, status, subject, trade_date, trade_no, trade_type)
               values
               (v_id,0,r.amount,r.currency,sysdate,sysdate,'重复交易',r.id,x.trxnum,null,r.payer_account_no,r.payer_code,r.payer_id,r.payee_account_no,r.payee_code,r.payee_id,
                  r.root_id,'starting',x.trxnum||'(重复交易退款)',to_number(to_char(sysdate,'yyyyMMdd')),r.trade_no||x.trxnum,'refund');

              insert into trade_refund
                 (id, acquirer_code, acquirer_merchant_no, back_fee, check_date, check_operator_id, check_status, customer_oper_id, from_bank_account_no, from_bank_code, from_bank_no, handle_batch, handle_oper_id, handle_oper_name, handle_status, handle_time, real_refund_amount, submit_batch, submit_type, submitter)
               values
                 (v_root_id, v_acquirer_code, v_acquirer_merchant_no, v_back_fee, v_check_date, v_check_operator_id, v_check_status, v_customer_oper_id, v_from_bank_account_no, v_from_bank_code, v_from_bank_no, v_handle_batch, v_handle_oper_id, v_handle_oper_name, v_handle_status, sysdate, v_real_refund_amount, v_submit_batch, v_submit_type, v_submitter);

             end loop;
          else
              --composit payment
              select sum(amount) into v_compamount from trade_base b inner join trade_charge c on b.id=c.id where c.payment_request_id=y.id;
              if v_compamount>=y.amount then
                 update trade_base set status='completed',version=version+1 where id=v_root_id and trade_no=y.id;

              end if;

          end if;
          */
          null;
      end if;
    end loop;
    v_step:='0';
    v_result:=v_root_id;
    proc_postins(v_root_id,v_step,v_result);
    if v_step=0 then
      v_step:=1;
    end if;
  end loop;
  commit;
  Exception
      when others then
           dbms_output.put_line(TO_CHAR(SQLCODE)||SQLERRM);
           v_step:=-1;
           v_result:=TO_CHAR(SQLCODE)||SQLERRM;
           rollback;
end PROC_POSTTRX;
/

prompt
prompt Creating procedure PROC_ROUTECHANNEL
prompt ====================================
prompt
create or replace procedure PROC_ROUTECHANNEL(v_orderid in varchar, v_indexc in varchar2,v_channelid out varchar2)
is
cursor thisorder(orderid varchar) is select * from gworders where id=orderid;
--get the optimistc channel for the order
begin
  v_channelid:=0;
  if v_orderid<>'' then
    for x in thisorder(v_orderid) loop
        select id into v_channelid from (
               select * from gwchannel g where (g.acquire_indexc=v_indexc or g.acquire_code=v_indexc or g.acquire_indexc='UNP-'||v_indexc)
               and x.order_type=g.channel_type and x.amount<=g.qutor and channel_sts=0 order by g.qutor asc
        ) where rownum=1;
    end loop;
  else -- direct charge
        select id into v_channelid from (
               select * from gwchannel g where (g.acquire_indexc=v_indexc or g.acquire_code=v_indexc or g.acquire_indexc='UNP-'||v_indexc)
               and 1=g.channel_type and channel_sts=0 order by g.qutor asc
        ) where rownum=1;
  end if;

  EXCEPTION
    when others then
      v_channelid:=0;

end PROC_ROUTECHANNEL;
/

prompt
prompt Creating procedure PROC_UNLOCK_CMOPERATOR
prompt =========================================
prompt
create or replace procedure PROC_UNLOCK_CMOPERATOR is
begin

       update cm_customer_operator set status='normal',login_error_time=0 where status='locked';

end PROC_UNLOCK_CMOPERATOR;
/


spool off
